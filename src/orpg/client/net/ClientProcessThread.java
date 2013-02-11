package orpg.client.net;

import java.util.Queue;

import orpg.client.data.ClientReceivedPacket;
import orpg.client.data.ClientSentPacket;
import orpg.client.net.packets.ClientPacket;

/**
 * This thread is responsible for processing packets in the client's input
 * queue.
 * 
 * @author Dominic Charley-Roy
 * 
 */
public abstract class ClientProcessThread implements Runnable {

	private Queue<ClientReceivedPacket> inputQueue;
	private Queue<ClientPacket> outputQueue;
	private BaseClient baseClient;

	public BaseClient getBaseClient() {
		return baseClient;
	}
	
	public void setBaseClient(BaseClient baseClient) {
		this.inputQueue = baseClient.getInputQueue();
		this.outputQueue = baseClient.getOutputQueue();
		this.baseClient = baseClient;
	}
	
	public Queue<ClientPacket> getOutputQueue() {
		return outputQueue;
	}
	
	@Override
	public void run() {
		ClientReceivedPacket p;
		while (true) {
			if (!inputQueue.isEmpty()) {
				handlePacket(inputQueue.remove());
			} else {
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public abstract void handlePacket(ClientReceivedPacket p);

}
