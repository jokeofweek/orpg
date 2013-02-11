package orpg.server.net.handlers;

import java.io.IOException;
import java.util.logging.Level;

import orpg.server.BaseServer;
import orpg.server.data.FileSystem;
import orpg.server.data.ServerReceivedPacket;
import orpg.server.net.packets.EditorMapDataPacket;
import orpg.server.net.packets.ErrorPacket;
import orpg.shared.Priority;
import orpg.shared.data.Map;
import orpg.shared.net.OutputByteBuffer;
import orpg.shared.net.ServerPacketType;

public class EditorEditMapHandler implements ServerPacketHandler {

	@Override
	public void handle(ServerReceivedPacket packet, BaseServer baseServer) {

		int number = packet.getByteBuffer().getInt();

		try {
			Map map = FileSystem.loadMap(number);
			baseServer.getOutputQueue().add(
					new EditorMapDataPacket(packet.getSession(), map));
		} catch (IllegalArgumentException e) {
			baseServer
					.getConfigManager()
					.getSessionLogger()
					.log(Level.WARNING,
							"Session " + packet.getSession()
									+ " tried to edit invalid map number "
									+ number + ".");
			baseServer.getOutputQueue()
					.add(new ErrorPacket(packet.getSession(),
							"Invalid map number."));
		} catch (IOException e) {
			baseServer
					.getConfigManager()
					.getSessionLogger()
					.log(Level.SEVERE,
							"Session " + packet.getSession()
									+ " could not load map " + number
									+ " for editing. Reason: " + e.getMessage());
			baseServer
					.getOutputQueue()
					.add(new ErrorPacket(packet.getSession(),
							"An error occured while fetching the map information. Please try again later."));
		}
	}

}
