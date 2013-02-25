package orpg.editor.map.tool;

import orpg.editor.controller.MapController;
import orpg.editor.controller.MapEditorController;

public interface Tool {

	public void leftClick(MapEditorController editorController,
			MapController mapController, int x, int y);

	public void rightClick(MapEditorController editorController,
			MapController mapController, int x, int y);

}
