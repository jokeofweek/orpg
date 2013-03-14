package orpg.server.net.packets;

import javax.swing.text.Position;

import orpg.server.ServerSession;
import orpg.shared.data.Map;
import orpg.shared.net.ServerPacketType;
import orpg.shared.net.serialize.OutputByteBuffer;

public class ClientNewMapPacket extends SessionPacket {

	private byte[] bytes;

	public ClientNewMapPacket(ServerSession session, Map map, int x, int y) {
		super(session);

		OutputByteBuffer out = new OutputByteBuffer();
		out.putMapDescriptor(map);

		// Need to put in client x and y to determine the segment we need
		out.putInt(x);
		out.putInt(y);

		this.bytes = out.getBytes();
	}

	@Override
	public ServerPacketType getPacketType() {
		return ServerPacketType.CLIENT_NEW_MAP;
	}

	@Override
	public byte[] getBytes() {
		return this.bytes;
	}

}
