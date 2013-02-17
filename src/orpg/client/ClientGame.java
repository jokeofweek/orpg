package orpg.client;

import java.net.Socket;

import orpg.client.net.BaseClient;
import orpg.client.net.ClientProcessThread;
import orpg.client.screen.MainMenuScreen;

import com.badlogic.gdx.Game;

public class ClientGame extends Game {

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
		BaseClient baseClient = new BaseClient(s, new ClientProcessThread());
		MainMenuScreen mainMenu = new MainMenuScreen(this, baseClient);
		setScreen(mainMenu);
	}

}
