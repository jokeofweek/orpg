package orpg.editor.ui.autotile;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.HashMap;

import orpg.editor.controller.MapController;
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
	public void draw(Graphics2D graphics, int x, int y, int z, int tile,
			int xRenderPos, int yRenderPos, int tileWidth, int tileHeight,
			MapController mapController, Image[] tilesets) {
		int matchingTiles = 0;
		int convertedTile = ((tile / Constants.TILESET_WIDTH) * (2 * Constants.TILESET_WIDTH_IN_AUTOTILES))
				+ ((tile % Constants.TILESET_WIDTH) * 2);

		boolean xLeftBound = x == 0;
		boolean xRightBound = x == mapController.getMapWidth() - 1;
		boolean yUpBound = y == 0;
		boolean yDownBound = y == mapController.getMapHeight() - 1;

		if (xLeftBound || mapController.getTile(x - 1, y, z) == tile)
			matchingTiles |= MIDDLE_LEFT;
		if (xRightBound || mapController.getTile(x + 1, y, z) == tile)
			matchingTiles |= MIDDLE_RIGHT;

		if (yUpBound || mapController.getTile(x, y - 1, z) == tile)
			matchingTiles |= TOP_MIDDLE;
		if (yDownBound || mapController.getTile(x, y + 1, z) == tile)
			matchingTiles |= BOTTOM_MIDDLE;

		// Top left corner
		int corner = autotileTopLeftCorner.get(matchingTiles
				& (TOP_MIDDLE | BOTTOM_MIDDLE | MIDDLE_LEFT | MIDDLE_RIGHT));
		int cornerTile = convertedTile
				+ ((corner / (AUTOTILESET_WIDTH)) * Constants.TILESET_WIDTH_IN_AUTOTILES)
				+ (corner % AUTOTILESET_WIDTH);
		int srcX = (cornerTile % Constants.TILESET_WIDTH_IN_AUTOTILES)
				* Constants.AUTOTILE_WIDTH;
		int srcY = ((cornerTile / Constants.TILESET_HEIGHT_IN_AUTOTILES) % Constants.TILESET_HEIGHT_IN_AUTOTILES)
				* Constants.AUTOTILE_HEIGHT;

		graphics.drawImage(tilesets[cornerTile
				/ Constants.AUTOTILES_PER_TILESET], xRenderPos, yRenderPos,
				xRenderPos + (tileWidth / 2), yRenderPos + (tileHeight / 2),
				srcX, srcY, srcX + Constants.AUTOTILE_WIDTH, srcY
						+ Constants.AUTOTILE_HEIGHT, null);

		// Top right corner

		corner = autotileTopRightCorner.get(matchingTiles
				& (TOP_MIDDLE | BOTTOM_MIDDLE | MIDDLE_LEFT | MIDDLE_RIGHT));
		cornerTile = convertedTile
				+ ((corner / (AUTOTILESET_WIDTH)) * Constants.TILESET_WIDTH_IN_AUTOTILES)
				+ (corner % AUTOTILESET_WIDTH);
		srcX = (cornerTile % Constants.TILESET_WIDTH_IN_AUTOTILES)
				* Constants.AUTOTILE_WIDTH;
		srcY = ((cornerTile / Constants.TILESET_HEIGHT_IN_AUTOTILES) % Constants.TILESET_HEIGHT_IN_AUTOTILES)
				* Constants.AUTOTILE_HEIGHT;

		graphics.drawImage(tilesets[cornerTile
				/ Constants.AUTOTILES_PER_TILESET], xRenderPos
				+ (tileWidth / 2), yRenderPos, xRenderPos + (tileWidth),
				yRenderPos + (tileHeight / 2), srcX, srcY, srcX
						+ Constants.AUTOTILE_WIDTH, srcY
						+ Constants.AUTOTILE_HEIGHT, null);

		// Bottom left corner corner =
		corner = autotileBottomLeftCorner.get(matchingTiles
				& (TOP_MIDDLE | BOTTOM_MIDDLE | MIDDLE_LEFT | MIDDLE_RIGHT));
		cornerTile = convertedTile
				+ ((corner / (AUTOTILESET_WIDTH)) * Constants.TILESET_WIDTH_IN_AUTOTILES)
				+ (corner % AUTOTILESET_WIDTH);
		srcX = (cornerTile % Constants.TILESET_WIDTH_IN_AUTOTILES)
				* Constants.AUTOTILE_WIDTH;
		srcY = ((cornerTile / Constants.TILESET_HEIGHT_IN_AUTOTILES) % Constants.TILESET_HEIGHT_IN_AUTOTILES)
				* Constants.AUTOTILE_HEIGHT;

		graphics.drawImage(tilesets[cornerTile
				/ Constants.AUTOTILES_PER_TILESET], xRenderPos, yRenderPos
				+ (tileHeight / 2), xRenderPos + (tileWidth / 2), yRenderPos
				+ (tileHeight), srcX, srcY, srcX + Constants.AUTOTILE_WIDTH,
				srcY + Constants.AUTOTILE_HEIGHT, null);

		// Bottom right corner corner =
		corner = autotileBottomRightCorner.get(matchingTiles
				& (TOP_MIDDLE | BOTTOM_MIDDLE | MIDDLE_LEFT | MIDDLE_RIGHT));
		cornerTile = convertedTile
				+ ((corner / (AUTOTILESET_WIDTH)) * Constants.TILESET_WIDTH_IN_AUTOTILES)
				+ (corner % AUTOTILESET_WIDTH);
		srcX = (cornerTile % Constants.TILESET_WIDTH_IN_AUTOTILES)
				* Constants.AUTOTILE_WIDTH;
		srcY = ((cornerTile / Constants.TILESET_HEIGHT_IN_AUTOTILES) % Constants.TILESET_HEIGHT_IN_AUTOTILES)
				* Constants.AUTOTILE_HEIGHT;

		graphics.drawImage(tilesets[cornerTile
				/ Constants.AUTOTILES_PER_TILESET], xRenderPos
				+ (tileWidth / 2), yRenderPos + (tileHeight / 2), xRenderPos
				+ (tileWidth), yRenderPos + (tileHeight), srcX, srcY, srcX
				+ Constants.AUTOTILE_WIDTH, srcY + Constants.AUTOTILE_HEIGHT,
				null);

	}
}
