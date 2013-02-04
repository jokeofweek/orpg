package orpg.editor.controller;

import java.util.Observable;
import java.util.Observer;

import java.util.List;

import org.apache.pivot.wtk.Action;
import org.apache.pivot.wtk.Component;

import orpg.editor.data.TileRange;
import orpg.editor.data.change.EditorChange;
import orpg.editor.data.change.EditorChangeManager;
import orpg.shared.data.MapLayer;

public class MapEditorController extends Observable implements Observer {

	private MapLayer currentLayer;
	private TileRange tileRange;
	private EditorChangeManager changeManager;
	private Action undoAction;
	private Action redoAction;
	private Action zoomInAction;
	private Action zoomOutAction;

	private static final int SCALE_FACTORS[] = new int[] { 1, 2, 4, 8 };
	private int scaleFactorPosition;

	public MapEditorController() {
		this.currentLayer = MapLayer.GROUND;
		this.tileRange = new TileRange();
		this.changeManager = new EditorChangeManager();
		this.changeManager.addObserver(this);

		setupActions();
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

	private void setupActions() {
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

		this.zoomInAction = new Action() {
			@Override
			public void perform(Component source) {
				zoomIn();
			}
		};
		this.zoomInAction.setEnabled(canZoomIn());

		this.zoomOutAction = new Action() {
			@Override
			public void perform(Component source) {
				zoomOut();
			}
		};
		this.zoomOutAction.setEnabled(canZoomOut());
	}

	public Action getUndoAction() {
		return undoAction;
	}

	public Action getRedoAction() {
		return redoAction;
	}

	public Action getZoomInAction() {
		return zoomInAction;
	}

	public Action getZoomOutAction() {
		return zoomOutAction;
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

	public int getScaleFactor() {
		return MapEditorController.SCALE_FACTORS[scaleFactorPosition];
	}

	public boolean canZoomIn() {
		return scaleFactorPosition > 0;
	}

	public void zoomIn() {
		if (!canZoomIn()) {
			throw new IllegalStateException("Cannot zoom in any closer.");
		}
		scaleFactorPosition--;
		zoomOutAction.setEnabled(canZoomOut());
		zoomInAction.setEnabled(canZoomIn());
		this.setChanged();
		this.notifyObservers();
	}

	public boolean canZoomOut() {
		return scaleFactorPosition < SCALE_FACTORS.length - 1;
	}

	public void zoomOut() {
		if (!canZoomOut()) {
			throw new IllegalStateException("Cannot zoom out any further.");
		}
		scaleFactorPosition++;
		zoomOutAction.setEnabled(canZoomOut());
		zoomInAction.setEnabled(canZoomIn());
		this.setChanged();
		this.notifyObservers();
	}
}
