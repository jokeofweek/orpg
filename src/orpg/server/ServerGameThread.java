package orpg.server;

import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.logging.Level;

import orpg.server.data.FileSystem;
import orpg.server.data.ServerReceivedPacket;
import orpg.server.data.ServerSentPacket;
import orpg.shared.Priority;
import orpg.shared.data.Map;
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

	private Queue<ServerReceivedPacket> inputQueue;
	private PriorityQueue<ServerSentPacket> outputQueue;
	private BaseServer baseServer;

	public ServerGameThread(BaseServer baseServer) {
		this.baseServer = baseServer;
		this.inputQueue = baseServer.getInputQueue();
		this.outputQueue = baseServer.getOutputQueue();
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
					ServerPacketType.EDITOR_LOGIN_OK, Priority.URGENT, sender));
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
		OutputByteBuffer out = new OutputByteBuffer();

		switch (packet.getType()) {
		case EDITOR_READY:
			out.putInt(baseServer.getConfigManager().getTotalMaps());
			for (int i = 0; i < baseServer.getConfigManager().getTotalMaps(); i++) {
				// put map name here.
			}
			outputQueue.add(ServerSentPacket.getSessionPacket(
					ServerPacketType.EDITOR_MAP_LIST, Priority.MEDIUM,
					packet.getSession(), out.getBytes()));
			break;
		case EDITOR_EDIT_MAP:
			handleEditMap(packet);
			break;
		case EDITOR_MAP_SAVE:
			// Save maps
			Map map = packet.getByteBuffer().getMap();
			try {
				FileSystem.save(map);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			packet.getSession().disconnect("Invalid packet.");
		}
	}

	public void handleEditMap(ServerReceivedPacket packet) {
		OutputByteBuffer out = new OutputByteBuffer();
		int number = packet.getByteBuffer().getInt();

		try {
			Map map = FileSystem.loadMap(number);
			sendEditorMapData(packet.getSession(), map);
		} catch (IllegalArgumentException e) {
			out.putString("Invalid map number.");
			baseServer
					.getConfigManager()
					.getSessionLogger()
					.log(Level.WARNING,
							"Session " + packet.getSession()
									+ " tried to edit invalid map number "
									+ number + ".");
			outputQueue.add(ServerSentPacket.getSessionPacket(
					ServerPacketType.ERROR, Priority.URGENT,
					packet.getSession(), out.getBytes()));
		} catch (IOException e) {
			out.putString("An error occured while fetching the map information. Please try again later.");
			baseServer
					.getConfigManager()
					.getSessionLogger()
					.log(Level.SEVERE,
							"Session " + packet.getSession()
									+ " could not load map " + number
									+ " for editing. Reason: " + e.getMessage());
			outputQueue.add(ServerSentPacket.getSessionPacket(
					ServerPacketType.ERROR, Priority.URGENT,
					packet.getSession(), out.getBytes()));
		}
	}

	public void sendEditorMapData(ServerSession session, Map map) {
		OutputByteBuffer out = new OutputByteBuffer();
		out.putMap(map);
		outputQueue.add(ServerSentPacket.getSessionPacket(
				ServerPacketType.EDITOR_MAP_DATA, Priority.MEDIUM, session,
				out.getBytes()));
	}
}
