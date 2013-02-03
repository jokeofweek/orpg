package orpg.client;

import java.util.Queue;

import orpg.client.data.ClientReceivedPacket;

/**
 * This thread is responsible for processing packets in the client's input
 * queue.
 * 
 * @author Dominic Charley-Roy
 * 
 */
public class ClientGameThread implements Runnable {

	private Queue<ClientReceivedPacket> inputQueue;

	public ClientGameThread(BaseClient baseClient) {
		this.inputQueue = baseClient.getInputQueue();
	}

	@Override
	public void run() {
		ClientReceivedPacket p;
		while (true) {
			if (!inputQueue.isEmpty()) {
				p = inputQueue.remove();

				// Just print out the message.
				ClientWindow.textArea.setText(ClientWindow.textArea
						.getText()
						+ p.getByteBuffer().getString()
						+ "\r\n");
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

}
