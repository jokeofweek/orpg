package orpg.client.ui.autotile;

import orpg.shared.data.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface AutoTileRenderer {

	public int getCacheValue(int x, int y, int z, Map map);
	
	public void draw(SpriteBatch batch, float parentAlpha, int x, int y, int z,
			int tile, int cacheValue, int xRenderPos, int yRenderPos, Map map,
			Texture[] tilesets);

}
