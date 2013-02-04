package orpg.editor.controller;

import java.util.Observable;
import java.util.Observer;

import java.util.List;

import org.apache.pivot.wtk.Action;
import org.apache.pivot.wtk.Component;

import orpg.editor.data.EditorChange;
import orpg.editor.data.EditorChangeManager;
import orpg.editor.data.TileRange;
import orpg.shared.data.MapLayer;

public class MapEditorController extends Observable implements Observer {

	private MapLayer currentLayer;
	private TileRange tileRange;
	private EditorChangeManager changeManager;
	private Action undoAction;
	private Action redoAction;

	public MapEditorController() {
		this.currentLayer = MapLayer.GROUND;
		this.tileRange = new TileRange();
		this.changeManager = new EditorChangeManager();
		this.changeManager.addObserver(this);
		
		this.undoAction = new Action() {
			@Override
			public void perform(Component source) {
				getChangeManager().undo();
			}			
		};
		this.undoAction.setEnabled(changeManager.canUndo());
		
		this.redoAction = new Action() {
			@Override
			public void perform(Component source) {
				getChangeManager().redo();
			}			
		};
		this.redoAction.setEnabled(changeManager.canRedo());
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

	public Action getUndoAction() {
		return undoAction;
	}
	
	public Action getRedoAction() {
		return redoAction;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// If the observable is the change manager, notify all our observers.
		if (o == changeManager) {
			undoAction.setEnabled(changeManager.canUndo());
			redoAction.setEnabled(changeManager.canRedo());
			this.setChanged();
			this.notifyObservers();
		}
	}
}
