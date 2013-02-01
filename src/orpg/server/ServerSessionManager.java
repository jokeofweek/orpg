package orpg.server;

import java.util.HashSet;
import java.util.PriorityQueue;

import orpg.server.data.SentPacket;
import orpg.shared.Priority;
import orpg.shared.ServerPacketType;

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
	private PriorityQueue<SentPacket> outputQueue;

	public ServerSessionManager(BaseServer server) {
		this.server = server;
		this.sessions = new HashSet<ServerSession>();
		this.outputQueue = server.getOutputQueue();
	}

	public void addSession(ServerSession session) {
		outputQueue.add(SentPacket.getGlobalPacket(ServerPacketType.HELLO,
				new byte[] {}, Priority.MEDIUM));
		sessions.add(session);
	}

	public void removeSession(ServerSession session) {
		sessions.remove(session);
		outputQueue.add(SentPacket.getGlobalPacket(ServerPacketType.GOODBYE,
				new byte[] {}, Priority.MEDIUM));
	}

	@Override
	public void run() {
		SentPacket packet;
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
				}
			}
		}

	}

}
