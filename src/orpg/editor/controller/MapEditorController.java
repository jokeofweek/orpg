package orpg.editor.controller;

import java.util.Observable;
import java.util.Observer;

import java.util.List;

import orpg.editor.data.EditorChange;
import orpg.editor.data.EditorChangeManager;
import orpg.editor.data.TileRange;
import orpg.shared.data.MapLayer;

public class MapEditorController extends Observable implements Observer {

	private MapLayer currentLayer;
	private TileRange tileRange;
	private EditorChangeManager changeManager;

	public MapEditorController() {
		this.currentLayer = MapLayer.GROUND;
		this.tileRange = new TileRange();
		this.changeManager = new EditorChangeManager();
		this.changeManager.addObserver(this);
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

	public EditorChangeManager getChangeManager() {
		return changeManager;
	}

	@Override
	public void update(Observable o, Object arg) {
		// If the observable is the change manager, notify all our observers.
		if (o == changeManager) {
			this.setChanged();
			this.notifyObservers();
		}
	}
}
