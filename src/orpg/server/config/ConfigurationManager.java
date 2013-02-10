package orpg.server.config;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.badlogic.gdx.files.FileHandle;

import orpg.server.console.ServerConsole;
import orpg.shared.Constants;

public class ConfigurationManager {

	private Properties properties;
	private Logger sessionLogger;

	// Constant property names
	private static final String SERVER_PORT = "Server.Port";

	public ConfigurationManager(String[] configurationFiles,
			ServerConsole console) {
		// Load the properties from the properties file.
		Properties properties = new Properties();
		FileReader in = null;
		for (String file : configurationFiles) {
			try {
				in = new FileReader(file);
				properties.load(in);
			} catch (IOException io) {
				console.out().println(
						"Error reading properties file '" + file
								+ "'. Error message: " + io.getMessage());
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

		this.sessionLogger = Logger.getLogger("session");
		this.sessionLogger.setUseParentHandlers(false);
		try {
			sessionLogger.addHandler(new FileHandler(Constants.SERVER_LOGS_PATH
					+ "session.log"));

			// Add the console handler if it exists
			Handler consoleHandler = console.getHandler();
			if (consoleHandler != null) {
				sessionLogger.addHandler(consoleHandler);
			}
		} catch (IOException e) {
			console.out().println(
					"Error creationg session log file. Error message: "
							+ e.getMessage());
		}
	}

	public int getServerPort() {
		return Integer.parseInt(properties.getProperty(SERVER_PORT));
	}

	public Logger getSessionLogger() {
		return sessionLogger;
	}
}
