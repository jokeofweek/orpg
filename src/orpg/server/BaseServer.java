package orpg.server;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import orpg.server.config.ConfigurationManager;
import orpg.server.console.ServerConsole;
import orpg.server.data.FileSystem;
import orpg.server.data.ServerReceivedPacket;
import orpg.server.data.ServerSentPacket;
import orpg.server.data.managers.MapManager;
import orpg.shared.Constants;
import orpg.shared.Priority;
import orpg.shared.data.Map;
import orpg.shared.net.OutputByteBuffer;
import orpg.shared.net.ServerPacketType;

public class BaseServer {

	private ConfigurationManager config;
	private ServerSessionManager serverSessionManager;
	private ServerGameThread serverGameThread;
	private ServerSocketThread serverSocketThread;
	private ServerConsole console;
	private Queue<ServerReceivedPacket> inputQueue;
	private PriorityQueue<ServerSentPacket> outputQueue;
	private MapManager mapManager;

	public BaseServer(ConfigurationManager config, ServerConsole console) {
		this.config = config;
		this.console = console;

		boolean encounteredSetupProblems = false;

		// Set up our queues
		this.inputQueue = new LinkedList<ServerReceivedPacket>();
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

		// Load all necessary data
		if (!encounteredSetupProblems) {
			encounteredSetupProblems = !loadData();
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

	public ConfigurationManager getConfigManager() {
		return config;
	}

	public ServerConsole getConsole() {
		return console;
	}

	public Queue<ServerReceivedPacket> getInputQueue() {
		return inputQueue;
	}

	public PriorityQueue<ServerSentPacket> getOutputQueue() {
		return outputQueue;
	}

	public ServerSessionManager getServerSessionManager() {
		return serverSessionManager;
	}

	public MapManager getMapManager() {
		return mapManager;
	}

	/**
	 * @return true is the loading was successful, else false
	 */
	private boolean loadData() {
		console.out().println("Loading maps...");
		this.mapManager = new MapManager(this);
		return this.mapManager.load();
	}

	public void sendEditorMapData(ServerSession session, Map map) {
		OutputByteBuffer out = new OutputByteBuffer();
		out.putMap(map);
		outputQueue.add(ServerSentPacket.getSessionPacket(
				ServerPacketType.EDITOR_MAP_DATA, Priority.MEDIUM,
				session, out.getBytes()));
	}

}
