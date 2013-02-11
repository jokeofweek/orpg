package orpg.editor.net.packets;

import orpg.client.net.packets.ClientPacket;
import orpg.shared.net.ClientPacketType;

public class EditorLoginPacket extends ClientPacket {

	@Override
	public ClientPacketType getPacketType() {
		return ClientPacketType.EDITOR_LOGIN;
	}

	@Override
	public byte[] getBytes() {
		return new byte[0];
	}

}
