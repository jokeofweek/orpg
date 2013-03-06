package orpg.server.data.entity;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.PlayerManager;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;

import orpg.server.BaseServer;
import orpg.shared.Constants;
import orpg.shared.component.IsPlayer;
import orpg.shared.component.Named;
import orpg.shared.component.Position;
import orpg.shared.component.Renderable;
import orpg.shared.data.AccountCharacter;

public class EntityFactory {

	private BaseServer baseServer;
	private World world;

	public EntityFactory(BaseServer baseServer, World world) {
		this.baseServer = baseServer;
		this.world = world;
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
