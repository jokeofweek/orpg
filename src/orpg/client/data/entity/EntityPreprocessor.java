package orpg.client.data.entity;

import orpg.client.BaseClient;
import orpg.client.ClientConstants;
import orpg.shared.component.IsPlayer;
import orpg.shared.component.Named;
import orpg.shared.component.Position;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityManager;
import com.artemis.EntityObserver;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.managers.PlayerManager;

public class EntityPreprocessor extends EntityManager {

	private BaseClient baseClient;
	
	private ComponentMapper<Position> positionMapper;
	private ComponentMapper<Named> namedMapper;
	private ComponentMapper<IsPlayer> isPlayerMapper;

	public EntityPreprocessor(BaseClient baseClient) {
		this.baseClient = baseClient;

		this.positionMapper = baseClient.getWorld().getMapper(Position.class);
		this.namedMapper = baseClient.getWorld().getMapper(Named.class);
		this.isPlayerMapper = baseClient.getWorld().getMapper(IsPlayer.class);
	}

	@Override
	public void added(Entity e) {
		// Add groups based on components
		Position position = positionMapper.getSafe(e);

		if (position != null) {
			GroupManager groupManager = baseClient.getWorld().getManager(
					GroupManager.class);

			// Register the map / segment groups
			groupManager.add(e, ClientConstants.GROUP_MAP);
			groupManager.add(e, String.format(
					ClientConstants.GROUP_MAP_SEGMENT, baseClient.getMap()
							.getSegmentX(position.getX()), baseClient
							.getMap().getSegmentY(position.getY())));

			if (isPlayerMapper.getSafe(e) != null) {
				groupManager.add(e, ClientConstants.GROUP_PLAYERS);
				baseClient.getWorld().getManager(PlayerManager.class)
						.setPlayer(e, namedMapper.get(e).getName());
			}
		}
	}
}
