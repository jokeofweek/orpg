package orpg.server.net.handlers;

import orpg.server.BaseServer;
import orpg.server.SessionType;
import orpg.server.data.ServerReceivedPacket;
import orpg.server.net.packets.EditorLoginOkPacket;
import orpg.shared.Priority;
import orpg.shared.net.ServerPacketType;

public class LoginHandler implements ServerPacketHandler {

	private boolean isEditorHandler;

	public LoginHandler(boolean isEditorHandler) {
		this.isEditorHandler = isEditorHandler;
	}

	@Override
	public void handle(ServerReceivedPacket packet, BaseServer baseServer) {
		if (isEditorHandler) {
			packet.getSession().setSessionType(SessionType.EDITOR);
			baseServer.getOutputQueue().add(
					new EditorLoginOkPacket(packet.getSession()));
		}
	}

}
