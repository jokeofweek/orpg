package orpg.server;

import java.util.PriorityQueue;

import orpg.server.data.ReceivedPacket;
import orpg.server.data.SentPacket;
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

	private PriorityQueue<ReceivedPacket> inputQueue;
	private PriorityQueue<SentPacket> outputQueue;

	public ServerGameThread(BaseServer server) {
		this.inputQueue = server.getInputQueue();
		this.outputQueue = server.getOutputQueue();
	}

	@Override
	public void run() {
		ReceivedPacket p = null;
		while (true) {
			if (!inputQueue.isEmpty()) {
				p = inputQueue.remove();
				System.out.println("Removed a packet!");
				outputQueue.add(SentPacket.getGlobalPacket(
						ServerPacketType.PONG, p.getBytes(),
						Priority.MEDIUM));
			}
		}
	}

}
