package orpg.client;

import java.util.Queue;

import orpg.client.data.ClientReceivedPacket;
import orpg.shared.ByteStream;

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
				Object[] objects = ByteStream.unserialize(p.getBytes(),
						String.class);
				ClientWindow.textArea.setText(ClientWindow.textArea
						.getText() + objects[0] + "\r\n");
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
