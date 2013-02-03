package orpg.editor.controller;

import java.util.Observable;

import java.util.List;

import orpg.editor.data.EditorChange;
import orpg.editor.data.TileRange;
import orpg.shared.data.MapLayer;

public class MapEditorController extends Observable {

	private MapLayer currentLayer;
	private TileRange tileRange;

	private List<EditorChange> editorChanges;
	private int changePosition;

	public MapEditorController() {
		currentLayer = MapLayer.GROUND;
		tileRange = new TileRange();
		
	}

	public MapLayer getCurrentLayer() {
		return currentLayer;
	}

	public void setCurrentLayer(MapLayer currentLayer) {
		if (this.currentLayer != currentLayer) {
			this.setChanged();
		}
		this.currentLayer = currentLayer;
		this.notifyObservers();
	}

	public TileRange getTileRange() {
		return tileRange;
	}

	public void updateTileRange(int startX, int startY, int endX, int endY) {
		this.tileRange.setStartX(startX);
		this.tileRange.setStartY(startY);
		this.tileRange.setEndX(endX);
		this.tileRange.setEndY(endY);
		this.setChanged();
		this.notifyObservers();
	}
}
