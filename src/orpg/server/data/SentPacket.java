package orpg.server.data;

import java.io.IOException;
import java.net.Socket;

import orpg.server.ServerSession;
import orpg.shared.Priority;
import orpg.shared.ServerPacketType;

public class SentPacket implements Comparable<SentPacket> {

	private DestinationType destinationType;
	private Object destinationObject;
	private byte[] data;
	private Priority priority;

	private SentPacket(ServerPacketType type, byte[] bytes,
			DestinationType destinationType, Object destinationObject,
			Priority priority) {
		super();
		this.destinationType = destinationType;
		this.destinationObject = destinationObject;
		this.priority = priority;

		// build the data before hand. currently done this
		// way to simplify for global packets as well as to
		// ensure that either all is written or nothing. we
		// don't want to write the size data and then there
		// is an exception and we don't write the real data..
		
		this.data = new byte[bytes.length + 3];
		this.data[0] = (byte) type.ordinal();
		this.data[1] = (byte) (bytes.length >> 8);
		this.data[2] = (byte) (bytes.length % 0xff);
		for (int i = 0; i < bytes.length; i++) {
			this.data[3 + i] = bytes[i];
		}
	}

	public static SentPacket getGlobalPacket(ServerPacketType type,
			byte[] data, Priority priority) {
		return new SentPacket(type, data, DestinationType.GLOBAL, null,
				priority);
	}

	public static SentPacket getSessionPacket(ServerPacketType type,
			byte[] data, Priority priority, ServerSession session) {
		return new SentPacket(type, data, DestinationType.SINGLE_SESSION,
				session, priority);
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
	public int compareTo(SentPacket o) {
		return this.getPriority().compareTo(o.getPriority());
	}

	public void write(Socket s) throws IOException {
		s.getOutputStream().write(data);
	}

}
