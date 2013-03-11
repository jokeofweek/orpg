package orpg.client.data.entity;

import orpg.client.BaseClient;
import orpg.client.ClientConstants;
import orpg.client.data.component.AnimatedPlayer;
import orpg.client.data.component.HandlesInput;
import orpg.client.systems.AnimationSystem;
import orpg.client.systems.MovementSystem;
import orpg.shared.data.Direction;
import orpg.shared.data.component.Collideable;
import orpg.shared.data.component.IsPlayer;
import orpg.shared.data.component.Named;
import orpg.shared.data.component.Position;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityManager;
import com.artemis.EntityObserver;
import com.artemis.World;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.managers.PlayerManager;

public class EntityPreprocessor extends EntityManager {

	private BaseClient baseClient;

	private ComponentMapper<Position> positionMapper;
	private ComponentMapper<Named> namedMapper;
	private ComponentMapper<IsPlayer> isPlayerMapper;
	private ComponentMapper<Collideable> collideableMapper;

	private GroupManager groupManager;
	private PlayerManager playerManager;

	public EntityPreprocessor(BaseClient baseClient) {
		this.baseClient = baseClient;

		World world = baseClient.getWorld();

		this.positionMapper = world.getMapper(Position.class);
		this.namedMapper = world.getMapper(Named.class);
		this.isPlayerMapper = world.getMapper(IsPlayer.class);
		this.collideableMapper = world.getMapper(Collideable.class);

		this.groupManager = world.getManager(GroupManager.class);
		this.playerManager = world.getManager(PlayerManager.class);
	}

	@Override
	public void added(Entity e) {
		if (isPlayerMapper.getSafe(e) != null) {
			groupManager.add(e, ClientConstants.GROUP_PLAYERS);
			playerManager.setPlayer(e, namedMapper.get(e).getName());

			e.addComponent(new AnimatedPlayer());

			// If the entity is the base client's player, add the input
			// component
			if (namedMapper.get(e).getName()
					.equals(baseClient.getAccountCharacter().getName())) {
				e.addComponent(new HandlesInput());
			}

			e.changedInWorld();
		}

		if (collideableMapper.getSafe(e) != null) {
			groupManager.add(e, ClientConstants.GROUP_COLLIDEABLE);
		}

		baseClient.getWorld().getSystem(MovementSystem.class)
				.updateEntitySegment(e, -1, -1);

	}

}
