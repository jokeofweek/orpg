package orpg.server.net.handlers;

import orpg.server.BaseServer;
import orpg.server.data.ServerReceivedPacket;
import orpg.server.data.controllers.MapController;
import orpg.server.net.packets.ClientSegmentDataPacket;
import orpg.shared.data.Map;
import orpg.shared.data.Segment;

public class ClientNeedSegmentHandler implements ServerPacketHandler {

	@Override
	public void handle(ServerReceivedPacket packet, BaseServer baseServer) {
		int mapId = packet.getByteBuffer().getInt();
		int segmentX = packet.getByteBuffer().getInt();
		int segmentY = packet.getByteBuffer().getInt();
		
		Map map = packet.getSession().getCharacter().getMap();

		// Drop the request if map id doesn't match
		if (map.getId() != mapId) {
			return;
		}
		
		// Load the segment
		Segment segment = baseServer.getMapController().getSegment(map.getId(),
				segmentX, segmentY);

		// Send the segment to the player.
		baseServer.sendPacket(new ClientSegmentDataPacket(packet.getSession(), map.getId(),
				segment));

		// If the player was changing maps before, then we update.
		if (packet.getSession().getCharacter().isChangingMap()) {
			packet.getSession().getCharacter().setChangingMap(false);
		}
	}
}
