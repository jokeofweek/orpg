package orpg.server.net.packets;

import orpg.server.ServerSession;
import orpg.shared.net.ServerPacketType;

public class EditorLoginOkPacket extends SessionPacket {

	public EditorLoginOkPacket(ServerSession session) {
		super(session);

	}

	@Override
	public ServerPacketType getPacketType() {
		return ServerPacketType.EDITOR_LOGIN_OK;
	}

	@Override
	public byte[] getBytes() {
		return new byte[0];
	}

}
