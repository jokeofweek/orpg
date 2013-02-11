package orpg.client.net.packets;

import orpg.shared.net.ClientPacketType;

public abstract class ClientPacket {

	public abstract ClientPacketType getPacketType();

	public abstract byte[] getBytes();

	public final byte[] getRawBytes() {
		byte[] data = getBytes();
		byte[] bytes = new byte[data.length + 5];
		bytes[0] = (byte) getPacketType().ordinal();
		bytes[1] = (byte) ((data.length >> 24) & 0xff);
		bytes[2] = (byte) ((data.length >> 16) & 0xff);
		bytes[3] = (byte) ((data.length >> 8) & 0xff);
		bytes[4] = (byte) (data.length & 0xff);
		System.arraycopy(data, 0, bytes, 5, data.length);
		return bytes;
	}
}
