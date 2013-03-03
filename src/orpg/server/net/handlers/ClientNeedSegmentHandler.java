package orpg.server.net.handlers;

import orpg.server.BaseServer;
import orpg.server.data.ServerReceivedPacket;
import orpg.server.net.packets.ClientSegmentDataPacket;
import orpg.shared.data.Map;
import orpg.shared.data.Segment;

public class ClientNeedSegmentHandler implements ServerPacketHandler {

	@Override
	public void handle(ServerReceivedPacket packet, BaseServer baseServer) {
		int mapId = packet.getByteBuffer().getInt();
		short segmentX = packet.getByteBuffer().getShort();
		short segmentY = packet.getByteBuffer().getShort();

		Map map = packet.getSession().getCharacter().getMap();

		// Drop the request if map id doesn't match
		if (map.getId() != mapId) {
			return;
		}

		// Load the segment
		Segment segment = baseServer.getMapController().getSegment(map.getId(),
				segmentX, segmentY);

		// Match the revisions to see if we need to send
		int revision = packet.getByteBuffer().getInt();
		long revisionTime = packet.getByteBuffer().getLong();
		boolean revisionsMatch = segment.revisionMatches(revision, revisionTime);

		// Send the segment to the player.
		baseServer.sendPacket(new ClientSegmentDataPacket(packet.getSession(),
				map.getId(), segment, revisionsMatch));

		// If the player was changing maps before, then we update.
		if (packet.getSession().getCharacter().isChangingMap()) {
			packet.getSession().getCharacter().setChangingMap(false);
		}
	}
}
