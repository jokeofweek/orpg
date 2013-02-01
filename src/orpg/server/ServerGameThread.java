package orpg.server;

import java.util.PriorityQueue;

import orpg.server.data.ServerReceivedPacket;
import orpg.server.data.ServerSentPacket;
import orpg.shared.ByteStream;
import orpg.shared.Priority;
import orpg.shared.ServerPacketType;

/**
 * This thread is responsible for handling all packets in the input queue as
 * well as potentially adding packets to the output queue.
 * 
 * @author Dominic Charley-Roy
 * 
 */
public class ServerGameThread implements Runnable {

	private PriorityQueue<ServerReceivedPacket> inputQueue;
	private PriorityQueue<ServerSentPacket> outputQueue;

	public ServerGameThread(BaseServer server) {
		this.inputQueue = server.getInputQueue();
		this.outputQueue = server.getOutputQueue();
	}

	@Override
	public void run() {
		ServerReceivedPacket p = null;
		while (true) {
			if (!inputQueue.isEmpty()) {
				p = inputQueue.remove();
				
				Object[] o = ByteStream.unserialize(p.getBytes(), String.class);
				outputQueue.add(ServerSentPacket.getSessionPacket(
						ServerPacketType.PONG, Priority.MEDIUM,
						p.getSession(), o[0]));
			}
		}
	}

}
