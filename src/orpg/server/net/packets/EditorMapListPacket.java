package orpg.server.net.packets;

import java.util.List;

import orpg.server.ServerSession;
import orpg.server.data.controllers.MapController;
import orpg.shared.data.Map;
import orpg.shared.data.Pair;
import orpg.shared.net.OutputByteBuffer;
import orpg.shared.net.ServerPacketType;

public class EditorMapListPacket extends SessionPacket {

	private byte[] bytes;

	public EditorMapListPacket(ServerSession session, List<String> names) {
		super(session);
		OutputByteBuffer out = new OutputByteBuffer();
		out.putInt(names.size()); // number of maps
		// Output the name of the map
		for (String name : names) {
			out.putString(name);
		}
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
