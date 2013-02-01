package orpg.server.data;

import orpg.server.ServerSession;
import orpg.shared.ClientPacketType;
import orpg.shared.Priority;

public class ReceivedPacket implements Comparable<ReceivedPacket> {

	private ServerSession session;
	private ClientPacketType type;
	private byte[] bytes;	
	private Priority priority;
	
	public ReceivedPacket(ServerSession session, ClientPacketType type, byte[] bytes, Priority priority) {
		super();
		this.session = session;
		this.type = type;
		this.bytes = bytes;
	}
	
	public ClientPacketType getType() {
		return type;
	}
	public byte[] getBytes() {
		return bytes;
	}
	
	public ServerSession getSession() {
		return session;
	}
	
	public Priority getPriority() {
		return priority;
	}
	
	@Override
	public int compareTo(ReceivedPacket o) {
		return this.getPriority().compareTo(o.getPriority());
	}
	
	
}
