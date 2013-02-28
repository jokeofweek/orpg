package orpg.server;

import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;

import orpg.server.data.Account;
import orpg.server.data.SessionType;
import orpg.server.net.packets.ClientInGamePacket;
import orpg.server.net.packets.EditorLoginOkPacket;
import orpg.server.net.packets.ErrorPacket;
import orpg.server.net.packets.LoginOkPacket;
import orpg.shared.ErrorMessage;
import orpg.shared.data.AccountCharacter;

public class ServerSession {

	private BaseServer baseServer;
	private Queue<byte[]> outputQueue;
	private SessionType sessionType;
	private volatile boolean connected;
	private String disconnectReason;
	private ServerSessionThread thread;

	private Account account;
	private AccountCharacter character;

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
						"Session " + getId()
								+ " preventatively disconnected for reason "
								+ reason + ".");
		disconnect("Preventative disconnect.");
	}

	public void disconnect(String reason) {
		// If the player was in game, remove them from the map
		if (getSessionType() == SessionType.GAME) {
			baseServer.getMapController().leaveMap(getCharacter());

			// Save the account
			baseServer.getAccountController().save(getAccount().getName());
		}

		baseServer
				.getConfigManager()
				.getSessionLogger()
				.log(Level.INFO,
						String.format("Session %s disconnected for reason %s.",
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

	public AccountCharacter getCharacter() {
		return character;
	}

	public void login(Account account, SessionType sessionType) {
		if (sessionType != SessionType.EDITOR
				&& sessionType != SessionType.LOGGED_IN) {
			baseServer
					.getConfigManager()
					.getErrorLogger()
					.log(Level.SEVERE,
							"Session " + getId() + " attempted to login to "
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

	public void useCharacter(AccountCharacter character) {
		if (sessionType != SessionType.LOGGED_IN) {
			baseServer
					.getConfigManager()
					.getErrorLogger()
					.log(Level.SEVERE,
							"Session " + getId()
									+ " attempted to select character "
									+ character.getName()
									+ " while not in the correct state.");
			return;
		}

		// Just a sanity test, make sure this character is in the account's
		// characters
		boolean found = false;
		for (AccountCharacter accountCharacter : account.getCharacters()) {
			if (character == accountCharacter) {
				found = true;
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
									+ character.getName()
									+ " while logged into a non-owning account "
									+ getAccount().getName() + ".");
			baseServer.sendPacket(new ErrorPacket(this,
					ErrorMessage.GENERIC_USE_CHARACTER_ERROR));
			return;
		}

		// Now we register the in-game session.
		this.character = character;
		this.sessionType = SessionType.GAME;
		if (baseServer.getServerSessionManager().registerInGameSession(this)) {
			// Update the id to include character name
			this.id = this.originalId + "(" + account.getName() + ":"
					+ character.getName() + ")";

			baseServer
					.getConfigManager()
					.getSessionLogger()
					.log(Level.INFO,
							String.format(
									"Session %s sucesfully selected character and entered game.",
									this.getId()));

			// Setup the character
			this.character.setChangingMap(true);

			// Notify the client that they are now in the game
			baseServer.sendPacket(new ClientInGamePacket(this, character));

			baseServer.getMapController().warpToMap(character,
					character.getMap().getId(), character.getX(),
					character.getY());
		} else {
			// Revert the account back to original state
			this.character = null;
			this.sessionType = SessionType.LOGGED_IN;
			baseServer.sendPacket(new ErrorPacket(this,
					ErrorMessage.CHARACTER_IN_USE));
		}
	}
}
