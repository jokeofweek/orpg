package orpg.client;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class ClientApplication {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new LwjglApplication(new ClientGame(), "Game", 800, 600, false);
	}

}
