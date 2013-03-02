package orpg.client;

import java.io.IOException;
import java.net.Socket;

import javax.swing.JOptionPane;

import orpg.client.config.ClientConfigurationManager;
import orpg.client.net.ClientProcessThread;
import orpg.client.state.ClientStateManager;
import orpg.client.state.MainMenuState;
import orpg.shared.Constants;
import orpg.shared.state.StateManager;

import com.badlogic.gdx.Game;

public class ClientGame extends Game {

	private BaseClient baseClient;

	@Override
	public void create() {
		// Load up the configuration manager
		ClientConfigurationManager config = null;
		try {
			config = new ClientConfigurationManager(
					new String[] { Constants.CLIENT_DATA_PATH
							+ "client.properties" });
		} catch (IOException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Error loading client.properties!");
		}

		Socket s = null;
		try {
			s = new Socket(config.getServerAddress(), config.getServerPort());
		} catch (Exception e) {
			System.out.println("Could not create socket.");
			e.printStackTrace();
			System.exit(1);
		}

		// Set up the queue of actions to perfor
		StateManager stateManager = new ClientStateManager(this);
		this.baseClient = new BaseClient(this, s, new ClientProcessThread(),
				stateManager, config);
		stateManager.pushState(new MainMenuState(baseClient));

	}

}
