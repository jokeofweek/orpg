package orpg.server.net.handlers;

import orpg.server.BaseServer;
import orpg.server.SessionType;
import orpg.server.data.ServerReceivedPacket;
import orpg.server.data.ServerSentPacket;
import orpg.shared.Priority;
import orpg.shared.net.ServerPacketType;

public class EditorLoginHandler implements ServerPacketHandler {

	@Override
	public void handle(ServerReceivedPacket packet, BaseServer baseServer) {
		packet.getSession().setSessionType(SessionType.EDITOR);
		baseServer.getOutputQueue().add(
				ServerSentPacket.getSessionPacket(
						ServerPacketType.EDITOR_LOGIN_OK, Priority.URGENT,
						packet.getSession()));
	}

}
