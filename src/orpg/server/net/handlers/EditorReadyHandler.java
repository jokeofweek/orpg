package orpg.server.net.handlers;

import orpg.server.BaseServer;
import orpg.server.data.ServerReceivedPacket;
import orpg.server.data.ServerSentPacket;
import orpg.server.data.managers.MapManager;
import orpg.shared.Priority;
import orpg.shared.net.OutputByteBuffer;
import orpg.shared.net.ServerPacketType;

public class EditorReadyHandler implements ServerPacketHandler {

	@Override
	public void handle(ServerReceivedPacket packet, BaseServer baseServer) {
		OutputByteBuffer out = new OutputByteBuffer();
		out.putInt(baseServer.getConfigManager().getTotalMaps());
		
		for (int i = 0; i < baseServer.getConfigManager().getTotalMaps(); i++) {
			// put map name here.
		}
		baseServer.getOutputQueue().add(
				ServerSentPacket.getSessionPacket(
						ServerPacketType.EDITOR_MAP_LIST, Priority.MEDIUM,
						packet.getSession(), out.getBytes()));

	}

}
