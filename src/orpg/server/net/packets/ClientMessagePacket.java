package orpg.server.net.packets;

import orpg.shared.data.Message;
import orpg.shared.net.ServerPacketType;
import orpg.shared.net.serialize.OutputByteBuffer;

public class ClientMessagePacket extends GlobalPacket {

	private byte[] bytes;

	public ClientMessagePacket(Message message) {
		OutputByteBuffer out = new OutputByteBuffer();
		out.putValue(message);
		this.bytes = out.getBytes();
	}

	@Override
	public ServerPacketType getPacketType() {
		return ServerPacketType.CLIENT_MESSAGE;
	}

	@Override
	public byte[] getBytes() {
		return this.bytes;
	}

}
