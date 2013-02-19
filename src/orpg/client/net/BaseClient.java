package orpg.client.net;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import orpg.client.data.ClientReceivedPacket;
import orpg.client.data.ClientSentPacket;
import orpg.client.net.packets.ClientPacket;
import orpg.shared.net.PacketProcessThread;
import orpg.shared.net.PacketReadThread;
import orpg.shared.net.PacketWriteThread;

public class BaseClient {

	private Socket socket;

	private PacketReadThread readThread;
	private PacketWriteThread writeThread;
	private PacketProcessThread gameThread;

	private BlockingQueue<ClientReceivedPacket> inputQueue;
	private BlockingQueue<ClientPacket> outputQueue;

	public BaseClient(Socket socket, PacketProcessThread gameThread) {
		// Setup the input and output queues
		this.inputQueue = new LinkedBlockingQueue<ClientReceivedPacket>();
		this.outputQueue = new LinkedBlockingQueue<ClientPacket>();
		this.socket = socket;

		// Setup our process thread
		this.gameThread = gameThread;
		this.gameThread.setBaseClient(this);
		this.gameThread.setInputQueue(this.inputQueue);
		this.gameThread.setOutputQueue(this.outputQueue);

		// Setup the necessary read/write threads
		this.readThread = new PacketReadThread(socket, this,
				this.inputQueue);
		this.writeThread = new PacketWriteThread(socket, this,
				this.outputQueue);

		// Run the threads
		new Thread(gameThread).start();
		new Thread(readThread).start();
		new Thread(writeThread).start();
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
