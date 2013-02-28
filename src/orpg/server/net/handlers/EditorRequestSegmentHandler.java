package orpg.server.net.handlers;

import orpg.server.BaseServer;
import orpg.server.data.ServerReceivedPacket;
import orpg.server.net.packets.EditorMapSegmentDataPacket;

public class EditorRequestSegmentHandler implements ServerPacketHandler {

	@Override
	public void handle(ServerReceivedPacket packet, BaseServer baseServer) {
		int id = packet.getByteBuffer().getInt();
		int x = packet.getByteBuffer().getInt();
		int y = packet.getByteBuffer().getInt();

		baseServer.sendPacket(new EditorMapSegmentDataPacket(packet
				.getSession(), id, baseServer.getMapController().getSegment(id, x,
				y)));
	}

}
