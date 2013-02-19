package orpg.server;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.PriorityQueue;
import java.util.Queue;

import orpg.server.data.ServerReceivedPacket;
import orpg.server.net.packets.DisconnectPacket;
import orpg.shared.Priority;
import orpg.shared.net.ClientPacketType;
import orpg.shared.net.OutputByteBuffer;
import orpg.shared.net.ServerPacketType;

public class ServerSessionThread extends Thread {

	private Socket socket;
	private BaseServer baseServer;
	private Queue<byte[]> outputQueue;
	private ServerSession session;

	private static final int READ_TICKS = 25;
	private static final int WRITE_TICKS = 25;

	public ServerSessionThread(BaseServer baseServer, Socket socket,
			ServerSession session) throws SocketException {
		this.socket = socket;
		this.socket.setSoTimeout(READ_TICKS);
		this.baseServer = baseServer;
		this.outputQueue = session.getOutputQueue();
		this.session = session;
	}

	@Override
	public void run() {
		// Incoming variables
		int tmpRead;
		ClientPacketType type = null;
		byte[] data = null;
		int remaining = 0;
		int currentPosition = 0;
		int readBytes;
		int sizeBytes = 0;

		byte[] outgoingPacket;

		long ticks;

		while (session.isConnected()) {
			// Handle reads
			ticks = System.currentTimeMillis() + READ_TICKS;
			do {
				try {
					// If we are awaiting size bytes, then read them in one at a
					// time
					if (sizeBytes > 0) {
						tmpRead = socket.getInputStream().read();

						if (tmpRead == -1) {
							throw new EOFException("End of stream");
						}
						remaining <<= 8;
						remaining |= (tmpRead & 0x0ff);
						sizeBytes--;
						if (sizeBytes == 0) {
							data = new byte[remaining];
						}
					} else if (remaining == 0 && type == null) {
						// New packet
						tmpRead = this.socket.getInputStream().read();
						// Bit-twiddling must be done here to convert it back to
						// an integer [0-255]
						tmpRead &= 0x00ff;
						if (tmpRead >= ClientPacketType.values().length) {
							// Invalid packet type...
							session.disconnect("Invalid packet type.");
						} else if (tmpRead == -1) {
							throw new EOFException("End of stream");
						} else {
							type = ClientPacketType.values()[tmpRead];
							remaining = 0;
							sizeBytes = 4;
						}
					} else {
						// Read in as many bytes as we can
						readBytes = this.socket.getInputStream().read(
								data, currentPosition, remaining);

						if (readBytes == -1) {
							throw new EOFException("End of stream.");
						}

						remaining -= readBytes;
						currentPosition += readBytes;

						// If we have 0 remaining, then we have a complete
						// packet.
						if (remaining == 0) {
							// Test the packet to make sure it is valid.
							System.out.println("<- " + type + "("
									+ (data.length + 5) + ")");
							baseServer
									.receivePacket(new ServerReceivedPacket(
											this.session, type, data,
											Priority.MEDIUM));
							currentPosition = 0;
							type = null;
						}
					}
				} catch (SocketTimeoutException e) {
					// This is fine...
				} catch (IOException e) {
					// Socket disconnected!
					session.disconnect();
					return;
				}

			} while (ticks > System.currentTimeMillis());

			// Handle writes
			ticks = System.currentTimeMillis() + WRITE_TICKS;
			do {
				if (!outputQueue.isEmpty()) {
					outgoingPacket = outputQueue.remove();
					try {
						this.socket.getOutputStream()
								.write(outgoingPacket);
					} catch (IOException e) {
						session.disconnect();
					}
				} else {
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} while (ticks > System.currentTimeMillis());
		}

		// Close the socket here, and write out a disconnect message
		if (socket.isConnected()) {
			try {
				socket.getOutputStream().write(
						new DisconnectPacket(session, session
								.getDisconnectReason()).getRawBytes());
			} catch (IOException e) {
			}

			try {
				socket.close();
			} catch (IOException e) {
			}

		}

	}
}
