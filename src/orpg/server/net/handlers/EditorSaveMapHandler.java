package orpg.server.net.handlers;

import java.util.logging.Level;

import orpg.server.BaseServer;
import orpg.server.data.ServerReceivedPacket;
import orpg.server.data.store.DataStoreException;
import orpg.server.net.packets.ErrorPacket;
import orpg.shared.ErrorMessage;
import orpg.shared.data.Map;

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

		if (!baseServer.getMapController().save(map)) {
			baseServer.sendPacket(new ErrorPacket(packet.getSession(),
					ErrorMessage.GENERIC_MAP_SAVE_ERROR));
		}

	}
}
