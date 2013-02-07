package orpg.shared;

import java.nio.charset.Charset;

public final class Constants {

	public static final int TILE_WIDTH = 32;
	public static final int TILE_HEIGHT = 32;
	public static final int TILESET_WIDTH = 8;
	
	public static final Charset CHARSET = Charset.forName("UTF-8");

	// Map editor options
	public static final float MAP_EDITOR_GRID_TRANSPARENCY = 0.60f;
	public static final float MAP_EDITOR_TILE_OVERLAY_TRANSPARENCY = 0.60f;
	
	// Data path options
	public static final String CLIENT_DATA_PATH = "data/client/";
	public static final String SERVER_DATA_PATH = "data/server/";
	
	// Map options
	public static final int MAP_SEGMENT_WIDTH = 60;
	public static final int MAP_SEGMENT_HEIGHT = 60;
	
	private Constants() {};
	
}
