package orpg.client.ui.autotile;

import java.util.HashMap;

import orpg.editor.ui.autotile.TwoByThreeAutoTileRenderer2;
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

	private static final HashMap<Integer, Integer> autotileTopLeftCorner;
	//private static final HashMap<Integer, Integer> autotileTopRightCorner;
	//private static final HashMap<Integer, Integer> autotileBottomLeftCorner;
	//private static final HashMap<Integer, Integer> autotileBottomRightCorner;
	private static final int AUTOTILESET_WIDTH = 4; // In autotiles

	static {
		autotileTopLeftCorner = new HashMap<Integer, Integer>(7);
		autotileTopLeftCorner.put(0,0);
		autotileTopLeftCorner.put(TOP_MIDDLE,8);
		autotileTopLeftCorner.put(MIDDLE_LEFT,2);
		autotileTopLeftCorner.put(BOTTOM_MIDDLE,0);
		autotileTopLeftCorner.put(MIDDLE_RIGHT,0);
		autotileTopLeftCorner.put(TOP_MIDDLE|MIDDLE_LEFT,8);
		autotileTopLeftCorner.put(TOP_MIDDLE|BOTTOM_MIDDLE,4);
		autotileTopLeftCorner.put(TOP_MIDDLE|MIDDLE_RIGHT,8);
		autotileTopLeftCorner.put(MIDDLE_LEFT|BOTTOM_MIDDLE,2);
		autotileTopLeftCorner.put(MIDDLE_LEFT|MIDDLE_RIGHT,1);
		autotileTopLeftCorner.put(MIDDLE_RIGHT|BOTTOM_MIDDLE,0);
		autotileTopLeftCorner.put(TOP_MIDDLE|MIDDLE_LEFT|BOTTOM_MIDDLE,4);
		autotileTopLeftCorner.put(TOP_MIDDLE|MIDDLE_LEFT|MIDDLE_RIGHT,8);
		autotileTopLeftCorner.put(MIDDLE_LEFT|BOTTOM_MIDDLE|MIDDLE_RIGHT,1);
		autotileTopLeftCorner.put(TOP_MIDDLE|MIDDLE_LEFT|BOTTOM_MIDDLE|MIDDLE_RIGHT,5);

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

		/*
		// Top right corner
		corner = autotileTopRightCorner.get(matchingTiles
				& (TOP_RIGHT | MIDDLE_RIGHT | TOP_MIDDLE));
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
				& (MIDDLE_LEFT | BOTTOM_LEFT | BOTTOM_MIDDLE));
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
				& (MIDDLE_RIGHT | BOTTOM_RIGHT | BOTTOM_MIDDLE));
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
				*/
	}

}
