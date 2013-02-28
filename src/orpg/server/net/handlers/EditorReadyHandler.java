package orpg.server.net.handlers;

import orpg.server.BaseServer;
import orpg.server.data.ServerReceivedPacket;
import orpg.server.net.packets.EditorMapListPacket;
import orpg.shared.net.OutputByteBuffer;

public class EditorReadyHandler implements ServerPacketHandler {

	@Override
	public void handle(ServerReceivedPacket packet, BaseServer baseServer) {
		OutputByteBuffer out = new OutputByteBuffer();
		out.putInt(baseServer.getConfigManager().getTotalMaps());

		baseServer.sendPacket(new EditorMapListPacket(packet.getSession(),
				baseServer.getMapController().getNameList()));

	}
}
