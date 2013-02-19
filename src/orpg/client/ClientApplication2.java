package orpg.client;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class ClientApplication2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new LwjglApplication(new ClientGame(), "Game", 800, 600, false);
	}

}
