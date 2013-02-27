package orpg.server.net.packets;

import orpg.server.ServerSession;
import orpg.shared.data.Segment;
import orpg.shared.net.OutputByteBuffer;
import orpg.shared.net.ServerPacketType;

public class EditorMapSegmentDataPacket extends SessionPacket {

	private byte[] bytes;

	public EditorMapSegmentDataPacket(ServerSession session, int mapId,
			Segment segment) {
		super(session);
		OutputByteBuffer out = new OutputByteBuffer();
		out.putInt(mapId);
		out.putSegment(segment, false);
		out.compress();
		this.bytes = out.getBytes();
	}

	@Override
	public ServerPacketType getPacketType() {
		return ServerPacketType.EDITOR_MAP_SEGMENT_DATA;
	}

	@Override
	public byte[] getBytes() {
		return this.bytes;
	}

}
