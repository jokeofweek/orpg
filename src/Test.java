import com.badlogic.gdx.backends.lwjgl.LwjglApplication;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new LwjglApplication(new TiledMapTest(), "Game", 512, 512, true);

	}

}
