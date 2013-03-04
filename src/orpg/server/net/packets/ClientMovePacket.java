package orpg.server.net.packets;

import orpg.server.ServerSession;
import orpg.shared.data.AccountCharacter;
import orpg.shared.data.Direction;
import orpg.shared.net.ServerPacketType;
import orpg.shared.net.serialize.OutputByteBuffer;

public class ClientMovePacket extends MapExceptForPacket {

	private byte[] bytes;

	public ClientMovePacket(ServerSession serverSession,
			AccountCharacter character, Direction direction) {
		super(serverSession, character.getMap());

		OutputByteBuffer out = new OutputByteBuffer();
		out.putString(character.getName());
		out.putByte((byte) direction.ordinal());
		this.bytes = out.getBytes();
	}

	@Override
	public ServerPacketType getPacketType() {
		return ServerPacketType.CLIENT_MOVE;
	}

	@Override
	public byte[] getBytes() {
		return this.bytes;
	}

}
