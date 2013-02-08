package orpg.client.data;

import java.io.IOException;
import java.net.Socket;

import orpg.shared.net.ClientPacketType;

public class ClientSentPacket {

	private byte[] bytes;

	public ClientSentPacket(ClientPacketType type, byte... data) {

		System.out.println("-> " + type + "(" + (data.length + 5) + "):");
		// Create the bytes to send right away
		this.bytes = new byte[data.length + 5];
		this.bytes[0] = (byte)type.ordinal();
		this.bytes[1] = (byte)((data.length >> 24) & 0xff);
		this.bytes[2] = (byte)((data.length >> 16) & 0xff);
		this.bytes[3] = (byte)((data.length >> 8) & 0xff);
		this.bytes[4] = (byte)(data.length & 0xff);
		System.arraycopy(data, 0, bytes, 5, data.length);
	}

	public void write(Socket s) throws IOException {
		s.getOutputStream().write(this.bytes);
	}
	
}
