package orpg.server.data;

import orpg.server.ServerSession;
import orpg.shared.Priority;
import orpg.shared.net.ClientPacketType;
import orpg.shared.net.InputByteBuffer;
import orpg.shared.net.OutputByteBuffer;

public class ServerReceivedPacket implements Comparable<ServerReceivedPacket> {

	private ServerSession session;
	private ClientPacketType type;
	private InputByteBuffer buffer;
	private Priority priority;
	
	public ServerReceivedPacket(ServerSession session, ClientPacketType type, byte[] bytes, Priority priority) {
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
	
	public Priority getPriority() {
		return priority;
	}
	
	@Override
	public int compareTo(ServerReceivedPacket o) {
		return this.getPriority().compareTo(o.getPriority());
	}
	
	
}
