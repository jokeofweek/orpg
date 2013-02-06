package orpg.client;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

import orpg.client.data.ClientReceivedPacket;
import orpg.client.data.ClientSentPacket;

public class BaseClient {

	private Socket socket;

	private ClientReadThread readThread;
	private ClientWriteThread writeThread;
	private ClientGameThread gameThread;

	private Queue<ClientReceivedPacket> inputQueue;
	private Queue<ClientSentPacket> outputQueue;

	public BaseClient(Socket socket) {
		// Setup the input and output queues
		this.inputQueue = new LinkedList<ClientReceivedPacket>();
		this.outputQueue = new LinkedList<ClientSentPacket>();
		this.socket = socket;
		
		new ClientWindow(this);
		
		// Setup the necessary threads
		this.gameThread = new ClientGameThread(this);
		this.readThread = new ClientReadThread(socket, this);
		this.writeThread = new ClientWriteThread(socket, this);
				
		// Run the threads
		new Thread(gameThread).start();
		new Thread(readThread).start();
		new Thread(writeThread).start();
	}

	public Queue<ClientReceivedPacket> getInputQueue() {
		return inputQueue;
	}
	
	public Queue<ClientSentPacket> getOutputQueue() {
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
