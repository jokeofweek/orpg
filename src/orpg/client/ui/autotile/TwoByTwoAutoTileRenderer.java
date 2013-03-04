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

	private static final HashMap<Integer, Integer> autotileTopLeftCorner;
	private static final HashMap<Integer, Integer> autotileTopRightCorner;
	private static final HashMap<Integer, Integer> autotileBottomLeftCorner;
	private static final HashMap<Integer, Integer> autotileBottomRightCorner;

	static {
		autotileTopLeftCorner = new HashMap<Integer, Integer>(16);
		autotileTopLeftCorner.put(0, 0);
		autotileTopLeftCorner.put(TOP_MIDDLE, 8);
		autotileTopLeftCorner.put(MIDDLE_LEFT, 2);
		autotileTopLeftCorner.put(BOTTOM_MIDDLE, 0);
		autotileTopLeftCorner.put(MIDDLE_RIGHT, 0);
		autotileTopLeftCorner.put(TOP_MIDDLE | MIDDLE_LEFT, 10);
		autotileTopLeftCorner.put(TOP_MIDDLE | BOTTOM_MIDDLE, 4);
		autotileTopLeftCorner.put(TOP_MIDDLE | MIDDLE_RIGHT, 8);
		autotileTopLeftCorner.put(MIDDLE_LEFT | BOTTOM_MIDDLE, 2);
		autotileTopLeftCorner.put(MIDDLE_LEFT | MIDDLE_RIGHT, 1);
		autotileTopLeftCorner.put(MIDDLE_RIGHT | BOTTOM_MIDDLE, 0);
		autotileTopLeftCorner.put(TOP_MIDDLE | MIDDLE_RIGHT | BOTTOM_MIDDLE, 4);
		autotileTopLeftCorner.put(TOP_MIDDLE | MIDDLE_RIGHT | MIDDLE_LEFT, 9);
		autotileTopLeftCorner
				.put(MIDDLE_RIGHT | BOTTOM_MIDDLE | MIDDLE_LEFT, 1);
		autotileTopLeftCorner.put(TOP_MIDDLE | BOTTOM_MIDDLE | MIDDLE_LEFT, 6);
		autotileTopLeftCorner.put(TOP_MIDDLE | MIDDLE_RIGHT | BOTTOM_MIDDLE
				| MIDDLE_LEFT, 5);

		autotileTopRightCorner = new HashMap<Integer, Integer>(16);
		autotileTopRightCorner.put(0, 3);
		autotileTopRightCorner.put(TOP_MIDDLE, 11);
		autotileTopRightCorner.put(MIDDLE_LEFT, 3);
		autotileTopRightCorner.put(BOTTOM_MIDDLE, 3);
		autotileTopRightCorner.put(MIDDLE_RIGHT, 1);
		autotileTopRightCorner.put(TOP_MIDDLE | MIDDLE_LEFT, 11);
		autotileTopRightCorner.put(TOP_MIDDLE | BOTTOM_MIDDLE, 7);
		autotileTopRightCorner.put(TOP_MIDDLE | MIDDLE_RIGHT, 9);
		autotileTopRightCorner.put(MIDDLE_LEFT | BOTTOM_MIDDLE, 3);
		autotileTopRightCorner.put(MIDDLE_LEFT | MIDDLE_RIGHT, 2);
		autotileTopRightCorner.put(MIDDLE_RIGHT | BOTTOM_MIDDLE, 1);
		autotileTopRightCorner
				.put(TOP_MIDDLE | MIDDLE_RIGHT | BOTTOM_MIDDLE, 5);
		autotileTopRightCorner.put(TOP_MIDDLE | MIDDLE_RIGHT | MIDDLE_LEFT, 10);
		autotileTopRightCorner.put(MIDDLE_RIGHT | BOTTOM_MIDDLE | MIDDLE_LEFT,
				2);
		autotileTopRightCorner.put(TOP_MIDDLE | BOTTOM_MIDDLE | MIDDLE_LEFT, 7);
		autotileTopRightCorner.put(TOP_MIDDLE | MIDDLE_RIGHT | BOTTOM_MIDDLE
				| MIDDLE_LEFT, 6);

		autotileBottomLeftCorner = new HashMap<Integer, Integer>(16);
		autotileBottomLeftCorner.put(0, 12);
		autotileBottomLeftCorner.put(TOP_MIDDLE, 12);
		autotileBottomLeftCorner.put(MIDDLE_LEFT, 14);
		autotileBottomLeftCorner.put(BOTTOM_MIDDLE, 4);
		autotileBottomLeftCorner.put(MIDDLE_RIGHT, 12);
		autotileBottomLeftCorner.put(TOP_MIDDLE | MIDDLE_LEFT, 14);
		autotileBottomLeftCorner.put(TOP_MIDDLE | BOTTOM_MIDDLE, 8);
		autotileBottomLeftCorner.put(TOP_MIDDLE | MIDDLE_RIGHT, 12);
		autotileBottomLeftCorner.put(MIDDLE_LEFT | BOTTOM_MIDDLE, 6);
		autotileBottomLeftCorner.put(MIDDLE_LEFT | MIDDLE_RIGHT, 13);
		autotileBottomLeftCorner.put(MIDDLE_RIGHT | BOTTOM_MIDDLE, 4);
		autotileBottomLeftCorner.put(TOP_MIDDLE | MIDDLE_RIGHT | BOTTOM_MIDDLE,
				8);
		autotileBottomLeftCorner.put(TOP_MIDDLE | MIDDLE_RIGHT | MIDDLE_LEFT,
				13);
		autotileBottomLeftCorner.put(
				MIDDLE_RIGHT | BOTTOM_MIDDLE | MIDDLE_LEFT, 5);
		autotileBottomLeftCorner.put(TOP_MIDDLE | BOTTOM_MIDDLE | MIDDLE_LEFT,
				10);
		autotileBottomLeftCorner.put(TOP_MIDDLE | MIDDLE_RIGHT | BOTTOM_MIDDLE
				| MIDDLE_LEFT, 9);

		autotileBottomRightCorner = new HashMap<Integer, Integer>(16);
		autotileBottomRightCorner.put(0, 15);
		autotileBottomRightCorner.put(TOP_MIDDLE, 15);
		autotileBottomRightCorner.put(MIDDLE_LEFT, 15);
		autotileBottomRightCorner.put(BOTTOM_MIDDLE, 7);
		autotileBottomRightCorner.put(MIDDLE_RIGHT, 13);
		autotileBottomRightCorner.put(TOP_MIDDLE | MIDDLE_LEFT, 15);
		autotileBottomRightCorner.put(TOP_MIDDLE | BOTTOM_MIDDLE, 11);
		autotileBottomRightCorner.put(TOP_MIDDLE | MIDDLE_RIGHT, 13);
		autotileBottomRightCorner.put(MIDDLE_LEFT | BOTTOM_MIDDLE, 7);
		autotileBottomRightCorner.put(MIDDLE_LEFT | MIDDLE_RIGHT, 14);
		autotileBottomRightCorner.put(MIDDLE_RIGHT | BOTTOM_MIDDLE, 5);
		autotileBottomRightCorner.put(
				TOP_MIDDLE | MIDDLE_RIGHT | BOTTOM_MIDDLE, 9);
		autotileBottomRightCorner.put(TOP_MIDDLE | MIDDLE_RIGHT | MIDDLE_LEFT,
				14);
		autotileBottomRightCorner.put(MIDDLE_RIGHT | BOTTOM_MIDDLE
				| MIDDLE_LEFT, 6);
		autotileBottomRightCorner.put(TOP_MIDDLE | BOTTOM_MIDDLE | MIDDLE_LEFT,
				11);
		autotileBottomRightCorner.put(TOP_MIDDLE | MIDDLE_RIGHT | BOTTOM_MIDDLE
				| MIDDLE_LEFT, 10);

	}
	private static TwoByTwoAutoTileRenderer instance = new TwoByTwoAutoTileRenderer();

	public static TwoByTwoAutoTileRenderer getInstance() {
		return instance;
	}

	private TwoByTwoAutoTileRenderer() {
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha, int x, int y, int z,
			int tile, int xRenderPos, int dY, Map map, Texture[] tilesets) {

		int matchingTiles = 0;
		int convertedTile = ((tile / Constants.TILESET_WIDTH) * (2 * Constants.TILESET_WIDTH_IN_AUTOTILES))
				+ ((tile % Constants.TILESET_WIDTH) * 2);

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
		if (xLeftBound || yDownBound || map.getTile(x - 1, y + 1, z) == tile) {
			matchingTiles |= BOTTOM_LEFT;
		}
		if (xRightBound || yUpBound || map.getTile(x + 1, y - 1, z) == tile) {
			matchingTiles |= TOP_RIGHT;
		}
		if (xRightBound || yDownBound || map.getTile(x + 1, y + 1, z) == tile) {
			matchingTiles |= BOTTOM_RIGHT;
		}

		// Top left corner
		int corner = autotileTopLeftCorner.get(matchingTiles
				& (TOP_MIDDLE | BOTTOM_MIDDLE | MIDDLE_LEFT | MIDDLE_RIGHT));
		int cornerTile = convertedTile
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
						* Constants.AUTOTILE_HEIGHT, Constants.AUTOTILE_WIDTH,
				Constants.AUTOTILE_HEIGHT, false, true);

		// Top right corner
		corner = autotileTopRightCorner.get(matchingTiles
				& (TOP_MIDDLE | BOTTOM_MIDDLE | MIDDLE_LEFT | MIDDLE_RIGHT));
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
						* Constants.AUTOTILE_HEIGHT, Constants.AUTOTILE_WIDTH,
				Constants.AUTOTILE_HEIGHT, false, true);

		// Bottom left corner
		corner = autotileBottomLeftCorner.get(matchingTiles
				& (TOP_MIDDLE | BOTTOM_MIDDLE | MIDDLE_LEFT | MIDDLE_RIGHT));
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
						* Constants.AUTOTILE_HEIGHT, Constants.AUTOTILE_WIDTH,
				Constants.AUTOTILE_HEIGHT, false, true);

		// Bottom right corner
		corner = autotileBottomRightCorner.get(matchingTiles
				& (TOP_MIDDLE | BOTTOM_MIDDLE | MIDDLE_LEFT | MIDDLE_RIGHT));
		cornerTile = convertedTile
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
						* Constants.AUTOTILE_HEIGHT, Constants.AUTOTILE_WIDTH,
				Constants.AUTOTILE_HEIGHT, false, true);

	}

}
