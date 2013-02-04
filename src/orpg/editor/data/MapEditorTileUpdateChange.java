package orpg.editor.data;

import orpg.editor.controller.MapController;
import orpg.editor.controller.MapEditorController;
import orpg.shared.Constants;
import orpg.shared.data.MapLayer;

public class MapEditorTileUpdateChange implements EditorChange {

	private MapEditorController editorController;
	private MapController mapController;
	private TileRange range;
	private MapLayer layer;
	private short[][] oldTiles;
	private int x;
	private int y;

	public MapEditorTileUpdateChange(MapEditorController editorController,
			MapController mapController, int x, int y) {
		this.range = new TileRange(editorController.getTileRange());
		this.layer = editorController.getCurrentLayer();
		this.x = x;
		this.y = y;
		this.editorController = editorController;
		this.mapController = mapController;

		// Copy all of the old tile data.
		short[][][] tiles = mapController.getMapTiles();
		int layerOrd = layer.ordinal();
		int diffY = this.range.getEndY() - this.range.getStartY() + 1;
		int diffX = this.range.getEndX() - this.range.getStartX() + 1;
		oldTiles = new short[diffY][diffX];

		for (int dY = 0; dY < diffY; dY++) {
			for (int dX = 0; dX < diffX; dX++) {
				oldTiles[dY][dX] = tiles[y + dY][x + dX][layerOrd];
			}
		}
	}

	@Override
	public void apply() {
		// Modify all the tiles in batch
		int startX = this.range.getStartX();
		int startY = this.range.getStartY();

		// Here we determine how much of the tiles in our range we can actually
		// place.
		int endX = Math
				.min(mapController.getMapWidth(), startX
						+ (editorController.getTileRange().getEndX()
								- editorController.getTileRange()
										.getStartX() + 1));
		int endY = Math
				.min(mapController.getMapHeight(), startY
						+ (editorController.getTileRange().getEndY()
								- editorController.getTileRange()
										.getStartY() + 1));

		// Update the tiles
		short tile = (short) (editorController.getTileRange().getStartX() + (editorController
				.getTileRange().getStartY() * Constants.TILESET_WIDTH));

		for (int dY = 0; dY < endY - startY; dY++) {
			for (int dX = 0; dX < endX - startX; dX++) {
				mapController.batchUpdateTile(this.x + dX, this.y + dY,
						layer, (short) (tile + dX));
			}
			tile += Constants.TILESET_WIDTH;
		}

		mapController.triggerTileUpdate();

	}

	@Override
	public boolean canApply() {
		return true;
	}

	@Override
	public void undo() {
		// Modify all the tiles in batch
		int startX = this.range.getStartX();
		int startY = this.range.getStartY();

		// Here we determine how much of the tiles in our range we can actually
		// place.
		int endX = Math
				.min(mapController.getMapWidth(), startX
						+ (editorController.getTileRange().getEndX()
								- editorController.getTileRange()
										.getStartX() + 1));
		int endY = Math
				.min(mapController.getMapHeight(), startY
						+ (editorController.getTileRange().getEndY()
								- editorController.getTileRange()
										.getStartY() + 1));

		// Update the tiles, only we use the tiles from the tiles array
		for (int dY = startY; dY < endY; dY++) {
			for (int dX = startX; dX < endX; dX++) {
				mapController.batchUpdateTile(this.x + (dX - startX),
						this.y + (dY - startY), layer, this.oldTiles[dY
								- startY][dX - startX]);
			}
		}

		mapController.triggerTileUpdate();
	}

	@Override
	public boolean canUndo() {
		return true;
	}

}
