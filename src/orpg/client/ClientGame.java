package orpg.client;

import java.net.Socket;

import orpg.client.net.ClientProcessThread;
import orpg.client.state.ClientStateManager;
import orpg.client.state.MainMenuState;
import orpg.shared.net.AbstractClient;
import orpg.shared.state.StateManager;

import com.badlogic.gdx.Game;

public class ClientGame extends Game {

	private AbstractClient baseClient;

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
		this.baseClient = new AbstractClient(s, new ClientProcessThread(),
				stateManager);
		stateManager.pushState(new MainMenuState(this, baseClient));

	}

}
