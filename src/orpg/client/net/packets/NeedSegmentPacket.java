package orpg.client.net.packets;

import orpg.client.BaseClient;
import orpg.shared.net.ClientPacketType;
import orpg.shared.net.OutputByteBuffer;

public class NeedSegmentPacket extends ClientPacket {

	private byte[] bytes;

	public NeedSegmentPacket(int mapId, int segmentX, int segmentY, int revision, long revisionTime) {
		OutputByteBuffer out = new OutputByteBuffer(24);
		out.putInt(mapId);
		out.putInt(segmentX);
		out.putInt(segmentY);
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
