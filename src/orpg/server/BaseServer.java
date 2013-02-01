package orpg.server;

import java.io.IOException;
import java.util.PriorityQueue;

import orpg.server.config.ConfigurationManager;
import orpg.server.console.ServerConsole;
import orpg.server.data.ReceivedPacket;
import orpg.server.data.SentPacket;

public class BaseServer {

	private ConfigurationManager config;
	private ServerSessionManager serverSessionManager;
	private ServerGameThread serverGameThread;
	private ServerSocketThread serverSocketThread;
	private ServerConsole console;
	private PriorityQueue<ReceivedPacket> input;
	private PriorityQueue<SentPacket> output;

	public BaseServer(ConfigurationManager config, ServerConsole console) {
		this.config = config;
		this.console = console;

		boolean encounteredSetupProblems = false;

		// Set up our queues
		this.input = new PriorityQueue<ReceivedPacket>(1000);
		this.output = new PriorityQueue<SentPacket>(1000);

		// Set up the various threads
		this.serverSessionManager = new ServerSessionManager(this);
		this.serverGameThread = new ServerGameThread(this);
		try {
			this.serverSocketThread = new ServerSocketThread(
					config.getServerPort(), this);
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

	public PriorityQueue<ReceivedPacket> getInputQueue() {
		return input;
	}

	public PriorityQueue<SentPacket> getOutputQueue() {
		return output;
	}

	public ServerSessionManager getServerSessionManager() {
		return serverSessionManager;
	}

}
