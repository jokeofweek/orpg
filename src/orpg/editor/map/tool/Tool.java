package orpg.editor.map.tool;

import java.awt.event.MouseEvent;

import orpg.editor.controller.MapController;
import orpg.editor.controller.MapEditorController;

public interface Tool {

	public void leftClick(MouseEvent e, MapEditorController editorController,
			MapController mapController, int x, int y);

	public void rightClick(MouseEvent e, MapEditorController editorController,
			MapController mapController, int x, int y);

}
