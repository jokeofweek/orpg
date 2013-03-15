package orpg.server;

import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.PlayerManager;
import com.artemis.managers.TagManager;
import com.artemis.utils.ImmutableBag;

import orpg.server.data.Account;
import orpg.server.data.SessionType;
import orpg.server.event.EntityLeaveGameEvent;
import orpg.server.event.EntityLeaveMapEvent;
import orpg.server.event.EntityWarpToPositionMovementEvent;
import orpg.server.net.packets.ClientInGamePacket;
import orpg.server.net.packets.EditorLoginOkPacket;
import orpg.server.net.packets.ErrorPacket;
import orpg.server.net.packets.LoginOkPacket;
import orpg.server.systems.MovementSystem;
import orpg.shared.ErrorMessage;
import orpg.shared.data.AccountCharacter;

public class ServerSession {

	private BaseServer baseServer;
	private Queue<byte[]> outputQueue;
	private SessionType sessionType;
	private volatile boolean connected;
	private String disconnectReason;
	private ServerSessionThread thread;
	private World world;

	private Account account;
	private String characterName;
	private Entity entity;
	private boolean isWaitingForMap;

	private String originalId;
	private String id;

	public ServerSession(BaseServer baseServer, Socket socket)
			throws SocketException {
		this.baseServer = baseServer;
		this.outputQueue = new LinkedList<byte[]>();
		this.setSessionType(SessionType.ANONYMOUS);

		this.originalId = socket.getInetAddress().toString();
		this.id = socket.getInetAddress().toString();

		// Start the session thread
		this.thread = new ServerSessionThread(baseServer, socket, this);
		this.thread.start();

		this.connected = true;
	}

	public String getId() {
		return id + "/" + sessionType;
	}

	public void setSessionType(SessionType sessionType) {
		baseServer
				.getConfigManager()
				.getSessionLogger()
				.log(Level.INFO,
						String.format(
								"Session %s changed session type from %s to %s",
								getId(), this.sessionType, sessionType));
		this.sessionType = sessionType;
	}

	public SessionType getSessionType() {
		return sessionType;
	}

	public Queue<byte[]> getOutputQueue() {
		return outputQueue;
	}

	public boolean isConnected() {
		return connected;
	}

	public void disconnect() {
		disconnect("Client disconnect.");
	}

	public void preventativeDisconnect(String reason) {
		baseServer
				.getConfigManager()
				.getErrorLogger()
				.log(Level.WARNING,
						"Session "
								+ getId()
								+ " preventatively disconnected for reason "
								+ reason + ".");
		disconnect("Preventative disconnect.");
	}

	public void disconnect(String reason) {
		// If the player was in game, remove them from the map
		if (getSessionType() == SessionType.GAME) {
			ImmutableBag<Entity> playerEntities = baseServer.getWorld()
					.getManager(PlayerManager.class)
					.getEntitiesOfPlayer(getCharacterName());
			MovementSystem movementSystem = baseServer.getWorld()
					.getSystem(MovementSystem.class);
			for (int i = 0; i < playerEntities.size(); i++) {
				if (playerEntities.get(i) != null) {
					movementSystem.addEvent(new EntityLeaveGameEvent(
							playerEntities.get(i)));
				}
			}

			// Save the account
			baseServer.getAccountController().save(getAccount().getName());
		}

		baseServer
				.getConfigManager()
				.getSessionLogger()
				.log(Level.INFO,
						String.format(
								"Session %s disconnected for reason %s.",
								getId(), reason));
		baseServer.getServerSessionManager().removeSession(this);

		this.connected = false;
		this.disconnectReason = reason;

	}

	public String getDisconnectReason() {
		return disconnectReason;
	}

	public Account getAccount() {
		return account;
	}

	public String getCharacterName() {
		return characterName;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public void login(Account account, SessionType sessionType) {
		if (sessionType != SessionType.EDITOR
				&& sessionType != SessionType.LOGGED_IN) {
			baseServer
					.getConfigManager()
					.getErrorLogger()
					.log(Level.SEVERE,
							"Session " + getId()
									+ " attempted to login to "
									+ account.getName()
									+ " while not in the correct state.");
			return;
		}

		this.account = account;
		// Update the id to include account name
		this.id = this.originalId + "(" + account.getName() + ")";

		this.setSessionType(sessionType);

		// If our login was successful, notify the session manager
		baseServer.getServerSessionManager().registerAccountSession(this);

		if (sessionType == SessionType.EDITOR) {
			baseServer.sendPacket(new EditorLoginOkPacket(this));
		} else if (sessionType == SessionType.LOGGED_IN) {
			baseServer.sendPacket(new LoginOkPacket(this, account));
		}

		baseServer
				.getConfigManager()
				.getSessionLogger()
				.log(Level.INFO,
						String.format("Session %s sucesfully logged in.",
								this.getId()));

	}

	public void useCharacter(String characterName) {
		if (sessionType != SessionType.LOGGED_IN) {
			baseServer
					.getConfigManager()
					.getErrorLogger()
					.log(Level.SEVERE,
							"Session " + getId()
									+ " attempted to select character "
									+ characterName
									+ " while not in the correct state.");
			return;
		}

		// Just a sanity test, make sure this character is in the account's
		// characters
		boolean found = false;
		AccountCharacter selectedCharacter = null;
		for (AccountCharacter accountCharacter : account.getCharacters()) {
			if (accountCharacter.getName().equals(characterName)) {
				found = true;
				selectedCharacter = accountCharacter;
				break;
			}
		}

		if (!found) {
			baseServer
					.getConfigManager()
					.getErrorLogger()
					.log(Level.WARNING,
							"Session "
									+ getId()
									+ " attempted to select character "
									+ characterName
									+ " while logged into a non-owning account "
									+ getAccount().getName() + ".");
			baseServer.sendPacket(new ErrorPacket(this,
					ErrorMessage.GENERIC_USE_CHARACTER_ERROR));
			return;
		}

		// Now we register the in-game session.
		this.characterName = characterName;
		this.sessionType = SessionType.GAME;
		if (baseServer.getServerSessionManager().registerInGameSession(
				this)) {
			// Update the id to include character name
			this.id = this.originalId + "(" + account.getName() + ":"
					+ characterName + ")";

			baseServer
					.getConfigManager()
					.getSessionLogger()
					.log(Level.INFO,
							String.format(
									"Session %s sucesfully selected character and entered game.",
									this.getId()));

			// Create the entity
			Entity entity = baseServer.getEntityFactory()
					.addAccountCharacterEntity(selectedCharacter);
			this.setWorld(entity.getWorld());
			this.setEntity(entity);
			this.baseServer.getServerSessionManager()
					.registerSessionEntity(this);
			this.setWaitingForMap(true);

			// Notify the client that they are now in the game
			baseServer
					.sendPacket(new ClientInGamePacket(this,
							selectedCharacter, baseServer
									.getAutoTileController()));

			baseServer
					.getWorld()
					.getSystem(MovementSystem.class)
					.addEvent(
							new EntityWarpToPositionMovementEvent(entity,
									selectedCharacter.getMap().getId(),
									selectedCharacter.getX(),
									selectedCharacter.getY()));
			;
		} else {
			// Revert the account back to original state
			this.characterName = null;
			this.sessionType = SessionType.LOGGED_IN;
			baseServer.sendPacket(new ErrorPacket(this,
					ErrorMessage.CHARACTER_IN_USE));
		}
	}

	public boolean isWaitingForMap() {
		return isWaitingForMap;
	}

	public void setWaitingForMap(boolean isWaitingForMap) {
		this.isWaitingForMap = isWaitingForMap;
	}
}
