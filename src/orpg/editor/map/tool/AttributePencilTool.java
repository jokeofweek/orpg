package orpg.editor.map.tool;

import orpg.editor.controller.MapController;
import orpg.editor.controller.MapEditorController;
import orpg.editor.data.change.map.MapEditorUpdateAttributeChange;

public class AttributePencilTool implements Tool {
	
	private static AttributePencilTool instance = new AttributePencilTool();

	private AttributePencilTool() {
	}

	public static AttributePencilTool getInstance() {
		return instance;
	}

	@Override
	public void leftClick(MapEditorController editorController,
			MapController mapController, int x, int y) {
		editorController.getChangeManager().addChange(
				new MapEditorUpdateAttributeChange(editorController,
						mapController, editorController.getCurrentAttribute(),
						x, y, false));
	}

	@Override
	public void rightClick(MapEditorController editorController,
			MapController mapController, int x, int y) {
		editorController.getChangeManager().addChange(
				new MapEditorUpdateAttributeChange(editorController,
						mapController, editorController.getCurrentAttribute(),
						x, y, true));
	}

}
