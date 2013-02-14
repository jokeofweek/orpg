package orpg.server.net.packets;

import orpg.server.ServerSession;
import orpg.shared.Priority;
import orpg.shared.net.ServerPacketType;

public class LoginOkPacket extends SessionPacket {

	public LoginOkPacket(ServerSession session) {
		super(session);

	}

	@Override
	public Priority getPriority() {
		return Priority.URGENT;
	}

	@Override
	public ServerPacketType getPacketType() {
		return ServerPacketType.LOGIN_OK;
	}

	@Override
	public byte[] getBytes() {
		return new byte[0];
	}

}
