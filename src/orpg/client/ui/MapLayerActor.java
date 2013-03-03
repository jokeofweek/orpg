package orpg.client.ui;

import java.util.HashMap;

import orpg.client.BaseClient;
import orpg.shared.Constants;
import orpg.shared.data.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MapLayerActor extends Actor {

	private BaseClient baseClient;
	private int[] layers;
	private int leftX;
	private int rightX;
	private int upY;
	private int downY;

	private int tilesWide;
	private int tilesHigh;

	private ViewBox viewbox;

	private Texture[] tilesets;
	private Texture loadingTileTexture;

	private static final int TOP_LEFT = 1;
	private static final int TOP_MIDDLE = 2;
	private static final int TOP_RIGHT = 4;
	private static final int MIDDLE_LEFT = 8;
	private static final int MIDDLE_RIGHT = 16;
	private static final int BOTTOM_LEFT = 32;
	private static final int BOTTOM_MIDDLE = 64;
	private static final int BOTTOM_RIGHT = 128;

	private static final HashMap<Integer, Integer> autotileTopLeftCorner;
	private static final HashMap<Integer, Integer> autotileTopRightCorner;
	private static final HashMap<Integer, Integer> autotileBottomLeftCorner;
	private static final HashMap<Integer, Integer> autotileBottomRightCorner;

	static {
		autotileTopLeftCorner = new HashMap<Integer, Integer>(7);
		autotileTopLeftCorner.put(0, 8);
		autotileTopLeftCorner.put(TOP_LEFT, 8);
		autotileTopLeftCorner.put(TOP_MIDDLE, 12);
		autotileTopLeftCorner.put(TOP_MIDDLE | TOP_LEFT, 12);
		autotileTopLeftCorner.put(MIDDLE_LEFT, 9);
		autotileTopLeftCorner.put(MIDDLE_LEFT | TOP_LEFT, 9);
		autotileTopLeftCorner.put(TOP_MIDDLE | MIDDLE_LEFT, 2);
		autotileTopLeftCorner.put(TOP_MIDDLE | MIDDLE_LEFT | TOP_LEFT, 13);

		autotileTopRightCorner = new HashMap<Integer, Integer>(7);
		autotileTopRightCorner.put(0, 11);
		autotileTopRightCorner.put(TOP_RIGHT, 11);
		autotileTopRightCorner.put(TOP_MIDDLE, 15);
		autotileTopRightCorner.put(TOP_MIDDLE | TOP_RIGHT, 15);
		autotileTopRightCorner.put(MIDDLE_RIGHT, 10);
		autotileTopRightCorner.put(MIDDLE_RIGHT | TOP_RIGHT, 10);
		autotileTopRightCorner.put(TOP_MIDDLE | MIDDLE_RIGHT, 3);
		autotileTopRightCorner.put(TOP_MIDDLE | MIDDLE_RIGHT | TOP_RIGHT, 14);

		autotileBottomLeftCorner = new HashMap<Integer, Integer>(7);
		autotileBottomLeftCorner.put(0, 20);
		autotileBottomLeftCorner.put(BOTTOM_LEFT, 20);
		autotileBottomLeftCorner.put(MIDDLE_LEFT, 21);
		autotileBottomLeftCorner.put(MIDDLE_LEFT | BOTTOM_LEFT, 21);
		autotileBottomLeftCorner.put(BOTTOM_MIDDLE, 16);
		autotileBottomLeftCorner.put(BOTTOM_MIDDLE | BOTTOM_LEFT, 16);
		autotileBottomLeftCorner.put(MIDDLE_LEFT | BOTTOM_MIDDLE, 6);
		autotileBottomLeftCorner.put(MIDDLE_LEFT | BOTTOM_MIDDLE | BOTTOM_LEFT,
				17);

		autotileBottomRightCorner = new HashMap<Integer, Integer>(7);
		autotileBottomRightCorner.put(0, 23);
		autotileBottomRightCorner.put(BOTTOM_RIGHT, 23);
		autotileBottomRightCorner.put(MIDDLE_RIGHT, 22);
		autotileBottomRightCorner.put(MIDDLE_RIGHT | BOTTOM_RIGHT, 22);
		autotileBottomRightCorner.put(BOTTOM_MIDDLE, 19);
		autotileBottomRightCorner.put(BOTTOM_MIDDLE | BOTTOM_RIGHT, 19);
		autotileBottomRightCorner.put(MIDDLE_RIGHT | BOTTOM_MIDDLE, 7);
		autotileBottomRightCorner.put(MIDDLE_RIGHT | BOTTOM_MIDDLE
				| BOTTOM_RIGHT, 18);

	}

	public MapLayerActor(BaseClient baseClient, ViewBox viewbox,
			Texture[] tilesets, Texture loadingTileTexture, int[] layers,
			int leftX, int rightX, int upY, int downY) {
		this.baseClient = baseClient;
		this.layers = layers;
		this.viewbox = viewbox;
		this.leftX = leftX;
		this.rightX = rightX;
		this.upY = upY;
		this.downY = downY;

		// Calculate tiles wide / high, add 2 for offset
		this.tilesWide = ((rightX - leftX) / Constants.TILE_WIDTH) + 2;
		this.tilesHigh = ((downY - upY) / Constants.TILE_HEIGHT) + 2;

		this.tilesets = tilesets;
		this.loadingTileTexture = loadingTileTexture;
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		int x;
		int y;
		int tile;

		if (baseClient.getAccountCharacter().isChangingMap()) {
			return;
		}

		Map map = baseClient.getMap();

		// Calculate starting position of tile
		int tileOffsetX = viewbox.getOffsetX() / Constants.TILE_WIDTH;
		int tileOffsetY = viewbox.getOffsetY() / Constants.TILE_HEIGHT;
		int layer;
		int dY;
		int dX;

		for (int z = 0; z < layers.length; z++) {
			layer = layers[z];
			dY = -(viewbox.getOffsetY() % Constants.TILE_HEIGHT);
			for (y = 0; y < tilesHigh; y++) {
				dX = -(viewbox.getOffsetX() % Constants.TILE_WIDTH);
				for (x = 0; x < tilesWide; x++) {
					if ((x + tileOffsetX) >= map.getWidth()
							|| (y + tileOffsetY) >= map.getHeight())
						break;

					tile = map.getTile(x + tileOffsetX, y + tileOffsetY, layer);
					if (tile != 0 || layer == 0) {
						if (tile == Map.LOADING_TILE) {
							batch.draw(loadingTileTexture, dX, dY,
									Constants.TILE_WIDTH,
									Constants.TILE_HEIGHT, 0, 0,
									Constants.TILE_WIDTH,
									Constants.TILE_HEIGHT, false, true);
						} else if (tile == 122) {
							drawAutoTile(batch, parentAlpha, x + tileOffsetX, y
									+ tileOffsetY, z, tile, dX, dY, map);
						} else {
							batch.draw(
									this.tilesets[tile
											/ Constants.TILES_PER_TILESET],
									dX,
									dY,
									Constants.TILE_WIDTH,
									Constants.TILE_HEIGHT,
									(tile % Constants.TILESET_WIDTH)
											* Constants.TILE_WIDTH,
									((tile / Constants.TILESET_HEIGHT) % Constants.TILESET_HEIGHT)
											* Constants.TILE_HEIGHT,
									Constants.TILE_WIDTH,
									Constants.TILE_HEIGHT, false, true);
						}
					}
					dX += Constants.TILE_WIDTH;
				}
				dY += Constants.TILE_HEIGHT;
			}
		}
	}

	public void drawAutoTile(SpriteBatch batch, float parentAlpha, int x,
			int y, int z, int tile, int dX, int dY, Map map) {
		int matchingTiles = 0;
		int convertedTile = ((tile / Constants.TILESET_WIDTH) * (2 * Constants.TILESET_WIDTH_IN_AUTOTILES))
				+ ((tile % Constants.TILESET_WIDTH) * 2);

		// First check boundaries
		if (x == 0) {
			matchingTiles |= TOP_LEFT | MIDDLE_LEFT | BOTTOM_LEFT;
		} else if (x == map.getWidth() - 1) {
			matchingTiles |= TOP_RIGHT | MIDDLE_RIGHT | BOTTOM_RIGHT;
		}

		if (y == 0) {
			matchingTiles |= TOP_LEFT | TOP_MIDDLE | TOP_RIGHT;
		} else if (y == map.getHeight() - 1) {
			matchingTiles |= BOTTOM_LEFT | BOTTOM_LEFT | BOTTOM_RIGHT;
		}

		if (map.getTile(x - 1, y - 1, z) == tile)
			matchingTiles |= TOP_LEFT;
		if (map.getTile(x, y - 1, z) == tile)
			matchingTiles |= TOP_MIDDLE;
		if (map.getTile(x + 1, y - 1, z) == tile)
			matchingTiles |= TOP_RIGHT;

		if (map.getTile(x - 1, y, z) == tile)
			matchingTiles |= MIDDLE_LEFT;
		if (map.getTile(x + 1, y, z) == tile)
			matchingTiles |= MIDDLE_RIGHT;

		if (map.getTile(x - 1, y + 1, z) == tile)
			matchingTiles |= BOTTOM_LEFT;
		if (map.getTile(x, y + 1, z) == tile)
			matchingTiles |= BOTTOM_MIDDLE;
		if (map.getTile(x + 1, y + 1, z) == tile)
			matchingTiles |= BOTTOM_RIGHT;

		// Top left corner
		int corner = autotileTopLeftCorner.get(matchingTiles
				& (TOP_LEFT | TOP_MIDDLE | MIDDLE_LEFT));
		int cornerTile = convertedTile
				+ ((corner / (Constants.AUTOTILESET_WIDTH)) * Constants.TILESET_WIDTH_IN_AUTOTILES)
				+ (corner % Constants.AUTOTILESET_WIDTH);

		batch.draw(
				this.tilesets[cornerTile / Constants.AUTOTILES_PER_TILESET],
				dX,
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
				& (TOP_RIGHT | MIDDLE_RIGHT | TOP_MIDDLE));
		cornerTile = convertedTile
				+ ((corner / (Constants.AUTOTILESET_WIDTH)) * Constants.TILESET_WIDTH_IN_AUTOTILES)
				+ (corner % Constants.AUTOTILESET_WIDTH);

		batch.draw(
				this.tilesets[cornerTile / Constants.AUTOTILES_PER_TILESET],
				dX + Constants.AUTOTILE_WIDTH,
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
				+ ((corner / (Constants.AUTOTILESET_WIDTH)) * Constants.TILESET_WIDTH_IN_AUTOTILES)
				+ (corner % Constants.AUTOTILESET_WIDTH);

		batch.draw(
				this.tilesets[cornerTile / Constants.AUTOTILES_PER_TILESET],
				dX,
				dY + Constants.AUTOTILE_WIDTH,
				Constants.AUTOTILE_WIDTH,
				Constants.AUTOTILE_HEIGHT,
				(cornerTile % Constants.TILESET_WIDTH_IN_AUTOTILES)
						* Constants.AUTOTILE_WIDTH,
				((cornerTile / Constants.TILESET_HEIGHT_IN_AUTOTILES) % Constants.TILESET_HEIGHT_IN_AUTOTILES)
						* Constants.AUTOTILE_HEIGHT, Constants.AUTOTILE_WIDTH,
				Constants.AUTOTILE_HEIGHT, false, true);

		// Bottom right corner
		System.out.println(matchingTiles);
		corner = autotileBottomRightCorner.get(matchingTiles
				& (MIDDLE_RIGHT | BOTTOM_RIGHT | BOTTOM_MIDDLE));
		cornerTile = convertedTile
				+ ((corner / (Constants.AUTOTILESET_WIDTH)) * Constants.TILESET_WIDTH_IN_AUTOTILES)
				+ (corner % Constants.AUTOTILESET_WIDTH);

		batch.draw(
				this.tilesets[cornerTile / Constants.AUTOTILES_PER_TILESET],
				dX + Constants.AUTOTILE_WIDTH,
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
