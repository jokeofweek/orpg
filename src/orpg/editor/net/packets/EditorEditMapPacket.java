package orpg.editor.net.packets;

import orpg.client.net.packets.ClientPacket;
import orpg.shared.net.ClientPacketType;
import orpg.shared.net.serialize.OutputByteBuffer;

public class EditorEditMapPacket extends ClientPacket {

	private byte[] bytes;

	public EditorEditMapPacket(int mapNumber) {
		OutputByteBuffer out = new OutputByteBuffer(4);
		out.putInt(mapNumber);
		this.bytes = out.getBytes();
	}

	@Override
	public ClientPacketType getPacketType() {
		return ClientPacketType.EDITOR_EDIT_MAP;
	}

	@Override
	public byte[] getBytes() {
		return this.bytes;
	}

}
