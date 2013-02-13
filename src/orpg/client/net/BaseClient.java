package orpg.client.net;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

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

	private Queue<ClientReceivedPacket> inputQueue;
	private Queue<ClientPacket> outputQueue;

	public BaseClient(Socket socket, Class clientProcessThreadClass) {
		// Setup the input and output queues
		this.inputQueue = new LinkedList<ClientReceivedPacket>();
		this.outputQueue = new LinkedList<ClientPacket>();
		this.socket = socket;

		// Setup our process thread
		try {
			this.gameThread = (PacketProcessThread) clientProcessThreadClass
					.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Could not set up game thread.", e);
		}
		this.gameThread.setBaseClient(this);

		// Setup the necessary read/write threads
		this.readThread = new PacketReadThread(socket, this);
		this.writeThread = new PacketWriteThread(socket, this);

		// Run the threads
		new Thread(gameThread).start();
		new Thread(readThread).start();
		new Thread(writeThread).start();
	}

	public Queue<ClientReceivedPacket> getInputQueue() {
		return inputQueue;
	}

	public Queue<ClientPacket> getOutputQueue() {
		return outputQueue;
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
