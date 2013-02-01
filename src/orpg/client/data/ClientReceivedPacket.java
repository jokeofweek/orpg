package orpg.client.data;

import orpg.shared.ServerPacketType;

public class ClientReceivedPacket {

	private ServerPacketType type;
	private byte[] bytes;

	public ClientReceivedPacket(ServerPacketType type, byte[] bytes) {
		super();
		this.type = type;
		this.bytes = bytes;
	}

	public ServerPacketType getType() {
		return type;
	}

	public byte[] getBytes() {
		return bytes;
	}

}
