package orpg.editor.data.change.map;

import com.artemis.Entity;

import orpg.editor.controller.MapController;
import orpg.editor.controller.MapEditorController;
import orpg.editor.data.change.EditorChange;
import orpg.shared.data.ComponentList;
import orpg.shared.data.TileFlag;

public class MapEditorReplaceEntityChange implements EditorChange {

	private MapEditorController editorController;
	private MapController mapController;
	private ComponentList oldEntity;
	private ComponentList newEntity;
	private boolean wasChanged;

	public MapEditorReplaceEntityChange(MapEditorController editorController,
			MapController mapController, ComponentList oldEntity,
			ComponentList newEntity) {
		this.oldEntity = oldEntity;
		this.newEntity = newEntity;
		this.editorController = editorController;
		this.mapController = mapController;
		this.wasChanged = editorController.hasSegmentChanged(mapController
				.getPositionSegment(oldEntity.getPosition().getX(), oldEntity
						.getPosition().getY()));
	}

	@Override
	public void apply() {
		mapController.removeEntity(oldEntity);
		mapController.addEntity(newEntity);

		editorController.setSegmentChanged(
				mapController.getPositionSegment(
						newEntity.getPosition().getX(), newEntity.getPosition()
								.getY()), true);
	}

	@Override
	public void undo() {
		mapController.removeEntity(newEntity);
		mapController.addEntity(oldEntity);

		if (!wasChanged) {

			editorController.setSegmentChanged(mapController
					.getPositionSegment(oldEntity.getPosition().getX(),
							oldEntity.getPosition().getY()), false);
		}

	}

	@Override
	public boolean canUndo() {
		return true;
	}

}
