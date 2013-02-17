package orpg.client;


import orpg.shared.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public final class Paths {

	private Paths() {}
	
	public static FileHandle asset(String path) {
		return Gdx.files.internal(Constants.CLIENT_ASSETS_PATH + path);
	}
	
	
}
