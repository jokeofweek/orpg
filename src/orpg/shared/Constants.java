package orpg.shared;

import java.nio.charset.Charset;

import orpg.shared.data.Direction;

public final class Constants {

	public static final Charset CHARSET = Charset.forName("UTF-8");

	// Map editor options
	public static final float MAP_EDITOR_ATTRIBUTE_TRANSPARENCY = 0.40f;
	public static final float MAP_EDITOR_GRID_TRANSPARENCY = 0.60f;
	public static final float MAP_EDITOR_TILE_OVERLAY_TRANSPARENCY = 0.60f;

	// Data path options
	public static final String CLIENT_DATA_PATH = "data/client/";
	public static final String CLIENT_ASSETS_PATH = CLIENT_DATA_PATH + "gfx/";
	public static final String EDITOR_DATA_PATH = "data/editor/";
	public static final String SERVER_DATA_PATH = "data/server/";
	public static final String SERVER_MAPS_PATH = SERVER_DATA_PATH + "maps/";
	public static final String SERVER_ACCOUNTS_PATH = SERVER_DATA_PATH
			+ "accounts/";
	public static final String SERVER_LOGS_PATH = SERVER_DATA_PATH + "logs/";

	// Map options
	public static final short MAP_SEGMENT_WIDTH = 60;
	public static final short MAP_SEGMENT_HEIGHT = 60;

	// Tileset options
	public static final int TILE_WIDTH = 32;
	public static final int TILE_HEIGHT = 32;
	public static final int TILESET_WIDTH = 8;
	public static final int TILESET_HEIGHT = 8;
	public static final int TILES_PER_TILESET = TILESET_WIDTH * TILESET_HEIGHT;
	public static final int TILESETS = 28;
	
	public static final int AUTOTILE_WIDTH = TILE_WIDTH / 2;
	public static final int AUTOTILE_HEIGHT = TILE_HEIGHT / 2;
	public static final int AUTOTILESET_WIDTH = 4; // In autotiles
	public static final int TILESET_WIDTH_IN_AUTOTILES = TILESET_WIDTH * 2;
	public static final int TILESET_HEIGHT_IN_AUTOTILES = TILESET_HEIGHT * 2;
	public static final int AUTOTILES_PER_TILESET = TILESET_WIDTH_IN_AUTOTILES * TILESET_HEIGHT_IN_AUTOTILES;

	// Sprite options
	public static final int SPRITESETS = 10;
	public static final int SPRITESET_WIDTH = 2;
	public static final int SPRITESET_HEIGHT = 2;
	public static final int SPRITES_PER_SPRITESET = SPRITESET_WIDTH
			* SPRITESET_HEIGHT;
	public static final int SPRITE_FRAME_WIDTH = 32;
	public static final int SPRITE_FRAME_HEIGHT = 32;
	public static final int SPRITE_FRAMES = 4;
	public static final int SPRITE_WIDTH = SPRITE_FRAME_WIDTH * SPRITE_FRAMES;
	public static final int SPRITE_HEIGHT = SPRITE_FRAME_HEIGHT
			* Direction.values().length;
	public static final int SPRITE_FRAME_OFFSET = Constants.TILE_WIDTH
			/ Constants.SPRITE_FRAMES;

	private Constants() {
	};

}
