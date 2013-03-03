package orpg.client.ui.autotile;

import orpg.shared.data.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface AutoTileRenderer {

	public void draw(SpriteBatch batch, float parentAlpha, int x, int y, int z,
			int tile, int xRenderPos, int yRenderPos, Map map,
			Texture[] tilesets);

}
