package orpg.server.net.handlers;

import orpg.server.BaseServer;
import orpg.server.ServerSessionManager;
import orpg.server.data.ServerReceivedPacket;
import orpg.server.data.controllers.MapController;
import orpg.server.event.RefreshMapPlayersEvent;
import orpg.server.net.packets.ErrorPacket;
import orpg.server.systems.MovementSystem;
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
			map.updateSegment(packet.getByteBuffer().getSegment(), true);
		}

		// Re-warp all players on map
		baseServer.getWorld().getSystem(MovementSystem.class)
				.addEvent(new RefreshMapPlayersEvent(descriptor.getId()));

		if (!baseServer.getMapController().save(map)) {
			baseServer.sendPacket(new ErrorPacket(packet.getSession(),
					ErrorMessage.GENERIC_MAP_SAVE_ERROR));
		}

	}
}
