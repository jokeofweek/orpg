package orpg.server;

import orpg.server.config.ConfigurationManager;
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
		ConfigurationManager config = new ConfigurationManager(
				propertiesFiles, console);
		console.out().println("Configuration manager setup...");

		new BaseServer(config, console);
		while (true) {
			Thread.sleep(1000000);
		}
	}

}
