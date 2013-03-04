package orpg.client.data.controllers;

import java.util.HashMap;

import orpg.shared.data.Map;

import orpg.client.BaseClient;
import orpg.client.ui.autotile.AutoTileRenderer;
import orpg.client.ui.autotile.TwoByThreeAutoTileRenderer;
import orpg.client.ui.autotile.TwoByTwoAutoTileRenderer;
import orpg.shared.data.AutoTileType;
import orpg.shared.data.MapLayer;
import orpg.shared.data.Segment;

public class AutoTileController {

	private java.util.Map<Short, AutoTileType> autoTiles;
	private java.util.Map<AutoTileType, AutoTileRenderer> autoTileRenderers;
	private BaseClient baseClient;

	public AutoTileController(BaseClient baseClient) {
		this.baseClient = baseClient;
		this.autoTileRenderers = new HashMap<AutoTileType, AutoTileRenderer>(
				2);
		this.autoTileRenderers.put(AutoTileType.TWO_BY_THREE,
				TwoByThreeAutoTileRenderer.getInstance());
		this.autoTileRenderers.put(AutoTileType.TWO_BY_TWO,
				TwoByTwoAutoTileRenderer.getInstance());
	}

	public void setAutoTiles(java.util.Map<Short, AutoTileType> autoTiles) {
		this.autoTiles = autoTiles;
	}

	public java.util.Map<Short, AutoTileType> getAutoTiles() {
		return autoTiles;
	}

	public void updateAutoTileCache(Map map, Segment segment) {
		// Create cache if necessary
		int[][][] cache = segment.getAutoTileCache();
		if (cache == null) {
			cache = new int[MapLayer.values().length][segment.getWidth()][segment
					.getHeight()];
			segment.setAutoTileCache(cache);
		}

		// Iterate through each layer and tile, updating the cache if necessary
		AutoTileType type;
		for (int z = 0; z < MapLayer.values().length; z++) {
			for (short x = 0; x < segment.getWidth(); x++) {
				for (short y = 0; y < segment.getHeight(); y++) {
					type = autoTiles.get(segment.getTiles()[z][x][y]);
					if (type != null) {
						cache[z][x][y] = autoTileRenderers.get(type)
								.getCacheValue(x, y, z, map);
					}
				}
			}
		}
	}

}
