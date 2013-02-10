package orpg.server.config;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

import orpg.server.console.ServerConsole;
import orpg.shared.Constants;

public class ConfigurationManager {

	private Properties properties;
	private Logger sessionLogger;
	private Logger errorLogger;

	private int serverPort;
	private int totalMaps;

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

		this.errorLogger = Logger.getLogger("error");
		this.errorLogger.setUseParentHandlers(false);
		try {
			errorLogger.addHandler(new FileHandler(Constants.SERVER_LOGS_PATH
					+ "error.log"));

			// Add the console handler if it exists
			Handler consoleHandler = console.getHandler();
			if (consoleHandler != null) {
				errorLogger.addHandler(consoleHandler);
			}
		} catch (IOException e) {
			console.out().println(
					"Error creationg error log file. Error message: "
							+ e.getMessage());
		}

		// Load the properties
		loadProperties();
	}

	public Logger getSessionLogger() {
		return sessionLogger;
	}

	public Logger getErrorLogger() {
		return errorLogger;
	}

	private void loadProperties() {
		this.serverPort = getIntProperty("Server.Port", 8000);
		this.totalMaps = getIntProperty("Data.Maps", 100);
	}

	private String getStringProperty(String key, String defaultValue) {
		String property = properties.getProperty(key);
		if (property == null) {
			return defaultValue;
		} else {
			return property;
		}
	}

	private int getIntProperty(String key, int defaultValue) {
		String property = properties.getProperty(key);
		if (property == null) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(property);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	public int getServerPort() {
		return serverPort;
	}

	public int getTotalMaps() {
		return totalMaps;
	}

}
