package orpg.editor.data.change.map;

import orpg.editor.controller.MapController;
import orpg.editor.controller.MapEditorController;
import orpg.editor.data.change.EditorChange;
import orpg.shared.data.TileAttribute;

public class MapEditorUpdateAttributeChange implements EditorChange {

	private MapEditorController editorController;
	private MapController mapController;
	private TileAttribute tileAttribute;
	private int x;
	private int y;
	private boolean isErasing;
	private boolean segmentWasChanged;

	public MapEditorUpdateAttributeChange(MapEditorController editorController,
			MapController mapController, TileAttribute tileAttribute, int x,
			int y, boolean isErasing) {
		this.editorController = editorController;
		this.mapController = mapController;
		this.tileAttribute = tileAttribute;
		this.x = x;
		this.y = y;
		this.isErasing = isErasing;
		this.segmentWasChanged = editorController.hasSegmentChanged(x, y);

	}

	@Override
	public void apply() {
		if (tileAttribute == TileAttribute.BLOCKED) {
			mapController.setBlocked(x, y, !isErasing);
			editorController.setSegmentChanged(x, y, true);
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
