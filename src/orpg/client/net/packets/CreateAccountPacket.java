package orpg.client.net.packets;

import orpg.shared.net.ClientPacketType;
import orpg.shared.net.OutputByteBuffer;

public class CreateAccountPacket extends ClientPacket {

	private byte[] bytes;

	public CreateAccountPacket(String accountName, String email, char[] password) {
		OutputByteBuffer out = new OutputByteBuffer();
		out.putString(accountName);
		out.putString(email);
		out.putCharArray(password);
		this.bytes = out.getBytes();
	}

	@Override
	public ClientPacketType getPacketType() {
		return ClientPacketType.CREATE_ACCOUNT;
	}

	@Override
	public byte[] getBytes() {
		return bytes;
	}

}
