package orpg.shared.net;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import orpg.client.net.BaseClient;
import orpg.client.net.packets.ClientPacket;

/**
 * This thread is responsible for writing data from the output queue to the
 * client's socket.
 * 
 * @author Dominic Charley-Roy
 * 
 */
public class PacketWriteThread implements Runnable {

	private Socket socket;
	private BaseClient baseClient;
	private BlockingQueue<ClientPacket> outputQueue;

	public PacketWriteThread(Socket socket, BaseClient baseClient,
			BlockingQueue<ClientPacket> outputQueue) {
		this.socket = socket;
		this.baseClient = baseClient;
		this.outputQueue = outputQueue;
	}

	@Override
	public void run() {
		ClientPacket p;
		try {
			// Repeatedly remove from the output queue and write.
			while (true) {
				p = outputQueue.take();
				socket.getOutputStream().write(p.getRawBytes());
			}
		} catch (IOException io) {
			this.baseClient.disconnect();
		} catch (InterruptedException e) {
			e.printStackTrace();
			this.baseClient.disconnect();
		}
	}
}
