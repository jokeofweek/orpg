package orpg.server;


import orpg.server.config.ConfigurationManager;
import orpg.server.console.BaseServerConsole;

public class ServerApplication {

	public static void main(String... args) {
		// Setup the console
		BaseServerConsole console = new BaseServerConsole();
		console.out().println("Starting server...");
		
		// Set up the configuration
		String[] propertiesFiles = new String[] { "config/server.properties" };
		ConfigurationManager config = new ConfigurationManager(propertiesFiles, console);
		console.out().println("Configuration manager setup...");
		
		new BaseServer(config, console);
		while (true);
	}

}
