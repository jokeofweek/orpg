package orpg.editor.map.tool;

import java.awt.event.MouseEvent;

import orpg.editor.controller.MapController;
import orpg.editor.controller.MapEditorController;
import orpg.editor.data.change.map.MapEditorUpdateFlagChange;

public class AttributePencilTool implements Tool {
	
	private static AttributePencilTool instance = new AttributePencilTool();

	private AttributePencilTool() {
	}

	public static AttributePencilTool getInstance() {
		return instance;
	}

	@Override
	public void leftClick(MouseEvent e, MapEditorController editorController,
			MapController mapController, int x, int y) {
		editorController.getChangeManager().addChange(
				new MapEditorUpdateFlagChange(editorController,
						mapController, editorController.getCurrentTileFlag(),
						x, y, false));
	}

	@Override
	public void rightClick(MouseEvent e, MapEditorController editorController,
			MapController mapController, int x, int y) {
		editorController.getChangeManager().addChange(
				new MapEditorUpdateFlagChange(editorController,
						mapController, editorController.getCurrentTileFlag(),
						x, y, true));
	}

}
