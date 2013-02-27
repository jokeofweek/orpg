package orpg.client.net.packets;

import orpg.client.BaseClient;
import orpg.shared.net.ClientPacketType;
import orpg.shared.net.OutputByteBuffer;

public class NeedSegmentPacket extends ClientPacket {

	private byte[] bytes;

	public NeedSegmentPacket(int segmentX, int segmentY) {
		OutputByteBuffer out = new OutputByteBuffer();
		out.putInt(segmentX);
		out.putInt(segmentY);
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
