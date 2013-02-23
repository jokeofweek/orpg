package orpg.server;

import java.io.IOException;

import orpg.server.config.ServerConfigurationManager;
import orpg.server.console.BaseServerConsole;
import orpg.shared.Constants;

public class ServerApplication {

	public static void main(String... args) throws InterruptedException {
		// Setup the console
		BaseServerConsole console = new BaseServerConsole();
		console.out().println("Starting server...");

		// Set up the configuration
		String[] propertiesFiles = new String[] { Constants.SERVER_DATA_PATH
				+ "server.properties" };
		
		ServerConfigurationManager config = null;
		console.out().println("Loading configuration...");
		try {
			config = new ServerConfigurationManager(propertiesFiles, console);
		} catch (IOException e) {
			console.out().println(e.getMessage());
			console.out().println("Shutting down...");
			System.exit(1);
		}

		new BaseServer(config, console);
		while (true) {
			Thread.sleep(1000000);
		}
	}

}
