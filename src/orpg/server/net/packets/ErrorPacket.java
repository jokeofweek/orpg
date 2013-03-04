package orpg.server.net.packets;

import orpg.server.ServerSession;
import orpg.shared.ErrorMessage;
import orpg.shared.net.ServerPacketType;
import orpg.shared.net.serialize.OutputByteBuffer;

public class ErrorPacket extends SessionPacket {

	private byte[] bytes;

	public ErrorPacket(ServerSession session, String message) {
		super(session);

		OutputByteBuffer out = new OutputByteBuffer();
		out.putBoolean(false); // whether it is a string or enum
		out.putString(message);
		this.bytes = out.getBytes();
	}
	
	public ErrorPacket(ServerSession session, ErrorMessage message) {
		super(session);

		OutputByteBuffer out = new OutputByteBuffer();
		out.putBoolean(true); // since it's an enum
		out.putInt(message.ordinal());
		this.bytes = out.getBytes();
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
