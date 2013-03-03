package orpg.server.net.handlers;

import java.util.logging.Level;

import orpg.server.BaseServer;
import orpg.server.data.ServerReceivedPacket;
import orpg.server.net.packets.EditorMapDataPacket;
import orpg.server.net.packets.ErrorPacket;
import orpg.shared.data.Map;
import orpg.shared.data.Segment;

public class EditorEditMapHandler implements ServerPacketHandler {

	@Override
	public void handle(ServerReceivedPacket packet, BaseServer baseServer) {

		int number = packet.getByteBuffer().getInt();

		try {
			Map map = baseServer.getMapController().get(number);
			Segment segment = baseServer.getMapController().getSegment(
					map.getId(), (short)0, (short)0);
			baseServer.sendPacket(new EditorMapDataPacket(packet.getSession(),
					map, segment));
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
		}
	}

}
