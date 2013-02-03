package orpg.client;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Queue;

import orpg.client.data.ClientReceivedPacket;
import orpg.client.data.ClientSentPacket;
import orpg.shared.net.ServerPacketType;

/**
 * This thread is responsible for writing data from the output queue
 * to the client's socket. 
 * @author Dominic Charley-Roy
 * 
 */
public class ClientWriteThread implements Runnable {

	private Socket socket;
	private BaseClient baseClient;
	private Queue<ClientSentPacket> outputQueue;

	public ClientWriteThread(Socket socket, BaseClient baseClient) {
		this.socket = socket;
		this.baseClient = baseClient;
		this.outputQueue = baseClient.getOutputQueue();
	}

	@Override
	public void run() {
		ClientSentPacket p;
		try {
			// Repeatedly remove from the output queue and write.
			while (true) {
				if (!outputQueue.isEmpty()) {
					p = outputQueue.remove();
					p.write(socket);
				} else {
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (IOException io) {
			this.baseClient.disconnect();
		}
	}
}
