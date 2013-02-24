package orpg.client;

import java.net.Socket;

import orpg.client.net.ClientProcessThread;
import orpg.client.state.ClientStateManager;
import orpg.client.state.MainMenuState;
import orpg.shared.net.AbstractClient;
import orpg.shared.state.StateManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;

public class ClientGame extends Game {

	private BaseClient baseClient;

	@Override
	public void create() {
		Socket s = null;
		try {
			s = new Socket("localhost", 8000);
		} catch (Exception e) {
			System.out.println("Could not create socket.");
			e.printStackTrace();
			System.exit(1);
		}

		// Set up the queue of actions to perfor
		StateManager stateManager = new ClientStateManager(this);
		this.baseClient = new BaseClient(this, s, new ClientProcessThread(),
				stateManager);
		stateManager.pushState(new MainMenuState(baseClient));

	}
	
	

}
