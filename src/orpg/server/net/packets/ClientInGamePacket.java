package orpg.server.net.packets;

import orpg.server.ServerSession;
import orpg.shared.data.AccountCharacter;
import orpg.shared.net.OutputByteBuffer;
import orpg.shared.net.ServerPacketType;

public class ClientInGamePacket extends SessionPacket {

	private byte[] bytes;

	public ClientInGamePacket(ServerSession session, AccountCharacter character) {
		super(session);
		OutputByteBuffer out = new OutputByteBuffer();
		out.putAccountCharacter(character);
		this.bytes = out.getBytes();
	}

	@Override
	public ServerPacketType getPacketType() {
		return ServerPacketType.CLIENT_IN_GAME;
	}

	@Override
	public byte[] getBytes() {
		return bytes;
	}

}
