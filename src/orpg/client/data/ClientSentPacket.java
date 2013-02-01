package orpg.client.data;

import java.io.IOException;
import java.net.Socket;

import orpg.shared.ByteStream;
import orpg.shared.ClientPacketType;

public class ClientSentPacket {

	private byte[] bytes;

	public ClientSentPacket(ClientPacketType type, Object... objects) {
		this.bytes = ByteStream.serialize((byte)type.ordinal(), objects);
	}

	public void write(Socket s) throws IOException {
		s.getOutputStream().write(bytes);
	}
	
}
