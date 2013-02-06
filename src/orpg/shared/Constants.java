package orpg.shared;

import java.nio.charset.Charset;

public final class Constants {

	public static final int TILE_WIDTH = 32;
	public static final int TILE_HEIGHT = 32;
	public static final int TILESET_WIDTH = 8;
	
	public static final Charset CHARSET = Charset.forName("UTF-8");

	public static final float MAP_EDITOR_GRID_TRANSPARENCY = 0.60f;
	public static final float MAP_EDITOR_TILE_OVERLAY_TRANSPARENCY = 0.60f;
	
	private Constants() {};
	
}
