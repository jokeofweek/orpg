package orpg.editor.net.packets;

import orpg.client.net.packets.ClientPacket;
import orpg.shared.net.ClientPacketType;
import orpg.shared.net.serialize.OutputByteBuffer;

public class EditorRequestSegmentPacket extends ClientPacket {

	private byte[] bytes;

	public EditorRequestSegmentPacket(int mapId, short x, short y) {
		OutputByteBuffer out = new OutputByteBuffer();
		out.putInt(mapId);
		out.putShort(x);
		out.putShort(y);
		this.bytes = out.getBytes();
	}

	@Override
	public ClientPacketType getPacketType() {
		return ClientPacketType.EDITOR_REQUEST_SEGMENT;
	}

	@Override
	public byte[] getBytes() {
		return this.bytes;
	}

}
