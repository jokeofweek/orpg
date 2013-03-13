package orpg.editor.data.change.map;

import com.artemis.Entity;

import orpg.editor.controller.MapController;
import orpg.editor.controller.MapEditorController;
import orpg.editor.data.change.EditorChange;
import orpg.shared.data.ComponentList;
import orpg.shared.data.TileFlag;

public class MapEditorEraseEntityChange implements EditorChange {

	private MapEditorController editorController;
	private MapController mapController;
	private ComponentList entity;
	private boolean removed;
	private boolean wasChanged;

	public MapEditorEraseEntityChange(MapEditorController editorController,
			MapController mapController, ComponentList entity) {
		this.entity = entity;
		this.editorController = editorController;
		this.mapController = mapController;
		this.wasChanged = editorController.hasSegmentChanged(mapController
				.getPositionSegment(entity.getPosition().getX(), entity
						.getPosition().getY()));

	}

	@Override
	public void apply() {
		this.removed = mapController.removeEntity(entity);
		editorController
				.setSegmentChanged(mapController.getPositionSegment(entity
						.getPosition().getX(), entity.getPosition().getY()),
						true);

	}

	@Override
	public void undo() {
		if (this.removed) {
			mapController.addEntity(entity);
		}

		if (!wasChanged) {
			editorController.setSegmentChanged(mapController
					.getPositionSegment(entity.getPosition().getX(), entity
							.getPosition().getY()), false);
		}

	}

	@Override
	public boolean canUndo() {
		return true;
	}

}
