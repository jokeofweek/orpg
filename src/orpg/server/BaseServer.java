package orpg.server;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.PlayerManager;

import orpg.server.config.ServerConfigurationManager;
import orpg.server.console.ServerConsole;
import orpg.server.data.ServerReceivedPacket;
import orpg.server.data.components.SystemEventProcessor;
import orpg.server.data.controllers.AccountController;
import orpg.server.data.controllers.AutoTileController;
import orpg.server.data.controllers.ChatController;
import orpg.server.data.controllers.MapController;
import orpg.server.data.entity.EntityFactory;
import orpg.server.data.entity.EntityPreprocessor;
import orpg.server.data.store.DataStore;
import orpg.server.data.store.FileDataStore;
import orpg.server.net.packets.ServerPacket;
import orpg.server.systems.MapProcessSystem;
import orpg.server.systems.MovementSystem;

public class BaseServer {

	private ServerConfigurationManager config;
	private ServerSessionManager serverSessionManager;
	private ServerGameThread serverGameThread;
	private ServerSocketThread serverSocketThread;
	private ServerConsole console;
	private BlockingQueue<ServerReceivedPacket> inputQueue;
	private BlockingQueue<ServerPacket> outputQueue;
	private MapController mapController;
	private AccountController accountController;
	private AutoTileController autoTileController;
	private ChatController chatController;
	private DataStore dataStore;

	// World and entity datas
	private ServerWorldThread serverWorldThread;
	private World world;
	private EntityFactory entityFactory;

	public BaseServer(ServerConfigurationManager config, ServerConsole console) {
		this.config = config;
		this.console = console;

		boolean encounteredSetupProblems = false;

		// Set up our queues
		this.inputQueue = new LinkedBlockingQueue<ServerReceivedPacket>();
		this.outputQueue = new LinkedBlockingQueue<ServerPacket>();

		// Initialize the world
		console.out().println("Creating world...");
		this.world = new World();
		world.setManager(new GroupManager());
		world.setManager(new PlayerManager());
		world.setManager(new EntityPreprocessor());
		world.setSystem(new MapProcessSystem(this, 5000));
		world.setSystem(new MovementSystem(this));
		world.initialize();
		this.entityFactory = new EntityFactory(this, this.world);
		this.serverWorldThread = new ServerWorldThread(this);

		// Create an event processor entity to be used by the aspects
		Entity entity = world.createEntity();
		entity.addComponent(new SystemEventProcessor());
		world.addEntity(entity);

		// Set up the various threads
		this.serverSessionManager = new ServerSessionManager(this, outputQueue);
		this.serverGameThread = new ServerGameThread(this, inputQueue,
				outputQueue);
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
		console.out().println("Setting up data store...");
		if (!encounteredSetupProblems) {
			this.dataStore = new FileDataStore(this);
			encounteredSetupProblems = !this.dataStore.setup();

			if (!encounteredSetupProblems) {
				encounteredSetupProblems = !setupControllers(dataStore);
			}
		}

		// Close if any setup problems
		if (encounteredSetupProblems) {
			if (this.dataStore != null) {
				this.dataStore.shutdown();
			}
			console.out().println(
					"Encountered setup problems. Shutting down...");
			System.exit(1);
		}

		// Now that everything is setup, we are ready to go.
		console.out().println("Starting threads...");
		new Thread(serverWorldThread).start();
		new Thread(serverSessionManager).start();
		new Thread(serverGameThread).start();
		new Thread(serverSocketThread).start();
		console.out().println("Ready to go!");
	}

	public ServerConfigurationManager getConfigManager() {
		return config;
	}

	public ServerConsole getConsole() {
		return console;
	}

	public void sendPacket(ServerPacket packet) {
		outputQueue.add(packet);
	}

	public void receivePacket(ServerReceivedPacket packet) {
		inputQueue.add(packet);
	}

	public ServerSessionManager getServerSessionManager() {
		return serverSessionManager;
	}

	public MapController getMapController() {
		return mapController;
	}

	public AccountController getAccountController() {
		return accountController;
	}

	public AutoTileController getAutoTileController() {
		return autoTileController;
	}

	public ChatController getChatController() {
		return chatController;
	}

	public World getWorld() {
		return world;
	}

	public EntityFactory getEntityFactory() {
		return entityFactory;
	}

	/**
	 * @return true is the loading was successful, else false
	 */
	private boolean setupControllers(DataStore dataStore) {
		console.out().println("Setting up account controller...");
		this.accountController = new AccountController(this, dataStore);
		if (!accountController.setup()) {
			return false;
		}

		console.out().println("Setting up map controller...");
		this.mapController = new MapController(this, dataStore);
		if (!this.mapController.setup()) {
			return false;
		}

		console.out().println("Setting up autotiles...");
		this.autoTileController = new AutoTileController(this, dataStore);
		if (!this.autoTileController.setup()) {
			return false;
		}

		console.out().println("Setting up chat...");
		this.chatController = new ChatController(this);

		return true;
	}

}
