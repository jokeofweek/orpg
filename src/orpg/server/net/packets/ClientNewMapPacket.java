package orpg.server.net.packets;

import orpg.server.ServerSession;
import orpg.shared.net.OutputByteBuffer;
import orpg.shared.net.ServerPacketType;

public class ClientNewMapPacket extends SessionPacket {

	private byte[] bytes;

	public ClientNewMapPacket(ServerSession session) {
		super(session);

		OutputByteBuffer out = new OutputByteBuffer();
		out.putMapDescriptor(session.getCharacter().getMap());

		// Need to put in client x and y to determine the segment we need
		out.putInt(session.getCharacter().getX());
		out.putInt(session.getCharacter().getY());

		this.bytes = out.getBytes();
	}

	@Override
	public ServerPacketType getPacketType() {
		return ServerPacketType.CLIENT_NEW_MAP;
	}

	@Override
	public byte[] getBytes() {
		return this.bytes;
	}

}
