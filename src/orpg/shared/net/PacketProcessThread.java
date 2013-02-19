package orpg.shared.net;

import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

import orpg.client.data.ClientReceivedPacket;
import orpg.client.data.ClientSentPacket;
import orpg.client.net.BaseClient;
import orpg.client.net.packets.ClientPacket;
import orpg.editor.net.handlers.EditorPacketHandler;

/**
 * This thread is responsible for processing packets in the client's input
 * queue.
 * 
 * @author Dominic Charley-Roy
 * 
 */
public abstract class PacketProcessThread implements Runnable {

	private BlockingQueue<ClientReceivedPacket> inputQueue;
	private BlockingQueue<ClientPacket> outputQueue;
	private BaseClient baseClient;

	public BaseClient getBaseClient() {
		return baseClient;
	}

	public final void setBaseClient(BaseClient baseClient) {
		this.baseClient = baseClient;
	}

	public final void setInputQueue(
			BlockingQueue<ClientReceivedPacket> inputQueue) {
		this.inputQueue = inputQueue;
	}

	public final void setOutputQueue(
			BlockingQueue<ClientPacket> outputQueue) {
		this.outputQueue = outputQueue;
	}

	@Override
	public void run() {
		ClientReceivedPacket p;
		try {
			while (true) {
				p = inputQueue.take();
				handlePacket(p);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			baseClient.disconnect();
		}
	}

	public abstract void handlePacket(ClientReceivedPacket p);

}
