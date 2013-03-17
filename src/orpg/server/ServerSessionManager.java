package orpg.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.managers.PlayerManager;
import com.artemis.utils.ImmutableBag;

import orpg.server.data.SessionType;
import orpg.server.net.DestinationMatcher;
import orpg.server.net.packets.ConnectedPacket;
import orpg.server.net.packets.ServerPacket;
import orpg.shared.Constants;
import orpg.shared.data.Map;
import orpg.shared.data.Pair;
import orpg.shared.data.Segment;
import orpg.shared.data.component.Named;
import orpg.shared.data.component.Position;

/**
 * The server sessions manager is responsible for keeping track of the current
 * active sessions. It also takes care of dispatching all packets in the
 * outputQueue, routing them based on their destination types.
 * 
 * @author Dominic Charley-Roy
 */
public class ServerSessionManager implements Runnable {

	private ComponentMapper<Named> namedMapper;

	private HashSet<ServerSession> sessions;
	private BaseServer baseServer;
	private BlockingQueue<ServerPacket> outputQueue;
	private HashMap<String, List<ServerSession>> accountSessions;
	private HashMap<String, ServerSession> inGameSessions;
	private HashMap<Entity, ServerSession> entitySessions;

	public ServerSessionManager(BaseServer server,
			BlockingQueue<ServerPacket> outputQueue) {
		this.baseServer = server;
		this.outputQueue = outputQueue;
		this.sessions = new HashSet<ServerSession>();
		this.inGameSessions = new HashMap<String, ServerSession>();
		this.accountSessions = new HashMap<String, List<ServerSession>>();
		this.entitySessions = new HashMap<Entity, ServerSession>();

		this.namedMapper = baseServer.getWorld().getMapper(Named.class);
	}

	public void addSession(ServerSession session) {
		baseServer.getConfigManager().getSessionLogger()
				.log(Level.INFO, "Session created - " + session.getId());
		// Send connected packet
		outputQueue.add(new ConnectedPacket(session));
		sessions.add(session);
	}

	public void removeSession(ServerSession session) {
		// If the client was in game, remove from in game sessions as well
		if (session.getSessionType() == SessionType.GAME) {
			inGameSessions.remove(session.getCharacterName());
		}

		// If the session had an account, remove from the accountSessions
		// and then check if we can release the account
		if (session.getAccount() != null) {

			String name = session.getAccount().getName();
			accountSessions.get(name).remove(session);
			if (accountSessions.get(name).size() == 0) {
				baseServer.getAccountController().release(name);
			}
		}

		sessions.remove(session);
		baseServer.getConfigManager().getSessionLogger()
				.log(Level.INFO, "Session removed - " + session.getId());

		session.setSessionType(SessionType.LOGGED_OUT);
	}

	public ServerSession getInGameSession(String name) {
		return inGameSessions.get(name);
	}

	public ServerSession getEntitySession(Entity entity) {
		synchronized (entitySessions) {
			return entitySessions.get(entity);
		}
	}

	public synchronized void registerAccountSession(ServerSession session) {
		if (session.getAccount() == null) {
			this.baseServer
					.getConfigManager()
					.getErrorLogger()
					.log(Level.SEVERE,
							"Attempt to register invalid account session "
									+ session.getId() + ".");
			throw new IllegalStateException(
					"Cannot register account session. Not a valid session.");
		}

		// Either append to the list of active account sessions if
		// there is at least one other session with that account, else
		// create a new list.
		if (accountSessions.containsKey(session.getAccount().getName())) {
			accountSessions.get(session.getAccount().getName()).add(session);
		} else {
			List<ServerSession> sessions = new ArrayList<ServerSession>(1);
			sessions.add(session);
			accountSessions.put(session.getAccount().getName(), sessions);
		}
	}

	public synchronized boolean registerInGameSession(ServerSession session)
			throws IllegalStateException {
		if (session.getSessionType() != SessionType.GAME
				|| session.getAccount() == null
				|| session.getCharacterName() == null) {
			this.baseServer
					.getConfigManager()
					.getErrorLogger()
					.log(Level.SEVERE,
							"Attempt to register invalid in-game session "
									+ session.getId() + ".");
			throw new IllegalStateException(
					"Cannot register ingame session. Not a valid session.");
		}

		if (getInGameSession(session.getCharacterName()) != null) {
			return false;
		}

		inGameSessions.put(session.getCharacterName(), session);
		return true;
	}

	public void registerSessionEntity(ServerSession session) {
		synchronized (entitySessions) {
			entitySessions.put(session.getEntity(), session);
		}
	}

	public void removeSessionEntity(Entity entity) {
		synchronized (entitySessions) {
			entitySessions.remove(entity);
		}
	}

	@SuppressWarnings("unchecked")
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
				case MAP:
					Integer id = (Integer) packet.getDestinationObject();

					ImmutableBag<Entity> entities = baseServer
							.getWorld()
							.getManager(GroupManager.class)
							.getEntities(
									String.format(Constants.GROUP_MAP_PLAYERS,
											id));

					for (int i = 0; i < entities.size(); i++) {
						getEntitySession(entities.get(i)).getOutputQueue().add(
								rawBytes);
					}

					break;
				case MAP_EXCEPT_FOR:
					Pair<ServerSession, Integer> destinationObject = (Pair<ServerSession, Integer>) packet
							.getDestinationObject();
					ServerSession session;
					ServerSession toExclude = destinationObject.getFirst();

					entities = baseServer
							.getWorld()
							.getManager(GroupManager.class)
							.getEntities(
									String.format(Constants.GROUP_MAP_PLAYERS,
											destinationObject.getSecond()));

					for (int i = 0; i < entities.size(); i++) {
						session = getEntitySession(entities.get(i));
						if (session != toExclude) {
							session.getOutputQueue().add(rawBytes);
						}
					}
					break;
				case GLOBAL_MATCHING:
					Collection<ServerSession> recipients = ((DestinationMatcher) packet
							.getDestinationObject())
							.getReceivingSessions(baseServer);
					for (ServerSession recipient : recipients) {
						recipient.getOutputQueue().add(rawBytes);
					}
					break;
				}
			}
		} catch (InterruptedException e) {

		}

	}
}
