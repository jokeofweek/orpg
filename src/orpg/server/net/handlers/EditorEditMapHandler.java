package orpg.server.net.handlers;

import java.util.logging.Level;

import orpg.server.BaseServer;
import orpg.server.data.ServerReceivedPacket;
import orpg.server.data.store.DataStoreException;
import orpg.server.net.packets.EditorMapDataPacket;
import orpg.server.net.packets.ErrorPacket;
import orpg.shared.data.Map;

public class EditorEditMapHandler implements ServerPacketHandler {

	@Override
	public void handle(ServerReceivedPacket packet, BaseServer baseServer) {

		int number = packet.getByteBuffer().getInt();

		try {
			Map map = baseServer.getDataStore().loadMap(number);
			baseServer.sendPacket(new EditorMapDataPacket(packet
					.getSession(), map));
		} catch (IllegalArgumentException e) {
			baseServer
					.getConfigManager()
					.getSessionLogger()
					.log(Level.WARNING,
							"Session " + packet.getSession()
									+ " tried to edit invalid map number "
									+ number + ".");
			baseServer.sendPacket(new ErrorPacket(packet.getSession(),
					"Invalid map number."));
		} catch (DataStoreException e) {
			baseServer
					.getConfigManager()
					.getSessionLogger()
					.log(Level.SEVERE,
							"Session " + packet.getSession()
									+ " could not load map " + number
									+ " for editing. Reason: "
									+ e.getMessage());
			baseServer
					.sendPacket(new ErrorPacket(
							packet.getSession(),
							"An error occured while fetching the map information. Please try again later."));
		}
	}

}
