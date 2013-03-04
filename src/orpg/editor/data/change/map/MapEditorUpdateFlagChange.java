package orpg.editor.data.change.map;

import orpg.editor.controller.MapController;
import orpg.editor.controller.MapEditorController;
import orpg.editor.data.change.EditorChange;
import orpg.shared.data.TileFlag;

public class MapEditorUpdateFlagChange implements EditorChange {

	private MapEditorController editorController;
	private MapController mapController;
	private TileFlag flag;
	private int x;
	private int y;
	private boolean isErasing;
	private boolean segmentWasChanged;
	private boolean oldValue;

	public MapEditorUpdateFlagChange(MapEditorController editorController,
			MapController mapController, TileFlag flag, int x, int y,
			boolean isErasing) {
		this.editorController = editorController;
		this.mapController = mapController;
		this.flag = flag;
		this.x = x;
		this.y = y;
		this.isErasing = isErasing;
		this.segmentWasChanged = editorController.hasSegmentChanged(x, y);
		this.oldValue = mapController.hasFlag(x, y, flag);

	}

	@Override
	public void apply() {
		mapController.setFlag(x, y, flag, !isErasing);
		editorController.setSegmentChanged(x, y, true);
	}

	@Override
	public void undo() {
		mapController.setFlag(x, y, flag, oldValue);
		editorController.setSegmentChanged(x, y, segmentWasChanged);
	}

	@Override
	public boolean canUndo() {
		return true;
	}

}
