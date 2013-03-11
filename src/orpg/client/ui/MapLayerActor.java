package orpg.client.ui;

import java.util.HashMap;
import java.util.HashSet;

import orpg.client.BaseClient;
import orpg.client.data.component.Camera;
import orpg.client.ui.autotile.AutoTileRenderer;
import orpg.client.ui.autotile.TwoByThreeAutoTileRenderer;
import orpg.client.ui.autotile.TwoByTwoAutoTileRenderer;
import orpg.shared.Constants;
import orpg.shared.data.AutoTileType;
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

	private Texture[] tilesets;
	private Texture loadingTileTexture;

	private java.util.Map<Short, AutoTileType> autotiles;
	private java.util.Map<AutoTileType, AutoTileRenderer> autotileRenderers;

	public MapLayerActor(BaseClient baseClient, Texture[] tilesets,
			Texture loadingTileTexture, int[] layers, int leftX,
			int rightX, int upY, int downY) {
		this.baseClient = baseClient;
		this.layers = layers;
		this.leftX = leftX;
		this.rightX = rightX;
		this.upY = upY;
		this.downY = downY;

		// Calculate tiles wide / high, add 2 for offset
		this.tilesWide = ((rightX - leftX) / Constants.TILE_WIDTH) + 2;
		this.tilesHigh = ((downY - upY) / Constants.TILE_HEIGHT) + 2;

		this.tilesets = tilesets;
		this.loadingTileTexture = loadingTileTexture;

		this.autotiles = baseClient.getAutoTileController().getAutoTiles();

		this.autotileRenderers = new HashMap<AutoTileType, AutoTileRenderer>(
				AutoTileType.values().length);
		this.autotileRenderers.put(AutoTileType.TWO_BY_THREE,
				TwoByThreeAutoTileRenderer.getInstance());
		this.autotileRenderers.put(AutoTileType.TWO_BY_TWO,
				TwoByTwoAutoTileRenderer.getInstance());
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		int x;
		int y;
		short tile;

		if (baseClient.isChangingMap()) {
			return;
		}

		Map map = baseClient.getMap();

		Camera camera = baseClient.getWorld().getMapper(Camera.class).get(baseClient.getEntity());
		
		// Calculate starting position of tile
		int tileOffsetX = camera.getOffsetX() / Constants.TILE_WIDTH;
		int tileOffsetY = camera.getOffsetY() / Constants.TILE_HEIGHT;
		int layer;
		int dY;
		int dX;

		for (int z = 0; z < layers.length; z++) {
			layer = layers[z];
			dY = -(camera.getOffsetY() % Constants.TILE_HEIGHT);
			for (y = 0; y < tilesHigh; y++) {
				dX = -(camera.getOffsetX() % Constants.TILE_WIDTH);
				for (x = 0; x < tilesWide; x++) {
					if ((x + tileOffsetX) >= map.getWidth()
							|| (y + tileOffsetY) >= map.getHeight())
						break;

					tile = map.getTile(x + tileOffsetX, y + tileOffsetY,
							layer);
					if (tile != 0 || layer == 0) {
						if (tile == Map.LOADING_TILE) {
							batch.draw(loadingTileTexture, dX, dY,
									Constants.TILE_WIDTH,
									Constants.TILE_HEIGHT, 0, 0,
									Constants.TILE_WIDTH,
									Constants.TILE_HEIGHT, false, true);
						} else if (autotiles.containsKey(tile)) {
							autotileRenderers.get(autotiles.get(tile))
									.draw(batch,
											parentAlpha,
											x + tileOffsetX,
											y + tileOffsetY,
											z,
											tile,
											map.getAutoTileCacheValue(x
													+ tileOffsetX, y
													+ tileOffsetY, z), dX,
											dY, map, this.tilesets);
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
}
