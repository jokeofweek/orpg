package orpg.server;

import java.io.IOException;
import java.util.PriorityQueue;

import orpg.server.config.ConfigurationManager;
import orpg.server.console.ServerConsole;
import orpg.server.data.ServerReceivedPacket;
import orpg.server.data.ServerSentPacket;

public class BaseServer {

	private ConfigurationManager config;
	private ServerSessionManager serverSessionManager;
	private ServerGameThread serverGameThread;
	private ServerSocketThread serverSocketThread;
	private ServerConsole console;
	private PriorityQueue<ServerReceivedPacket> inputQueue;
	private PriorityQueue<ServerSentPacket> outputQueue;

	public BaseServer(ConfigurationManager config, ServerConsole console) {
		this.config = config;
		this.console = console;

		boolean encounteredSetupProblems = false;

		// Set up our queues
		this.inputQueue = new PriorityQueue<ServerReceivedPacket>(1000);
		this.outputQueue = new PriorityQueue<ServerSentPacket>(1000);

		// Set up the various threads
		this.serverSessionManager = new ServerSessionManager(this);
		this.serverGameThread = new ServerGameThread(this);
		try {
			this.serverSocketThread = new ServerSocketThread(this,
					config.getServerPort());
		} catch (IOException e) {
			console.out().println(
					"Could not setup server socket thread. Error: "
							+ e.getMessage());
			e.printStackTrace(console.out());
			encounteredSetupProblems = true;
		}

		// Close if any setup problems
		if (encounteredSetupProblems) {
			console.out().println(
					"Encountered setup problems. Shutting down...");
			System.exit(1);
		}

		// Now that everything is setup, we are ready to go.
		console.out().println("Starting threads...");
		new Thread(serverSessionManager).start();
		new Thread(serverGameThread).start();
		new Thread(serverSocketThread).start();
		console.out().println("Ready to go!");
	}

	public ConfigurationManager getConfigurationManager() {
		return config;
	}

	public ServerConsole getConsole() {
		return console;
	}

	public PriorityQueue<ServerReceivedPacket> getInputQueue() {
		return inputQueue;
	}

	public PriorityQueue<ServerSentPacket> getOutputQueue() {
		return outputQueue;
	}

	public ServerSessionManager getServerSessionManager() {
		return serverSessionManager;
	}

}
