package orpg.shared.net;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

import orpg.client.data.ClientReceivedPacket;

/**
 * This thread is responsible for reading data from the client's socket and
 * adding it to the input queue.
 * 
 * @author Dominic Charley-Roy
 * 
 */
public class PacketReadThread implements Runnable {

	private Socket socket;
	private AbstractClient client;
	private Queue<ClientReceivedPacket> inputQueue;

	public PacketReadThread(Socket socket, AbstractClient client,
			BlockingQueue<ClientReceivedPacket> inputQueue) {
		this.client = client;
		this.socket = socket;
		this.inputQueue = inputQueue;
	}

	@Override
	public void run() {
		int tmpVal;
		int remaining = 0;
		ServerPacketType type;
		int length;

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
					this.client.disconnect("Invalid server packet type.");
					break;
				}

				// Must do some bit twiddling to convert the range of value from
				// [-128,127] to [0, 255]
				tmpVal &= 0x00ff;
				type = ServerPacketType.values()[tmpVal];

				// Read in length bytes
				tmpVal = in.read();
				if (tmpVal == -1) {
					throw new EOFException("End of stream.");
				}
				length = tmpVal;
				tmpVal = in.read();
				if (tmpVal == -1) {
					throw new EOFException("End of stream.");
				}
				length = (length << 8) | (tmpVal & 0x0ff);
				tmpVal = in.read();
				if (tmpVal == -1) {
					throw new EOFException("End of stream.");
				}
				length = (length << 8) | (tmpVal & 0x0ff);
				tmpVal = in.read();
				if (tmpVal == -1) {
					throw new EOFException("End of stream.");
				}
				length = (length << 8) | (tmpVal & 0x0ff);

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
				System.out.println("<- " + type + "(" + (bytes.length + 5)
						+ ")");
				inputQueue.add(new ClientReceivedPacket(type, bytes));
			}
		} catch (IOException io) {
			this.client.disconnect("End of stream.");
		}
	}

}
