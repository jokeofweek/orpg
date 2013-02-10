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

public class EditorSaveMapHandler implements ServerPacketHandler {

	@Override
	public void handle(ServerReceivedPacket packet, BaseServer baseServer) {
		Map map = packet.getByteBuffer().getMap();
		try {
			FileSystem.save(map);
		} catch (IOException e) {
			baseServer
					.getConfigManager()
					.getErrorLogger()
					.log(Level.SEVERE,
							"Session " + packet.getSession()
									+ " could not save map " + map.getId()
									+ " in editor. Reason: " + e.getMessage());
			OutputByteBuffer out = new OutputByteBuffer();
			out.putString("An error occured while saving the map. Please try again later.");
			baseServer.getOutputQueue().add(
					ServerSentPacket.getSessionPacket(ServerPacketType.ERROR,
							Priority.URGENT, packet.getSession(),
							out.getBytes()));
		}
	}
}
