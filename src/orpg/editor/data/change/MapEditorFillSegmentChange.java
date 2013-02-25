package orpg.editor.data.change;

import orpg.editor.controller.MapController;
import orpg.editor.controller.MapEditorController;
import orpg.editor.data.TileRange;
import orpg.shared.Constants;
import orpg.shared.data.MapLayer;
import orpg.shared.data.Segment;

public class MapEditorFillSegmentChange implements EditorChange {

	private MapEditorController editorController;
	private MapController mapController;
	private int x;
	private int y;
	private int width;
	private int height;
	private MapLayer layer;
	private TileRange tileRange;

	public MapEditorFillSegmentChange(MapEditorController editorController,
			MapController mapController, int x, int y) {
		this.editorController = editorController;
		this.mapController = mapController;
		this.layer = editorController.getCurrentLayer();
		this.tileRange = editorController.getTileRange();

		// Setup bounds.
		Segment segment = mapController.getPositionSegment(x, y);
		this.width = segment.getWidth();
		this.height = segment.getHeight();
		this.x = width * segment.getX();
		this.y = height * segment.getY();
	}

	@Override
	public void apply() {
		int leftDiff = tileRange.getEndX() - tileRange.getStartX() + 1;
		int downDiff = tileRange.getEndY() - tileRange.getStartY() + 1;
		int tile;

		for (int dY = 0; dY < height; dY++) {
			tile = ((tileRange.getStartY() + (dY % downDiff)) * Constants.TILESET_HEIGHT)
					+ tileRange.getStartX();
			for (int dX = 0; dX < width; dX++) {
				mapController.batchUpdateTile(x + dX, y + dY, layer,
						(short) (tile + (dX % leftDiff)));
			}
		}

		editorController.setSegmentChanged(x, y, true);
		mapController.triggerTileUpdate();

	}

	@Override
	public void undo() {
		throw new IllegalStateException("Cannot undo.");
	}

	@Override
	public boolean canUndo() {
		return false;
	}

}
