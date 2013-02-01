package orpg.server.config;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import orpg.server.console.ServerConsole;

public class ConfigurationManager {

	private Properties properties;
	
	// Constant property names
	private static final String SERVER_PORT = "Server.Port";

	public ConfigurationManager(String[] configurationFiles, ServerConsole console) {
		// Load the properties from the properties file.
		Properties properties = new Properties();
		FileReader in = null;
		for (String file : configurationFiles) {
			try {
				in = new FileReader(file);
				properties.load(in);
			} catch (IOException io) {
				console.out().println("Error reading properties file '"
						+ file + "'. Error message: " + io.getMessage());
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
					}
				}
			}
		}
		
		this.properties = properties;
	}

	public int getServerPort() {
		return Integer.parseInt(properties.getProperty(SERVER_PORT));
	}
	
}
