package orpg.client.net.packets;

import orpg.shared.net.ClientPacketType;
import orpg.shared.net.serialize.OutputByteBuffer;

public class LoginPacket extends ClientPacket {

	private byte[] bytes;

	public LoginPacket(String name, char[] password,
			boolean editorLogin) {
		OutputByteBuffer out = new OutputByteBuffer();
		out.putBoolean(editorLogin);
		out.putString(name);
		out.putCharArray(password);
		this.bytes = out.getBytes();
	}

	@Override
	public ClientPacketType getPacketType() {
		return ClientPacketType.LOGIN;
	}

	@Override
	public byte[] getBytes() {
		return this.bytes;
	}

}
