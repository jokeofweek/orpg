package orpg.server.net.handlers;

import orpg.server.BaseServer;
import orpg.server.data.ServerReceivedPacket;
import orpg.server.data.managers.MapManager;
import orpg.server.net.packets.ClientMapDataPacket;
import orpg.shared.data.Map;
import orpg.shared.data.Segment;

public class ClientLoadMapHandler implements ServerPacketHandler {

	@Override
	public void handle(ServerReceivedPacket packet, BaseServer baseServer) {
		Map map = baseServer.getMapManager().get(1);
		Segment segment = baseServer.getMapManager().getSegment(1, 0, 0);
		baseServer.sendPacket(new ClientMapDataPacket(packet.getSession(), map,
				segment));
	}

}
