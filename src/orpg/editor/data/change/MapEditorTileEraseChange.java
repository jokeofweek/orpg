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

	public MapEditorTileEraseChange(MapEditorController editorController,
			MapController mapController, int x, int y) {
		this.editorController = editorController;
		this.mapController = mapController;
		this.layer = editorController.getCurrentLayer();
		this.x = x;
		this.y = y;

		this.oldTile = mapController.getSegment(x, y).getTiles()[layer
				.ordinal()][mapController.mapXToSegmentX(x)][mapController
				.mapYToSegmentY(y)];
	}

	@Override
	public void apply() {
		mapController.updateTile(this.x, this.y, this.layer, (short) 0);
	}

	@Override
	public void undo() {
		mapController.updateTile(this.x, this.y, this.layer, this.oldTile);
	}

}
