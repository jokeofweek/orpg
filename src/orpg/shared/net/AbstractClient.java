package orpg.shared.net;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import orpg.client.data.ClientReceivedPacket;
import orpg.client.net.packets.ClientPacket;
import orpg.shared.state.StateManager;

public class AbstractClient {

	private Socket socket;

	private PacketReadThread readThread;
	private PacketWriteThread writeThread;
	private PacketProcessThread gameThread;

	private BlockingQueue<ClientReceivedPacket> inputQueue;
	private BlockingQueue<ClientPacket> outputQueue;

	private StateManager stateManager;

	public AbstractClient(Socket socket, PacketProcessThread gameThread,
			StateManager stateManager) {
		// Setup the input and output queues
		this.inputQueue = new LinkedBlockingQueue<ClientReceivedPacket>();
		this.outputQueue = new LinkedBlockingQueue<ClientPacket>();
		this.socket = socket;

		// Setup the state manager
		this.stateManager = stateManager;

		// Setup our process thread
		this.gameThread = gameThread;
		this.gameThread.setBaseClient(this);
		this.gameThread.setInputQueue(this.inputQueue);
		this.gameThread.setOutputQueue(this.outputQueue);

		// Setup the necessary read/write threads
		this.readThread = new PacketReadThread(socket, this, this.inputQueue);
		this.writeThread = new PacketWriteThread(socket, this, this.outputQueue);

		// Run the threads
		new Thread(gameThread).start();
		new Thread(readThread).start();
		new Thread(writeThread).start();
	}

	public StateManager getStateManager() {
		return stateManager;
	}

	public void sendPacket(ClientPacket packet) {
		this.outputQueue.add(packet);
	}

	public void disconnect() {
		disconnect("Server disconnect.");
	}

	public void disconnect(String reason) {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.exit(0);
	}

}
