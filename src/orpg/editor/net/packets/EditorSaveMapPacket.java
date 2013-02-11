package orpg.editor.net.packets;

import orpg.client.net.packets.ClientPacket;
import orpg.shared.data.Map;
import orpg.shared.net.ClientPacketType;
import orpg.shared.net.OutputByteBuffer;

public class EditorSaveMapPacket extends ClientPacket {

	private byte[] bytes;

	public EditorSaveMapPacket(Map map) {
		OutputByteBuffer out = new OutputByteBuffer();
		out.putMap(map);
		this.bytes = out.getBytes();
	}

	@Override
	public ClientPacketType getPacketType() {
		return ClientPacketType.EDITOR_SAVE_MAP;
	}

	@Override
	public byte[] getBytes() {
		return this.bytes;
	}

}
