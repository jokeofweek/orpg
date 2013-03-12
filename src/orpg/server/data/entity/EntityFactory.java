package orpg.server.data.entity;

import com.artemis.ComponentType;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.PlayerManager;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;

import orpg.server.BaseServer;
import orpg.shared.Constants;
import orpg.shared.data.AccountCharacter;
import orpg.shared.data.component.BasicCollideable;
import orpg.shared.data.component.Collideable;
import orpg.shared.data.component.IsPlayer;
import orpg.shared.data.component.Moveable;
import orpg.shared.data.component.Named;
import orpg.shared.data.component.Position;
import orpg.shared.data.component.Renderable;
import sun.awt.CausedFocusEvent.Cause;

public class EntityFactory {

	private BaseServer baseServer;
	private World world;

	private ComponentType collideableType;

	public EntityFactory(BaseServer baseServer, World world) {
		this.baseServer = baseServer;
		this.world = world;
		this.collideableType = ComponentType.getTypeFor(Collideable.class);
	}

	public Entity addAccountCharacterEntity(AccountCharacter character) {
		// Make sure the character doesn't already exist in the world
		if (world.getManager(PlayerManager.class)
				.getEntitiesOfPlayer(character.getName()).size() > 0) {
			throw new IllegalArgumentException("The character "
					+ character.getName()
					+ " already exists in the world.");
		}

		// Create the entity
		Entity entity = world.createEntity();
		entity.addComponent(new Position(character.getId(), character
				.getX(), character.getY()));
		entity.addComponent(new Renderable(character.getSprite()));
		entity.addComponent(new Named(character.getName()));
		entity.addComponent(IsPlayer.getInstance());
		Moveable moveable = new Moveable();
		moveable.setDirection(character.getDirection());
		entity.addComponent(moveable);
		entity.addComponent(BasicCollideable.BLOCKING, collideableType);

		// Add the player to the players group
		GroupManager groups = world.getManager(GroupManager.class);
		groups.add(entity, Constants.GROUP_PLAYERS);
		world.getManager(PlayerManager.class).setPlayer(entity,
				character.getName());

		world.addEntity(entity);

		return entity;
	}

	public void removeAccountCharacterEntity(AccountCharacter character) {
		ImmutableBag<Entity> matchingEntities = world.getManager(
				PlayerManager.class).getEntitiesOfPlayer(
				character.getName());

		// If we have a match, remove it from the world
		for (int i = 0; i < matchingEntities.size(); i++) {
			removeEntity(matchingEntities.get(i));
		}
	}

	public void removeEntity(Entity entity) {
		entity.getWorld().deleteEntity(entity);
	}

}
