package orpg.client;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Queue;

import orpg.client.data.ClientReceivedPacket;
import orpg.shared.ByteStream;
import orpg.shared.ServerPacketType;

/**
 * This thread is responsible for reading data from the client's socket and
 * adding it to the input queue.
 * 
 * @author Dominic Charley-Roy
 * 
 */
public class ClientReadThread implements Runnable {

	private Socket socket;
	private BaseClient baseClient;
	private Queue<ClientReceivedPacket> inputQueue;

	public ClientReadThread(Socket socket, BaseClient baseClient) {
		this.socket = socket;
		this.baseClient = baseClient;
		this.inputQueue = baseClient.getInputQueue();
	}

	@Override
	public void run() {
		int tmpVal;
		int remaining = 0;
		ServerPacketType type;
		short length;

		byte lengthBytes[] = new byte[2];
		byte bytes[];

		try {
			InputStream in = socket.getInputStream();

			while (true) {
				// Read in type
				tmpVal = in.read();
				if (tmpVal == -1) {
					throw new EOFException("End of stream.");
				} else if (tmpVal < -1
						|| tmpVal >= ServerPacketType.values().length) {
					this.baseClient
							.disconnect("Invalid server packet type.");
					break;
				}

				type = ServerPacketType.values()[tmpVal];

				// Read in length
				remaining = 2;
				while (remaining > 0) {
					tmpVal = in
							.read(lengthBytes, 2 - remaining, remaining);
					if (tmpVal == -1) {
						throw new EOFException("End of stream.");
					}
					remaining -= tmpVal;
				}

				length = ByteStream.readShort(lengthBytes, 0);
				bytes = new byte[length];

				// Read in packet data
				remaining = length;
				while (remaining > 0) {
					tmpVal = in.read(bytes, length - remaining, remaining);
					if (tmpVal == -1) {
						throw new EOFException("End of stream.");
					}
					remaining -= tmpVal;
				}

				// Add the packet to the input queue
				inputQueue.add(new ClientReceivedPacket(type, bytes));
			}
		} catch (IOException io) {
			this.baseClient.disconnect();
		}
	}
}
