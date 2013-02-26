package orpg.editor.data.change.map;

import orpg.editor.controller.MapController;
import orpg.editor.controller.MapEditorController;
import orpg.editor.data.TileRange;
import orpg.editor.data.change.EditorChange;
import orpg.shared.data.MapLayer;
import orpg.shared.data.TileAttribute;

public class MapEditorUpdateAttributeChange implements EditorChange {

	private TileAttribute tileAttribute;
	private MapController mapController;
	private int x;
	private int y;
	private boolean isErasing;

	public MapEditorUpdateAttributeChange(MapEditorController editorController,
			MapController mapController, TileAttribute tileAttribute, int x,
			int y, boolean isErasing) {
		this.tileAttribute = tileAttribute;
		this.mapController = mapController;
		this.x = x;
		this.y = y;
		this.isErasing = isErasing;
	}

	@Override
	public void apply() {
		if (tileAttribute == TileAttribute.BLOCKED) {
			mapController.setBlocked(x, y, !isErasing);
		}
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
