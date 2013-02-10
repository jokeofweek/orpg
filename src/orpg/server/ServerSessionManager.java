package orpg.server;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.logging.Level;

import orpg.server.data.ServerSentPacket;
import orpg.shared.Priority;
import orpg.shared.net.OutputByteBuffer;
import orpg.shared.net.ServerPacketType;

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
	private PriorityQueue<ServerSentPacket> outputQueue;

	public ServerSessionManager(BaseServer server) {
		this.server = server;
		this.sessions = new HashSet<ServerSession>();
		this.outputQueue = server.getOutputQueue();
	}

	public void addSession(ServerSession session) {
		server.getConfigurationManager().getSessionLogger()
				.log(Level.INFO, "Session created - " + session.getId());
		// Send connected packet
		outputQueue.add(ServerSentPacket.getSessionPacket(
				ServerPacketType.CONNECTED, Priority.URGENT, session));
		sessions.add(session);
	}

	public void removeSession(ServerSession session) {
		sessions.remove(session);
		server.getConfigurationManager().getSessionLogger()
				.log(Level.INFO, "Session removed - " + session.getId());
	}

	@Override
	public void run() {
		ServerSentPacket packet;
		// Repeatedly pop a packet from the output queue
		// dispatching it based on the destination type.
		while (true) {
			if (!outputQueue.isEmpty()) {
				packet = outputQueue.remove();
				switch (packet.getDestinationType()) {
				case SINGLE_SESSION:
					((ServerSession) packet.getDestinationObject())
							.getOutputQueue().add(packet);
					break;
				case GLOBAL:
					for (ServerSession session : sessions) {
						session.getOutputQueue().add(packet);
					}
					break;
				case GLOBAL_EXCEPT_FOR:
					for (ServerSession session : sessions) {
						if (session != packet.getDestinationObject()) {
							session.getOutputQueue().add(packet);
						}
					}
					break;
				}
			} else {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}

}
