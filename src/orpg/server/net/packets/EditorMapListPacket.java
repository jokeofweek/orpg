package orpg.server.net.packets;

import orpg.server.ServerSession;
import orpg.shared.data.Map;
import orpg.shared.net.OutputByteBuffer;
import orpg.shared.net.ServerPacketType;

public class EditorMapListPacket extends SessionPacket {

	private byte[] bytes;

	public EditorMapListPacket(ServerSession session, Map[] maps) {
		super(session);
		OutputByteBuffer out = new OutputByteBuffer();
		out.putInt(maps.length); // number of maps
		this.bytes = out.getBytes();
	}

	@Override
	public ServerPacketType getPacketType() {
		return ServerPacketType.EDITOR_MAP_LIST;
	}

	@Override
	public byte[] getBytes() {
		return this.bytes;
	}

}
