package orpg.server;

import java.util.PriorityQueue;

import orpg.server.data.ServerReceivedPacket;
import orpg.server.data.ServerSentPacket;
import orpg.shared.Priority;
import orpg.shared.net.InputByteBuffer;
import orpg.shared.net.OutputByteBuffer;
import orpg.shared.net.ServerPacketType;

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
				if (p.getSession().getSessionType() == SessionType.GAME) {
					handleGamePacket(p);
				} else if (p.getSession().getSessionType() == SessionType.EDITOR) {
					handleEditorPacket(p);
				}
			}
		}
	}

	public void handleGamePacket(ServerReceivedPacket packet) {
		ServerSession sender = packet.getSession();

		switch (packet.getType()) {
		case LOGIN_EDITOR:
			sender.setSessionType(SessionType.EDITOR);
			outputQueue.add(ServerSentPacket.getSessionPacket(
					ServerPacketType.LOGIN_EDITOR_OK, Priority.URGENT,
					sender));
			break;
		default:
			InputByteBuffer buffer = packet.getByteBuffer();
			OutputByteBuffer outBuffer = new OutputByteBuffer();
			outBuffer.putString("Repeat " + buffer.getString());

			outputQueue.add(ServerSentPacket.getGlobalPacket(
					ServerPacketType.PONG, Priority.MEDIUM,
					outBuffer.getBytes()));
		}
	}

	public void handleEditorPacket(ServerReceivedPacket packet) {
		System.out.println(":D");
	}

}
