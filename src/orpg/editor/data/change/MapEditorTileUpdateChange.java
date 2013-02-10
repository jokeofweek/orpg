package orpg.editor.data.change;

import orpg.editor.controller.MapController;
import orpg.editor.controller.MapEditorController;
import orpg.editor.data.TileRange;
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
		int layerOrd = layer.ordinal();

		// Update the range to fit in the space we have
		if (x + (range.getEndX() - range.getStartX()) >= mapController
				.getMapWidth()) {
			range.setEndX(range.getStartX()
					+ (mapController.getMapWidth() - x - 1));
		}
		if (y + (range.getEndY() - range.getStartY()) >= mapController
				.getMapHeight()) {
			range.setEndY(range.getStartY()
					+ (mapController.getMapHeight() - y - 1));
		}

		// Save our old tiles
		int diffX = range.getEndX() - range.getStartX() + 1;
		int diffY = range.getEndY() - range.getStartY() + 1;
		oldTiles = new short[diffX][diffY];

		for (int dX = 0; dX < diffX; dX++) {
			for (int dY = 0; dY < diffY; dY++) {
				oldTiles[dX][dY] = mapController.getPositionSegment(x + dX, y + dY)
						.getTiles()[layerOrd][mapController.mapXToSegmentX(x
						+ dX)][mapController.mapYToSegmentY(y + dY)];
			}
		}
	}

	@Override
	public void apply() {
		// Update the tiles
		int diffX = range.getEndX() - range.getStartX() + 1;
		int diffY = range.getEndY() - range.getStartY() + 1;

		short tile = (short) (range.getStartX() + (range.getStartY() * Constants.TILESET_WIDTH));

		for (int dY = 0; dY < diffY; dY++) {
			for (int dX = 0; dX < diffX; dX++) {
				mapController.batchUpdateTile(this.x + dX, this.y + dY, layer,
						(short) (tile + dX));
			}
			tile += Constants.TILESET_WIDTH;
		}

		mapController.triggerTileUpdate();

	}

	@Override
	public void undo() {
		// Modify all the tiles in batch
		int startX = this.range.getStartX();
		int startY = this.range.getStartY();

		// Here we determine how much of the tiles in our range we can actually
		// place.
		int endX = Math.min(mapController.getMapWidth(),
				startX + (this.range.getEndX() - this.range.getStartX() + 1));
		int endY = Math.min(mapController.getMapHeight(),
				startY + (this.range.getEndY() - this.range.getStartY() + 1));

		// Update the tiles, only we use the tiles from the tiles array
		for (int dY = startY; dY < endY; dY++) {
			for (int dX = startX; dX < endX; dX++) {
				mapController.batchUpdateTile(this.x + (dX - startX), this.y
						+ (dY - startY), layer, this.oldTiles[dX - startX][dY
						- startY]);
			}
		}

		mapController.triggerTileUpdate();
	}

}
