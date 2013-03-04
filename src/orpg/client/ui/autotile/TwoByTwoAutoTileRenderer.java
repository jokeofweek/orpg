package orpg.client.ui.autotile;

import java.util.HashMap;

import orpg.editor.ui.autotile.TwoByThreeAutoTileRenderer;
import orpg.shared.Constants;
import orpg.shared.data.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TwoByTwoAutoTileRenderer implements AutoTileRenderer {

	private static final int TOP_LEFT = 1;
	private static final int TOP_MIDDLE = 2;
	private static final int TOP_RIGHT = 4;
	private static final int MIDDLE_LEFT = 8;
	private static final int MIDDLE_RIGHT = 16;
	private static final int BOTTOM_LEFT = 32;
	private static final int BOTTOM_MIDDLE = 64;
	private static final int BOTTOM_RIGHT = 128;

	private static final int AUTOTILESET_WIDTH = 4; // In autotiles

	private static final HashMap<Integer, Byte> autotileTopLeftCorner;
	private static final HashMap<Integer, Byte> autotileTopRightCorner;
	private static final HashMap<Integer, Byte> autotileBottomLeftCorner;
	private static final HashMap<Integer, Byte> autotileBottomRightCorner;

	static {
		autotileTopLeftCorner = new HashMap<Integer, Byte>(16);
		autotileTopLeftCorner.put(0, (byte) 0);
		autotileTopLeftCorner.put(TOP_MIDDLE, (byte) 8);
		autotileTopLeftCorner.put(MIDDLE_LEFT, (byte) 2);
		autotileTopLeftCorner.put(BOTTOM_MIDDLE, (byte) 0);
		autotileTopLeftCorner.put(MIDDLE_RIGHT, (byte) 0);
		autotileTopLeftCorner.put(TOP_MIDDLE | MIDDLE_LEFT, (byte) 10);
		autotileTopLeftCorner.put(TOP_MIDDLE | BOTTOM_MIDDLE, (byte) 4);
		autotileTopLeftCorner.put(TOP_MIDDLE | MIDDLE_RIGHT, (byte) 8);
		autotileTopLeftCorner.put(MIDDLE_LEFT | BOTTOM_MIDDLE, (byte) 2);
		autotileTopLeftCorner.put(MIDDLE_LEFT | MIDDLE_RIGHT, (byte) 1);
		autotileTopLeftCorner.put(MIDDLE_RIGHT | BOTTOM_MIDDLE, (byte) 0);
		autotileTopLeftCorner.put(TOP_MIDDLE | MIDDLE_RIGHT
				| BOTTOM_MIDDLE, (byte) 4);
		autotileTopLeftCorner.put(TOP_MIDDLE | MIDDLE_RIGHT | MIDDLE_LEFT,
				(byte) 9);
		autotileTopLeftCorner.put(MIDDLE_RIGHT | BOTTOM_MIDDLE
				| MIDDLE_LEFT, (byte) 1);
		autotileTopLeftCorner.put(
				TOP_MIDDLE | BOTTOM_MIDDLE | MIDDLE_LEFT, (byte) 6);
		autotileTopLeftCorner.put(TOP_MIDDLE | MIDDLE_RIGHT
				| BOTTOM_MIDDLE | MIDDLE_LEFT, (byte) 5);

		autotileTopRightCorner = new HashMap<Integer, Byte>(16);
		autotileTopRightCorner.put(0, (byte) 3);
		autotileTopRightCorner.put(TOP_MIDDLE, (byte) 11);
		autotileTopRightCorner.put(MIDDLE_LEFT, (byte) 3);
		autotileTopRightCorner.put(BOTTOM_MIDDLE, (byte) 3);
		autotileTopRightCorner.put(MIDDLE_RIGHT, (byte) 1);
		autotileTopRightCorner.put(TOP_MIDDLE | MIDDLE_LEFT, (byte) 11);
		autotileTopRightCorner.put(TOP_MIDDLE | BOTTOM_MIDDLE, (byte) 7);
		autotileTopRightCorner.put(TOP_MIDDLE | MIDDLE_RIGHT, (byte) 9);
		autotileTopRightCorner.put(MIDDLE_LEFT | BOTTOM_MIDDLE, (byte) 3);
		autotileTopRightCorner.put(MIDDLE_LEFT | MIDDLE_RIGHT, (byte) 2);
		autotileTopRightCorner.put(MIDDLE_RIGHT | BOTTOM_MIDDLE, (byte) 1);
		autotileTopRightCorner.put(TOP_MIDDLE | MIDDLE_RIGHT
				| BOTTOM_MIDDLE, (byte) 5);
		autotileTopRightCorner.put(
				TOP_MIDDLE | MIDDLE_RIGHT | MIDDLE_LEFT, (byte) 10);
		autotileTopRightCorner.put(MIDDLE_RIGHT | BOTTOM_MIDDLE
				| MIDDLE_LEFT, (byte) 2);
		autotileTopRightCorner.put(TOP_MIDDLE | BOTTOM_MIDDLE
				| MIDDLE_LEFT, (byte) 7);
		autotileTopRightCorner.put(TOP_MIDDLE | MIDDLE_RIGHT
				| BOTTOM_MIDDLE | MIDDLE_LEFT, (byte) 6);

		autotileBottomLeftCorner = new HashMap<Integer, Byte>(16);
		autotileBottomLeftCorner.put(0, (byte) 12);
		autotileBottomLeftCorner.put(TOP_MIDDLE, (byte) 12);
		autotileBottomLeftCorner.put(MIDDLE_LEFT, (byte) 14);
		autotileBottomLeftCorner.put(BOTTOM_MIDDLE, (byte) 4);
		autotileBottomLeftCorner.put(MIDDLE_RIGHT, (byte) 12);
		autotileBottomLeftCorner.put(TOP_MIDDLE | MIDDLE_LEFT, (byte) 14);
		autotileBottomLeftCorner.put(TOP_MIDDLE | BOTTOM_MIDDLE, (byte) 8);
		autotileBottomLeftCorner.put(TOP_MIDDLE | MIDDLE_RIGHT, (byte) 12);
		autotileBottomLeftCorner
				.put(MIDDLE_LEFT | BOTTOM_MIDDLE, (byte) 6);
		autotileBottomLeftCorner
				.put(MIDDLE_LEFT | MIDDLE_RIGHT, (byte) 13);
		autotileBottomLeftCorner.put(MIDDLE_RIGHT | BOTTOM_MIDDLE,
				(byte) 4);
		autotileBottomLeftCorner.put(TOP_MIDDLE | MIDDLE_RIGHT
				| BOTTOM_MIDDLE, (byte) 8);
		autotileBottomLeftCorner.put(TOP_MIDDLE | MIDDLE_RIGHT
				| MIDDLE_LEFT, (byte) 13);
		autotileBottomLeftCorner.put(MIDDLE_RIGHT | BOTTOM_MIDDLE
				| MIDDLE_LEFT, (byte) 5);
		autotileBottomLeftCorner.put(TOP_MIDDLE | BOTTOM_MIDDLE
				| MIDDLE_LEFT, (byte) 10);
		autotileBottomLeftCorner.put(TOP_MIDDLE | MIDDLE_RIGHT
				| BOTTOM_MIDDLE | MIDDLE_LEFT, (byte) 9);

		autotileBottomRightCorner = new HashMap<Integer, Byte>(16);
		autotileBottomRightCorner.put(0, (byte) 15);
		autotileBottomRightCorner.put(TOP_MIDDLE, (byte) 15);
		autotileBottomRightCorner.put(MIDDLE_LEFT, (byte) 15);
		autotileBottomRightCorner.put(BOTTOM_MIDDLE, (byte) 7);
		autotileBottomRightCorner.put(MIDDLE_RIGHT, (byte) 13);
		autotileBottomRightCorner.put(TOP_MIDDLE | MIDDLE_LEFT, (byte) 15);
		autotileBottomRightCorner.put(TOP_MIDDLE | BOTTOM_MIDDLE,
				(byte) 11);
		autotileBottomRightCorner
				.put(TOP_MIDDLE | MIDDLE_RIGHT, (byte) 13);
		autotileBottomRightCorner.put(MIDDLE_LEFT | BOTTOM_MIDDLE,
				(byte) 7);
		autotileBottomRightCorner.put(MIDDLE_LEFT | MIDDLE_RIGHT,
				(byte) 14);
		autotileBottomRightCorner.put(MIDDLE_RIGHT | BOTTOM_MIDDLE,
				(byte) 5);
		autotileBottomRightCorner.put(TOP_MIDDLE | MIDDLE_RIGHT
				| BOTTOM_MIDDLE, (byte) 9);
		autotileBottomRightCorner.put(TOP_MIDDLE | MIDDLE_RIGHT
				| MIDDLE_LEFT, (byte) 14);
		autotileBottomRightCorner.put(MIDDLE_RIGHT | BOTTOM_MIDDLE
				| MIDDLE_LEFT, (byte) 6);
		autotileBottomRightCorner.put(TOP_MIDDLE | BOTTOM_MIDDLE
				| MIDDLE_LEFT, (byte) 11);
		autotileBottomRightCorner.put(TOP_MIDDLE | MIDDLE_RIGHT
				| BOTTOM_MIDDLE | MIDDLE_LEFT, (byte) 10);

	}
	private static TwoByTwoAutoTileRenderer instance = new TwoByTwoAutoTileRenderer();

	public static TwoByTwoAutoTileRenderer getInstance() {
		return instance;
	}

	private TwoByTwoAutoTileRenderer() {
	}

	@Override
	public int getCacheValue(int x, int y, int z, Map map) {
		int matchingTiles = 0;
		short tile = map.getTile(x, y, z);
		boolean xLeftBound = x == 0;
		boolean xRightBound = x == map.getWidth() - 1;
		boolean yUpBound = y == 0;
		boolean yDownBound = y == map.getHeight() - 1;

		if (xLeftBound || map.getTile(x - 1, y, z) == tile)
			matchingTiles |= MIDDLE_LEFT;
		if (xRightBound || map.getTile(x + 1, y, z) == tile)
			matchingTiles |= MIDDLE_RIGHT;

		if (yUpBound || map.getTile(x, y - 1, z) == tile)
			matchingTiles |= TOP_MIDDLE;
		if (yDownBound || map.getTile(x, y + 1, z) == tile)
			matchingTiles |= BOTTOM_MIDDLE;

		if (xLeftBound || yUpBound || map.getTile(x - 1, y - 1, z) == tile) {
			matchingTiles |= TOP_LEFT;
		}
		if (xLeftBound || yDownBound
				|| map.getTile(x - 1, y + 1, z) == tile) {
			matchingTiles |= BOTTOM_LEFT;
		}
		if (xRightBound || yUpBound
				|| map.getTile(x + 1, y - 1, z) == tile) {
			matchingTiles |= TOP_RIGHT;
		}
		if (xRightBound || yDownBound
				|| map.getTile(x + 1, y + 1, z) == tile) {
			matchingTiles |= BOTTOM_RIGHT;
		}

		// Every byte in the cache value represents the position within the
		// autotileset.

		// top-left corner
		int cacheValue = autotileTopLeftCorner
				.get(matchingTiles
						& (TOP_MIDDLE | BOTTOM_MIDDLE | MIDDLE_LEFT | MIDDLE_RIGHT));

		// top-right corner
		cacheValue = (cacheValue << 8)
				| autotileTopRightCorner
						.get(matchingTiles
								& (TOP_MIDDLE | BOTTOM_MIDDLE
										| MIDDLE_LEFT | MIDDLE_RIGHT));

		// bottom-left corner
		cacheValue = (cacheValue << 8)
				| autotileBottomLeftCorner
						.get(matchingTiles
								& (TOP_MIDDLE | BOTTOM_MIDDLE
										| MIDDLE_LEFT | MIDDLE_RIGHT));

		// bottom-right corner
		cacheValue = (cacheValue << 8)
				| autotileBottomRightCorner
						.get(matchingTiles
								& (TOP_MIDDLE | BOTTOM_MIDDLE
										| MIDDLE_LEFT | MIDDLE_RIGHT));

		return cacheValue;
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha, int x, int y,
			int z, int tile, int cacheValue, int xRenderPos, int dY, Map map,
			Texture[] tilesets) {

		int matchingTiles = 0;
		int convertedTile = ((tile / Constants.TILESET_WIDTH) * (2 * Constants.TILESET_WIDTH_IN_AUTOTILES))
				+ ((tile % Constants.TILESET_WIDTH) * 2);

		// Bottom right corner
		int corner = cacheValue & 0xff;
		cacheValue >>= 8;
		int cornerTile = convertedTile
				+ ((corner / (AUTOTILESET_WIDTH)) * Constants.TILESET_WIDTH_IN_AUTOTILES)
				+ (corner % AUTOTILESET_WIDTH);

		batch.draw(
				tilesets[cornerTile / Constants.AUTOTILES_PER_TILESET],
				xRenderPos + Constants.AUTOTILE_WIDTH,
				dY + Constants.AUTOTILE_HEIGHT,
				Constants.AUTOTILE_WIDTH,
				Constants.AUTOTILE_HEIGHT,
				(cornerTile % Constants.TILESET_WIDTH_IN_AUTOTILES)
						* Constants.AUTOTILE_WIDTH,
				((cornerTile / Constants.TILESET_HEIGHT_IN_AUTOTILES) % Constants.TILESET_HEIGHT_IN_AUTOTILES)
						* Constants.AUTOTILE_HEIGHT,
				Constants.AUTOTILE_WIDTH, Constants.AUTOTILE_HEIGHT,
				false, true);
		

		// Bottom left corner
		corner = corner = cacheValue & 0xff;
		cacheValue >>= 8;
		cornerTile = convertedTile
				+ ((corner / (AUTOTILESET_WIDTH)) * Constants.TILESET_WIDTH_IN_AUTOTILES)
				+ (corner % AUTOTILESET_WIDTH);

		batch.draw(
				tilesets[cornerTile / Constants.AUTOTILES_PER_TILESET],
				xRenderPos,
				dY + Constants.AUTOTILE_WIDTH,
				Constants.AUTOTILE_WIDTH,
				Constants.AUTOTILE_HEIGHT,
				(cornerTile % Constants.TILESET_WIDTH_IN_AUTOTILES)
						* Constants.AUTOTILE_WIDTH,
				((cornerTile / Constants.TILESET_HEIGHT_IN_AUTOTILES) % Constants.TILESET_HEIGHT_IN_AUTOTILES)
						* Constants.AUTOTILE_HEIGHT,
				Constants.AUTOTILE_WIDTH, Constants.AUTOTILE_HEIGHT,
				false, true);

		// Top right corner
		corner = corner = cacheValue & 0xff;
		cacheValue >>= 8;
		cornerTile = convertedTile
				+ ((corner / (AUTOTILESET_WIDTH)) * Constants.TILESET_WIDTH_IN_AUTOTILES)
				+ (corner % AUTOTILESET_WIDTH);

		batch.draw(
				tilesets[cornerTile / Constants.AUTOTILES_PER_TILESET],
				xRenderPos + Constants.AUTOTILE_WIDTH,
				dY,
				Constants.AUTOTILE_WIDTH,
				Constants.AUTOTILE_HEIGHT,
				(cornerTile % Constants.TILESET_WIDTH_IN_AUTOTILES)
						* Constants.AUTOTILE_WIDTH,
				((cornerTile / Constants.TILESET_HEIGHT_IN_AUTOTILES) % Constants.TILESET_HEIGHT_IN_AUTOTILES)
						* Constants.AUTOTILE_HEIGHT,
				Constants.AUTOTILE_WIDTH, Constants.AUTOTILE_HEIGHT,
				false, true);

		// Top left corner
		corner = corner = cacheValue & 0xff;
		cacheValue >>= 8;
		cornerTile = convertedTile
				+ ((corner / (AUTOTILESET_WIDTH)) * Constants.TILESET_WIDTH_IN_AUTOTILES)
				+ (corner % AUTOTILESET_WIDTH);

		batch.draw(
				tilesets[cornerTile / Constants.AUTOTILES_PER_TILESET],
				xRenderPos,
				dY,
				Constants.AUTOTILE_WIDTH,
				Constants.AUTOTILE_HEIGHT,
				(cornerTile % Constants.TILESET_WIDTH_IN_AUTOTILES)
						* Constants.AUTOTILE_WIDTH,
				((cornerTile / Constants.TILESET_HEIGHT_IN_AUTOTILES) % Constants.TILESET_HEIGHT_IN_AUTOTILES)
						* Constants.AUTOTILE_HEIGHT,
				Constants.AUTOTILE_WIDTH, Constants.AUTOTILE_HEIGHT,
				false, true);


	}

}
