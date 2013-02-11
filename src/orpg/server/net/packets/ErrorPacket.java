package orpg.server.net.packets;

import orpg.server.ServerSession;
import orpg.shared.Priority;
import orpg.shared.net.OutputByteBuffer;
import orpg.shared.net.ServerPacketType;

public class ErrorPacket extends SessionPacket {

	private String message;
	private byte[] bytes;

	public ErrorPacket(ServerSession session, String message) {
		super(session);
		this.message = message;

		OutputByteBuffer out = new OutputByteBuffer();
		out.putString(message);
		this.bytes = out.getBytes();
	}

	@Override
	public Priority getPriority() {
		return Priority.URGENT;
	}

	@Override
	public ServerPacketType getPacketType() {
		return ServerPacketType.ERROR;
	}

	@Override
	public byte[] getBytes() {
		return bytes;
	}

}
