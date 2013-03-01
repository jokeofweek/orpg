package orpg.client.config;

import java.io.IOException;

import orpg.shared.config.ConfigurationManager;

public class ClientConfigurationManager extends ConfigurationManager {

	private String serverAddress;
	private int serverPort;

	public ClientConfigurationManager(String[] configurationFiles)
			throws IOException {
		super(configurationFiles);
	}

	@Override
	protected void loadProperties() {
		this.serverAddress = getStringProperty("Server.Address", "localhost");
		this.serverPort = getIntProperty("Server.Port", 8000);
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public int getServerPort() {
		return serverPort;
	}
}
