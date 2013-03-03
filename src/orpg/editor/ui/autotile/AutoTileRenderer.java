package orpg.editor.ui.autotile;

import java.awt.Graphics2D;
import java.awt.Image;

import orpg.editor.controller.MapController;
import orpg.shared.data.Map;

public interface AutoTileRenderer {
	
	public void draw(Graphics2D graphics, int x, int y, int z,
			int tile, int xRenderPos, int yRenderPos, int tileWidth, int tileHeight, MapController mapController,
			Image[] tilesets);
	
}
