package orpg.server;

import java.io.File;
import java.util.PriorityQueue;

import orpg.server.data.ServerReceivedPacket;
import orpg.server.data.ServerSentPacket;
import orpg.shared.Constants;
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
		case EDITOR_LOGIN:
			sender.setSessionType(SessionType.EDITOR);
			outputQueue.add(ServerSentPacket.getSessionPacket(
					ServerPacketType.EDITOR_LOGIN_OK, Priority.URGENT,
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
		switch (packet.getType()) {
		case EDITOR_READY:
			File mapDirectory = new File(Constants.SERVER_MAPS_PATH);
			File[] mapFiles = mapDirectory.listFiles();
			OutputByteBuffer out = new OutputByteBuffer(
					(mapFiles.length * 10) + 2);
			out.putUnsignedShort(mapFiles.length);
			for (File file : mapFiles) {
				out.putString(file.getName());
			}
			
			outputQueue.add(ServerSentPacket.getSessionPacket(
					ServerPacketType.EDITOR_MAP_LIST, Priority.MEDIUM,
					packet.getSession(), out.getBytes()));
			break;
		default:
			packet.getSession().disconnect("Invalid packet.");
		}
	}

}
