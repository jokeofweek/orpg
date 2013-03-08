package orpg.server.net.packets;

import com.artemis.Entity;

import orpg.server.ServerSession;
import orpg.shared.data.Direction;
import orpg.shared.data.component.Position;
import orpg.shared.net.ServerPacketType;
import orpg.shared.net.serialize.OutputByteBuffer;

public class ClientMovePacket extends MapExceptForPacket {
	private byte[] bytes;

	public ClientMovePacket(ServerSession session, int mapId, Entity entity,
			Direction direction, boolean ignoreSession) {
		super(ignoreSession ? session : null, mapId);
		OutputByteBuffer out = new OutputByteBuffer(5);
		out.putInt(entity.getId());
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
