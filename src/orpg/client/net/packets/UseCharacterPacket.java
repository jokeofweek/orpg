package orpg.client.net.packets;

import orpg.shared.net.ClientPacketType;
import orpg.shared.net.OutputByteBuffer;

public class UseCharacterPacket extends ClientPacket {

	private byte[] bytes;

	public UseCharacterPacket(String characterName) {
		OutputByteBuffer out = new OutputByteBuffer();
		out.putString(characterName);
		this.bytes = out.getBytes();
	}

	@Override
	public ClientPacketType getPacketType() {
		return ClientPacketType.USE_CHARACTER;
	}

	@Override
	public byte[] getBytes() {
		return bytes;
	}

}
