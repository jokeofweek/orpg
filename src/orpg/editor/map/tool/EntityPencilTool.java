package orpg.editor.map.tool;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import com.artemis.Component;

import orpg.editor.EntityWindow;
import orpg.editor.controller.EntityController;
import orpg.editor.controller.MapController;
import orpg.editor.controller.MapEditorController;
import orpg.editor.data.change.map.MapEditorAddEntityChange;
import orpg.editor.data.change.map.MapEditorEraseEntityChange;
import orpg.shared.data.ComponentList;
import orpg.shared.data.component.Position;

public class EntityPencilTool implements Tool {

	private static EntityPencilTool instance = new EntityPencilTool();

	private int lastSearchX;
	private int lastSearchY;

	private EntityPencilTool() {
	}

	public static EntityPencilTool getInstance() {
		return instance;
	}

	@Override
	public void leftClick(MouseEvent e,
			MapEditorController editorController,
			MapController mapController, int x, int y) {

		ComponentList entity = mapController.findEntity(x, y);
		if (entity == null) {
			Position position = new Position(0, x, y);
			List<Component> components = new ArrayList<Component>(1);
			components.add(position);
			this.lastSearchX = x;
			this.lastSearchX = y;
			editorController.getChangeManager().addChange(
					new MapEditorAddEntityChange(editorController,
							mapController, new ComponentList(components)));
		} else {
			// If we double clicked and last click was same tile, edit the
			// entity
			if (e.getClickCount() == 2 && lastSearchX == x
					&& lastSearchY == y) {
				EntityController controller = new EntityController(
						editorController.getBaseEditor(), entity, null);
				new EntityWindow(controller);
			}
			
			this.lastSearchX = x;
			this.lastSearchY = y;
		}

	}

	@Override
	public void rightClick(MouseEvent e,
			MapEditorController editorController,
			MapController mapController, int x, int y) {
		// If there was an entity at this position, delete it
		ComponentList entity = mapController.findEntity(x, y);
		if (entity != null) {

			editorController.getChangeManager().addChange(
					new MapEditorEraseEntityChange(editorController,
							mapController, entity));
		}
	}

}
