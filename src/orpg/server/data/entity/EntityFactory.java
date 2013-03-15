package orpg.server.data.entity;

import java.util.logging.Level;

import orpg.server.BaseServer;
import orpg.server.data.Account;
import orpg.shared.Constants;
import orpg.shared.data.AccountCharacter;
import orpg.shared.data.component.BasicCollidable;
import orpg.shared.data.component.Collidable;
import orpg.shared.data.component.IsPlayer;
import orpg.shared.data.component.Moveable;
import orpg.shared.data.component.Named;
import orpg.shared.data.component.Position;
import orpg.shared.data.component.Renderable;

import com.artemis.ComponentMapper;
import com.artemis.ComponentType;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.PlayerManager;
import com.artemis.utils.ImmutableBag;

public class EntityFactory {

	private BaseServer baseServer;
	private World world;

	private ComponentType collideableType;

	private ComponentMapper<Position> positionMapper;
	private ComponentMapper<Named> namedMapper;
	private ComponentMapper<IsPlayer> isPlayerMapper;
	private ComponentMapper<Moveable> moveableMapper;

	public EntityFactory(BaseServer baseServer, World world) {
		this.baseServer = baseServer;
		this.world = world;
		this.collideableType = ComponentType.getTypeFor(Collidable.class);

		// Load the mappers
		this.positionMapper = world.getMapper(Position.class);
		this.namedMapper = world.getMapper(Named.class);
		this.isPlayerMapper = world.getMapper(IsPlayer.class);
		this.moveableMapper = world.getMapper(Moveable.class);
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
		entity.addComponent(BasicCollidable.BLOCKING);

		// Add the player to the players group
		GroupManager groups = world.getManager(GroupManager.class);
		groups.add(entity, Constants.GROUP_PLAYERS);
		world.getManager(PlayerManager.class).setPlayer(entity,
				character.getName());

		world.addEntity(entity);

		return entity;
	}

	public void updateEntityAccountCharacter(String accountName,
			Entity entity) {
		// Make sure the entity is a player
		if (isPlayerMapper.getSafe(entity) == null) {
			baseServer
					.getConfigManager()
					.getErrorLogger()
					.log(Level.INFO,
							"Tried to update account character for non-player entity.");
			return;
		}

		// Get current account character
		Account account = baseServer.getAccountController().get(
				accountName);
		String characterName = namedMapper.get(entity).getName();

		// Find the character
		AccountCharacter foundCharacter = null;
		for (AccountCharacter character : account.getCharacters()) {
			if (character.getName().equals(characterName)) {
				foundCharacter = character;
				break;
			}
		}

		if (foundCharacter == null) {
			baseServer
					.getConfigManager()
					.getErrorLogger()
					.log(Level.SEVERE,
							"Tried to update account character '"
									+ characterName
									+ "', but character does not exist for account '"
									+ accountName + "'.");
			return;
		}

		// Update the found character
		Position position = positionMapper.get(entity);
		foundCharacter.setMap(baseServer.getMapController().get(
				position.getMap()));
		foundCharacter.setX(position.getX());
		foundCharacter.setY(position.getY());
		
		Moveable moveable = moveableMapper.get(entity);
		foundCharacter.setDirection(moveable.getDirection());	
	}
}
