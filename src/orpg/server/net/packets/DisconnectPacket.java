package orpg.server.net.packets;

import orpg.server.ServerSession;
import orpg.shared.Priority;
import orpg.shared.net.OutputByteBuffer;
import orpg.shared.net.ServerPacketType;

public class DisconnectPacket extends SessionPacket {

	private byte[] bytes;
	
	public DisconnectPacket(ServerSession session, String reason) {
		super(session);
		OutputByteBuffer out = new OutputByteBuffer();
		out.putString(reason);
		this.bytes = out.getBytes();
	}
	
	@Override
	public Priority getPriority() {
		return Priority.URGENT;
	}
	
	@Override
	public ServerPacketType getPacketType() {
		return ServerPacketType.DISCONNECT;
	}
	
	@Override
	public byte[] getBytes() {
		return this.bytes;
	}

}
