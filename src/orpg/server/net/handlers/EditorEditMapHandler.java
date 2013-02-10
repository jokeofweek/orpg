package orpg.server.net.handlers;

import java.io.IOException;
import java.util.logging.Level;

import orpg.server.BaseServer;
import orpg.server.data.FileSystem;
import orpg.server.data.ServerReceivedPacket;
import orpg.server.data.ServerSentPacket;
import orpg.shared.Priority;
import orpg.shared.data.Map;
import orpg.shared.net.OutputByteBuffer;
import orpg.shared.net.ServerPacketType;

public class EditorEditMapHandler implements ServerPacketHandler {

	@Override
	public void handle(ServerReceivedPacket packet, BaseServer baseServer) {
		OutputByteBuffer out = new OutputByteBuffer();
		int number = packet.getByteBuffer().getInt();

		try {
			Map map = FileSystem.loadMap(number);
			baseServer.sendEditorMapData(packet.getSession(), map);
		} catch (IllegalArgumentException e) {
			out.putString("Invalid map number.");
			baseServer
					.getConfigManager()
					.getSessionLogger()
					.log(Level.WARNING,
							"Session " + packet.getSession()
									+ " tried to edit invalid map number "
									+ number + ".");
			baseServer.getOutputQueue().add(
					ServerSentPacket.getSessionPacket(ServerPacketType.ERROR,
							Priority.URGENT, packet.getSession(),
							out.getBytes()));
		} catch (IOException e) {
			out.putString("An error occured while fetching the map information. Please try again later.");
			baseServer
					.getConfigManager()
					.getSessionLogger()
					.log(Level.SEVERE,
							"Session " + packet.getSession()
									+ " could not load map " + number
									+ " for editing. Reason: " + e.getMessage());
			baseServer.getOutputQueue().add(
					ServerSentPacket.getSessionPacket(ServerPacketType.ERROR,
							Priority.URGENT, packet.getSession(),
							out.getBytes()));
		}
	}

}
