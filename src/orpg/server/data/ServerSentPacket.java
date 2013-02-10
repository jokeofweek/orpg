package orpg.server.data;

import java.io.IOException;
import java.net.Socket;

import orpg.server.ServerSession;
import orpg.shared.Priority;
import orpg.shared.net.ServerPacketType;

public class ServerSentPacket implements Comparable<ServerSentPacket> {

	private DestinationType destinationType;
	private Object destinationObject;
	private byte[] bytes;
	private Priority priority;
	private ServerPacketType type;

	private ServerSentPacket(ServerPacketType type,
			DestinationType destinationType, Object destinationObject,
			Priority priority, byte... data) {
		super();

		this.destinationType = destinationType;
		this.destinationObject = destinationObject;
		this.priority = priority;
		this.type = type;
		
		// Create the bytes to send right away
		this.bytes = new byte[data.length + 5];
		this.bytes[0] = (byte)type.ordinal();
		this.bytes[1] = (byte)((data.length >> 24) & 0xff);
		this.bytes[2] = (byte)((data.length >> 16) & 0xff);
		this.bytes[3] = (byte)((data.length >> 8) & 0xff);
		this.bytes[4] = (byte)(data.length & 0xff);
		System.arraycopy(data, 0, bytes, 5, data.length);
	}

	public static ServerSentPacket getGlobalPacket(ServerPacketType type,
			Priority priority, byte... bytes) {
		return new ServerSentPacket(type, DestinationType.GLOBAL, null,
				priority, bytes);
	}

	public static ServerSentPacket getGlobalExceptForPacket(ServerPacketType type,
			Priority priority, ServerSentPacket session, byte... bytes) {
		return new ServerSentPacket(type, DestinationType.GLOBAL_EXCEPT_FOR, session,
				priority, bytes);
	}

	public static ServerSentPacket getSessionPacket(ServerPacketType type,
			Priority priority, ServerSession session, byte... bytes) {
		return new ServerSentPacket(type, DestinationType.SINGLE_SESSION,
				session, priority, bytes);
	}

	public Priority getPriority() {
		return priority;
	}

	/**
	 * @return the type of packet describing the recipient.
	 */
	public DestinationType getDestinationType() {
		return destinationType;
	}

	/**
	 * @return the object associated with the destination of the packet (eg. the
	 *         session for SINGLE_SESSION packets).
	 */
	public Object getDestinationObject() {
		return destinationObject;
	}

	@Override
	public int compareTo(ServerSentPacket o) {
		return this.getPriority().compareTo(o.getPriority());
	}

	public void write(Socket s) throws IOException {
		System.out.println("-> " + this.type + "(" + bytes.length + ")");
		s.getOutputStream().write(bytes);
	}

}
