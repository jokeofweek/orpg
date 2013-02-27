package orpg.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;

import orpg.server.data.SessionType;
import orpg.server.net.packets.ConnectedPacket;
import orpg.server.net.packets.ServerPacket;

/**
 * The server sessions manager is responsible for keeping track of the current
 * active sessions. It also takes care of dispatching all packets in the
 * outputQueue, routing them based on their destination types.
 * 
 * @author Dominic Charley-Roy
 */
public class ServerSessionManager implements Runnable {

	private HashSet<ServerSession> sessions;
	private BaseServer server;
	private BlockingQueue<ServerPacket> outputQueue;

	private HashMap<String, ServerSession> inGameSessions;

	public ServerSessionManager(BaseServer server,
			BlockingQueue<ServerPacket> outputQueue) {
		this.server = server;
		this.outputQueue = outputQueue;
		this.sessions = new HashSet<ServerSession>();
		this.inGameSessions = new HashMap<String, ServerSession>();
	}

	public void addSession(ServerSession session) {
		server.getConfigManager().getSessionLogger()
				.log(Level.INFO, "Session created - " + session.getId());
		// Send connected packet
		outputQueue.add(new ConnectedPacket(session));
		sessions.add(session);
	}

	public void removeSession(ServerSession session) {
		// If the client was in game, remove from in game sessions as well
		if (session.getSessionType() == SessionType.GAME) {
			inGameSessions.remove(session.getCharacter().getName());
		}
		sessions.remove(session);
		server.getConfigManager().getSessionLogger()
				.log(Level.INFO, "Session removed - " + session.getId());
	}

	public ServerSession getInGameSession(String name) {
		return inGameSessions.get(name);
	}

	public synchronized boolean registerInGameSession(ServerSession session)
			throws IllegalStateException {
		if (session.getSessionType() != SessionType.GAME
				|| session.getAccount() == null
				|| session.getCharacter() == null) {
			this.server
					.getConfigManager()
					.getErrorLogger()
					.log(Level.SEVERE,
							"Attempt to register invalid in-game session "
									+ session.getId() + ".");
			throw new IllegalStateException(
					"Cannot register ingame session. Not a valid session.");
		}

		if (getInGameSession(session.getCharacter().getName()) != null) {
			return false;
		}

		inGameSessions.put(session.getCharacter().getName(), session);
		return true;
	}

	@Override
	public void run() {
		ServerPacket packet;
		byte[] rawBytes;

		// Repeatedly pop a packet from the output queue
		// dispatching it based on the destination type.
		try {
			while (true) {

				packet = outputQueue.take();
				rawBytes = packet.getRawBytes();
				System.out.println("-> " + packet.getPacketType() + "("
						+ rawBytes.length + ")");
				switch (packet.getDestinationType()) {
				case SINGLE_SESSION:
					((ServerSession) packet.getDestinationObject())
							.getOutputQueue().add(rawBytes);
					break;
				case GLOBAL:
					for (ServerSession session : sessions) {
						session.getOutputQueue().add(rawBytes);
					}
					break;
				case GLOBAL_EXCEPT_FOR:
					for (ServerSession session : sessions) {
						if (session != packet.getDestinationObject()) {
							session.getOutputQueue().add(rawBytes);
						}
					}
					break;

				}
			}
		} catch (InterruptedException e) {

		}

	}
}
