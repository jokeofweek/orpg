package orpg.server.net.packets;

import orpg.server.ServerSession;
import orpg.shared.data.Segment;
import orpg.shared.net.ServerPacketType;
import orpg.shared.net.serialize.OutputByteBuffer;

public class ClientSegmentDataPacket extends SessionPacket {

	private byte[] bytes;

	public ClientSegmentDataPacket(ServerSession session, int mapId,
			Segment segment, boolean revisionsMatch) {
		super(session);
		OutputByteBuffer out = new OutputByteBuffer();
		out.putInt(mapId);
		out.putBoolean(revisionsMatch);

		if (!revisionsMatch) {
			out.putSegment(segment);
		} else {
			out.putShort(segment.getX());
			out.putShort(segment.getY());
		}

		out.putSegmentCharacters(segment);

		out.compress();
		this.bytes = out.getBytes();
	}

	@Override
	public ServerPacketType getPacketType() {
		return ServerPacketType.CLIENT_SEGMENT_DATA;
	}

	@Override
	public byte[] getBytes() {
		return this.bytes;
	}

}
