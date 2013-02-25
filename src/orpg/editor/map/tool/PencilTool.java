package orpg.editor.map.tool;

import orpg.editor.controller.MapController;
import orpg.editor.controller.MapEditorController;
import orpg.editor.data.change.map.MapEditorTileEraseChange;
import orpg.editor.data.change.map.MapEditorTileUpdateChange;
import orpg.shared.Constants;
import orpg.shared.data.Map;

public class PencilTool implements Tool {

	private static PencilTool instance = new PencilTool();

	private PencilTool() {
	}

	public static PencilTool getInstance() {
		return instance;
	}

	@Override
	public void leftClick(MapEditorController editorController,
			MapController mapController, int x, int y) {
		// Make sure there is a change:
		int startX = editorController.getTileRange().getStartX();
		int startY = editorController.getTileRange().getStartY();
		int diffX = editorController.getTileRange().getEndX() - startX + 1;
		int diffY = editorController.getTileRange().getEndY() - startY + 1;
		int currentTile = startX + (startY * Constants.TILESET_WIDTH);

		int layer = editorController.getCurrentLayer().ordinal();

		boolean changed = false;
		// Convert to positional values

		// Check each x/y value
		for (int cY = y; cY < Math.min(mapController.getMapHeight(), y + diffY)
				&& !changed; cY++) {
			for (int cX = x; cX < Math.min(mapController.getMapWidth(), x
					+ diffX); cX++) {
				if (mapController.getTile(cX, cY, layer) != currentTile
						+ (cX - x)) {
					if (mapController.getTile(cX, cY, layer) != Map.LOADING_TILE) {
						changed = true;
						break;
					}
				}
			}
			currentTile += Constants.TILESET_WIDTH;
		}

		// If there was a change, add it
		if (changed) {
			editorController.getChangeManager().addChange(
					new MapEditorTileUpdateChange(editorController,
							mapController, x, y));
		}

	}

	@Override
	public void rightClick(MapEditorController editorController,
			MapController mapController, int x, int y) {

		// If a right-click, then erase the tile if not already empty.
		if (mapController.getTile(x, y, editorController.getCurrentLayer()
				.ordinal()) != 0
				&& mapController.getTile(x, y, editorController
						.getCurrentLayer().ordinal()) != Map.LOADING_TILE) {

			editorController.getChangeManager().addChange(
					new MapEditorTileEraseChange(editorController,
							mapController, x, y));
		}
	}

}
