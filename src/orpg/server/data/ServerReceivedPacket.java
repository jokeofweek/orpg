package orpg.server.data;

import orpg.server.ServerSession;
import orpg.shared.net.ClientPacketType;
import orpg.shared.net.serialize.InputByteBuffer;

public class ServerReceivedPacket {

	private ServerSession session;
	private ClientPacketType type;
	private InputByteBuffer buffer;
	
	public ServerReceivedPacket(ServerSession session, ClientPacketType type, byte[] bytes) {
		super();
		this.session = session;
		this.type = type;
		this.buffer = new InputByteBuffer(bytes);
	}
	
	public ClientPacketType getType() {
		return type;
	}

	public InputByteBuffer getByteBuffer() {
		return buffer;
	}
	
	public ServerSession getSession() {
		return session;
	}
	
}
