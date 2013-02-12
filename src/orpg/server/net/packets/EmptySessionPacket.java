package orpg.server.net.packets;

import orpg.server.ServerSession;
import orpg.shared.net.ServerPacketType;

public class EmptySessionPacket extends SessionPacket {

	private ServerPacketType type;
	
	public EmptySessionPacket(ServerSession session, ServerPacketType type) {
		super(session);
		this.type = type;
	}

	@Override
	public ServerPacketType getPacketType() {
		return this.type;
	}

	@Override
	public byte[] getBytes() {
		// TODO Auto-generated method stub
		return new byte[0];
	}

}
