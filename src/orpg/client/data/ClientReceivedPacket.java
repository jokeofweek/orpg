package orpg.client.data;

import orpg.shared.net.InputByteBuffer;
import orpg.shared.net.ServerPacketType;

public class ClientReceivedPacket {

	private ServerPacketType type;
	private InputByteBuffer byteBuffer;

	public ClientReceivedPacket(ServerPacketType type, byte[] bytes) {
		super();
		this.type = type;
		this.byteBuffer = new InputByteBuffer(bytes);
	}

	public ServerPacketType getType() {
		return type;
	}

	public InputByteBuffer getByteBuffer() {
		return this.byteBuffer;
	}
}
