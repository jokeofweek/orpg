package orpg.editor.data.change.map;

import com.artemis.Entity;

import orpg.editor.controller.MapController;
import orpg.editor.controller.MapEditorController;
import orpg.editor.data.change.EditorChange;
import orpg.shared.data.ComponentList;
import orpg.shared.data.TileFlag;

public class MapEditorAddEntityChange implements EditorChange {

	private MapEditorController editorController;
	private MapController mapController;
	private ComponentList entity;
	private boolean wasChanged;

	public MapEditorAddEntityChange(MapEditorController editorController,
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
		mapController.addEntity(entity);

		editorController.setSegmentChanged(mapController.getPositionSegment(
				entity.getPosition().getX(), entity.getPosition().getY()),
				true);
	}

	@Override
	public void undo() {
		mapController.removeEntity(entity);
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
