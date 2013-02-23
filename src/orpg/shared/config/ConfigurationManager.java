package orpg.shared.config;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import orpg.server.console.ServerConsole;
import orpg.shared.Constants;

public abstract class ConfigurationManager {

	private Properties properties;

	public ConfigurationManager(String[] configurationFiles) throws IOException {
		// Load the properties from the properties file.
		Properties properties = new Properties();
		FileReader in = null;
		for (String file : configurationFiles) {
			try {
				in = new FileReader(file);
				properties.load(in);
			} catch (IOException io) {
				throw new IOException("Error reading properties file '" + file
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

		// Load the properties
		loadProperties();
	}

	protected abstract void loadProperties();

	protected final String getStringProperty(String key, String defaultValue) {
		String property = properties.getProperty(key);
		if (property == null) {
			return defaultValue;
		} else {
			return property;
		}
	}

	protected final int getIntProperty(String key, int defaultValue) {
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

}
