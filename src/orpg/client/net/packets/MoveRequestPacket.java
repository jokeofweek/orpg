package orpg.client.net.packets;

import orpg.shared.data.Direction;
import orpg.shared.net.ClientPacketType;
import orpg.shared.net.OutputByteBuffer;

public class MoveRequestPacket extends ClientPacket {

	/*
	 * Cache these classes since this will be a pretty common packet and there
	 * is only 4 possibilities. That way we won't constantly be creating objects
	 * for nothing.
	 */
	public static final MoveRequestPacket UP = new MoveRequestPacket(
			Direction.UP);
	public static final MoveRequestPacket DOWN = new MoveRequestPacket(
			Direction.DOWN);
	public static final MoveRequestPacket LEFT = new MoveRequestPacket(
			Direction.LEFT);
	public static final MoveRequestPacket RIGHT = new MoveRequestPacket(
			Direction.RIGHT);

	private byte[] bytes;

	private MoveRequestPacket(Direction direction) {
		OutputByteBuffer out = new OutputByteBuffer(1);
		out.putByte((byte) direction.ordinal());
		this.bytes = out.getBytes();
	}

	@Override
	public ClientPacketType getPacketType() {
		return ClientPacketType.CLIENT_MOVE_REQUEST;
	}

	@Override
	public byte[] getBytes() {
		return this.bytes;
	}

}
