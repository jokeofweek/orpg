package orpg.server.net.handlers;

import orpg.server.BaseServer;
import orpg.server.data.ServerReceivedPacket;
import orpg.server.event.NeedSegmentMovementEvent;
import orpg.server.net.packets.ClientSegmentDataPacket;
import orpg.server.systems.MovementSystem;
import orpg.shared.data.Map;
import orpg.shared.data.Segment;

public class ClientNeedSegmentHandler implements ServerPacketHandler {

	@Override
	public void handle(ServerReceivedPacket packet, BaseServer baseServer) {
		int mapId = packet.getByteBuffer().getInt();
		short segmentX = packet.getByteBuffer().getShort();
		short segmentY = packet.getByteBuffer().getShort();
		int revision = packet.getByteBuffer().getInt();
		long revisionTime = packet.getByteBuffer().getLong();

		baseServer
				.getWorld()
				.getSystem(MovementSystem.class)
				.addEvent(
						new NeedSegmentMovementEvent(packet.getSession(),
								mapId, segmentX, segmentY, revision,
								revisionTime));
	}
}
