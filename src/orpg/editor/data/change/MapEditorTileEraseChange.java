package orpg.editor.data.change;

import orpg.editor.controller.MapController;
import orpg.editor.controller.MapEditorController;
import orpg.shared.data.MapLayer;

public class MapEditorTileEraseChange implements EditorChange {

	private MapEditorController editorController;
	private MapController mapController;
	private MapLayer layer;
	private int x;
	private int y;
	private short oldTile;
	private boolean segmentWasChanged;

	public MapEditorTileEraseChange(MapEditorController editorController,
			MapController mapController, int x, int y) {
		this.editorController = editorController;
		this.mapController = mapController;
		this.layer = editorController.getCurrentLayer();
		this.x = x;
		this.y = y;

		this.oldTile = mapController.getPositionSegment(x, y).getTiles()[layer
				.ordinal()][mapController.mapXToSegmentX(x)][mapController
				.mapYToSegmentY(y)];
		this.segmentWasChanged = editorController.hasSegmentChanged(x, y);
	}

	@Override
	public void apply() {
		mapController.updateTile(this.x, this.y, this.layer, (short) 0);
		editorController.setSegmentChanged(this.x, this.y, true);
	}

	@Override
	public void undo() {
		mapController.updateTile(this.x, this.y, this.layer, this.oldTile);
		editorController.setSegmentChanged(this.x, this.y, segmentWasChanged);
	}

}
