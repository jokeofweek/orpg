package orpg.server.config;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import orpg.server.console.ServerConsole;
import orpg.shared.Constants;
import orpg.shared.config.ConfigurationManager;

public class ServerConfigurationManager extends ConfigurationManager {

	private Logger sessionLogger;
	private Logger errorLogger;

	private int serverPort;
	private int totalMaps;

	public ServerConfigurationManager(String[] configurationFiles,
			ServerConsole console) throws IOException {
		super(configurationFiles);

		FileHandler fileHandler;
		this.sessionLogger = Logger.getLogger("session");
		this.sessionLogger.setUseParentHandlers(false);
		try {

			fileHandler = new FileHandler(Constants.SERVER_LOGS_PATH
					+ "session.log");
			fileHandler.setFormatter(new SimpleFormatter());
			sessionLogger.addHandler(fileHandler);

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
			fileHandler = new FileHandler(Constants.SERVER_LOGS_PATH
					+ "error.log");
			fileHandler.setFormatter(new SimpleFormatter());
			errorLogger.addHandler(fileHandler);

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
	}

	public Logger getSessionLogger() {
		return sessionLogger;
	}

	public Logger getErrorLogger() {
		return errorLogger;
	}

	protected void loadProperties() {
		this.serverPort = getIntProperty("Server.Port", 8000);
		this.totalMaps = getIntProperty("Data.Maps", 100);
	}

	public int getServerPort() {
		return serverPort;
	}

	public int getTotalMaps() {
		return totalMaps;
	}

}
