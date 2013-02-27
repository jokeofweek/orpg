package orpg.client.ui;

import orpg.client.BaseClient;
import orpg.client.Paths;
import orpg.client.state.LoadingState;
import orpg.shared.Constants;
import orpg.shared.data.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
					tile = map.getTile(x + tileOffsetX, y + tileOffsetY, layer);
					if (tile != 0 || layer == 0) {
						if (tile == Map.LOADING_TILE) {
							batch.draw(loadingTileTexture, dX, dY,
									Constants.TILE_WIDTH,
									Constants.TILE_HEIGHT, 0, 0,
									Constants.TILE_WIDTH,
									Constants.TILE_HEIGHT, false, true);
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
