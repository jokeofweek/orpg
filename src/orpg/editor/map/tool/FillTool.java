package orpg.editor.map.tool;

import java.awt.event.MouseEvent;

import orpg.editor.controller.MapController;
import orpg.editor.controller.MapEditorController;
import orpg.editor.data.change.map.MapEditorFillSegmentChange;

public class FillTool implements Tool {

	private static FillTool instance = new FillTool();
	
	private FillTool() {}
	
	public static FillTool getInstance() {
		return instance;
	}
	
	@Override
	public void leftClick(MouseEvent e, MapEditorController editorController,
			MapController mapController, int x, int y) {
		editorController.getChangeManager().addChange(
				new MapEditorFillSegmentChange(editorController, mapController,
						editorController.getTileRange(), editorController
								.getCurrentLayer(), x, y));

	}

	@Override
	public void rightClick(MouseEvent e, MapEditorController editorController,
			MapController mapController, int x, int y) {
		// For now just proxy to pencil right click
		PencilTool.getInstance().rightClick(e, editorController, mapController, x,
				y);
	}

}
