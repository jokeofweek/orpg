package orpg.editor.net.packets;

import orpg.client.net.packets.ClientPacket;
import orpg.shared.net.ClientPacketType;
import orpg.shared.net.OutputByteBuffer;

public class EditorRequestSegmentPacket extends ClientPacket {

	private byte[] bytes;

	public EditorRequestSegmentPacket(int mapId, int x, int y) {
		OutputByteBuffer out = new OutputByteBuffer();
		out.putInt(mapId);
		out.putInt(x);
		out.putInt(y);
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
