package orpg.server.net.packets;

import orpg.server.ServerSession;
import orpg.shared.data.Map;
import orpg.shared.data.Segment;
import orpg.shared.net.OutputByteBuffer;
import orpg.shared.net.ServerPacketType;

public class EditorMapDataPacket extends SessionPacket {

	private byte[] bytes;

	public EditorMapDataPacket(ServerSession session, Map map, Segment segment) {
		super(session);
		OutputByteBuffer out = new OutputByteBuffer();
		out.putMapDescriptor(map);
		out.putSegment(segment, false);
		out.compress();
		this.bytes = out.getBytes();
	}

	@Override
	public ServerPacketType getPacketType() {
		return ServerPacketType.EDITOR_MAP_DATA;
	}

	@Override
	public byte[] getBytes() {
		return bytes;
	}

}
