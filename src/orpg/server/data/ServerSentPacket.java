package orpg.server.data;

import java.io.IOException;
import java.net.Socket;

import orpg.server.ServerSession;
import orpg.shared.ByteStream;
import orpg.shared.Priority;
import orpg.shared.ServerPacketType;

public class ServerSentPacket implements Comparable<ServerSentPacket> {

	private DestinationType destinationType;
	private Object destinationObject;
	private byte[] data;
	private Priority priority;

	private ServerSentPacket(ServerPacketType type,
			DestinationType destinationType, Object destinationObject,
			Priority priority, Object[] objects) {
		super();
		this.destinationType = destinationType;
		this.destinationObject = destinationObject;
		this.priority = priority;

		// serialize the objects right away
		this.data = ByteStream.serialize((byte) type.ordinal(), objects);
	}

	public static ServerSentPacket getGlobalPacket(ServerPacketType type,
			Priority priority, Object... objects) {
		return new ServerSentPacket(type, DestinationType.GLOBAL, null,
				priority, objects);
	}

	public static ServerSentPacket getGlobalExceptForPacket(ServerPacketType type,
			Priority priority, ServerSentPacket session, Object... objects) {
		return new ServerSentPacket(type, DestinationType.GLOBAL_EXCEPT_FOR, session,
				priority, objects);
	}

	public static ServerSentPacket getSessionPacket(ServerPacketType type,
			Priority priority, ServerSession session, Object... objects) {
		return new ServerSentPacket(type, DestinationType.SINGLE_SESSION,
				session, priority, objects);
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
		s.getOutputStream().write(data);
	}

}
