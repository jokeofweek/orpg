package orpg.server.net.handlers;

import java.util.logging.Level;

import orpg.server.BaseServer;
import orpg.server.data.ServerReceivedPacket;
import orpg.server.data.store.DataStoreException;
import orpg.server.net.packets.ErrorPacket;
import orpg.shared.data.Map;
import orpg.shared.data.Segment;

public class EditorSaveMapHandler implements ServerPacketHandler {

	@Override
	public void handle(ServerReceivedPacket packet, BaseServer baseServer) {
		packet.getByteBuffer().decompress();
		Map map = packet.getByteBuffer().getMapDescriptor();

		// Fetch the old segments from the map manager
		Segment[][] oldSegments = baseServer.getMapManager()
				.get(map.getId()).getSegments();

		// Update all segments
		for (int x = 0; x < oldSegments.length; x++) {
			for (int y = 0; y < oldSegments[0].length; y++) {
				map.updateSegment(oldSegments[x][y]);
			}
		}

		// Fetch the new segments and apply them
		short updatedSegments = packet.getByteBuffer().getShort();
		for (int i = 0; i < updatedSegments; i++) {
			map.updateSegment(packet.getByteBuffer().getSegment());
		}

		try {
			baseServer.getDataStore().saveMap(map);
			baseServer.getMapManager().update(map);
		} catch (DataStoreException e) {
			baseServer
					.getConfigManager()
					.getErrorLogger()
					.log(Level.SEVERE,
							"Session " + packet.getSession()
									+ " could not save map " + map.getId()
									+ " in editor. Reason: " + e.getMessage());
			baseServer
					.sendPacket(new ErrorPacket(packet.getSession(),
							"An error occured while saving the map. Please try again later."));
		}
	}
}
