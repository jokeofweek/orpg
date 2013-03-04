package orpg.client.net.packets;

import orpg.shared.net.ClientPacketType;
import orpg.shared.net.serialize.OutputByteBuffer;

public class NeedSegmentPacket extends ClientPacket {

	private byte[] bytes;

	public NeedSegmentPacket(int mapId, short segmentX, short segmentY, int revision, long revisionTime) {
		OutputByteBuffer out = new OutputByteBuffer(20);
		out.putInt(mapId);
		out.putShort(segmentX);
		out.putShort(segmentY);
		out.putInt(revision);
		out.putLong(revisionTime);
		this.bytes = out.getBytes();
	}

	@Override
	public ClientPacketType getPacketType() {
		return ClientPacketType.CLIENT_NEED_SEGMENT;
	}

	@Override
	public byte[] getBytes() {
		// TODO Auto-generated method stub
		return this.bytes;
	}

}
