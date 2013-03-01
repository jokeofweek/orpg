package orpg.server.net.handlers;

import java.util.HashMap;
import java.util.logging.Level;

import orpg.client.net.handlers.SegmentDataHandler;
import orpg.server.BaseServer;
import orpg.server.ServerSessionManager;
import orpg.server.data.ServerReceivedPacket;
import orpg.server.data.controllers.MapController;
import orpg.server.data.store.DataStoreException;
import orpg.server.net.packets.ErrorPacket;
import orpg.shared.ErrorMessage;
import orpg.shared.data.AccountCharacter;
import orpg.shared.data.Map;
import orpg.shared.data.Segment;

public class EditorSaveMapHandler implements ServerPacketHandler {

	@Override
	public void handle(ServerReceivedPacket packet, BaseServer baseServer) {
		packet.getByteBuffer().decompress();
		Map descriptor = packet.getByteBuffer().getMapDescriptor();
		Map map = baseServer.getMapController().get(descriptor.getId());

		// Fetch the new segments and apply them
		short updatedSegments = packet.getByteBuffer().getShort();
		for (int i = 0; i < updatedSegments; i++) {
			map.updateSegment(packet.getByteBuffer().getSegment(false));
		}

		// Re-warp all players on map
		MapController mapController = baseServer.getMapController();
		ServerSessionManager sessionManager = baseServer.getServerSessionManager();
		int id = map.getId();
		for (Segment[] segmentCols : map.getSegments()) {
			for (Segment segment : segmentCols) {
				if (segment != null) {
					for (AccountCharacter character : segment.getPlayers().values()) {
						mapController.refreshMap(sessionManager
								.getInGameSession(character.getName()));
					}

				}
			}
		}

		if (!baseServer.getMapController().save(map)) {
			baseServer.sendPacket(new ErrorPacket(packet.getSession(),
					ErrorMessage.GENERIC_MAP_SAVE_ERROR));
		}

	}
}
