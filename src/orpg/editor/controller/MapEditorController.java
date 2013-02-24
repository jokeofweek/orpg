package orpg.editor.controller;

import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.Action;

import orpg.editor.BaseEditor;
import orpg.editor.EditorWindow;
import orpg.editor.data.TileRange;
import orpg.editor.data.change.EditorChangeManager;
import orpg.shared.data.Map;
import orpg.shared.data.MapLayer;
import orpg.shared.data.Segment;
import orpg.shared.data.TileAttribute;

public class MapEditorController extends EditorController<Map> implements
		Observer {

	private MapLayer currentLayer;
	private TileAttribute currentAttribute;

	private TileRange tileRange;
	private EditorChangeManager changeManager;
	private MapController mapController;

	private Action undoAction;
	private Action redoAction;
	private Action zoomInAction;
	private Action zoomOutAction;

	private boolean gridEnabled;
	private boolean hoverPreviewEnabled;
	private boolean[][] segmentChanged;

	private static final int SCALE_FACTORS[] = new int[] { 1, 2, 4, 8 };
	private int scaleFactorPosition;

	public MapEditorController(BaseEditor baseEditor,
			EditorWindow<Map> editorWindow, MapController mapController) {
		super(baseEditor, editorWindow);
		this.currentAttribute = TileAttribute.BLOCKED;
		this.currentLayer = MapLayer.GROUND;
		this.tileRange = new TileRange();
		this.changeManager = new EditorChangeManager();
		this.changeManager.addObserver(this);
		this.mapController = mapController;

		this.gridEnabled = false;
		this.hoverPreviewEnabled = true;
		this.segmentChanged = new boolean[mapController.getMap()
				.getSegmentsWide()][mapController.getMap().getSegmentsHigh()];

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

	public TileAttribute getCurrentAttribute() {
		return currentAttribute;
	}

	public void setCurrentAttribute(TileAttribute currentAttribute) {
		if (this.currentAttribute != currentAttribute) {
			this.setChanged();
		}
		this.currentAttribute = currentAttribute;
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

	@SuppressWarnings({ "serial" })
	private void setupActions() {

		this.undoAction = new AbstractAction("Undo") {
			@Override
			public void actionPerformed(ActionEvent e) {
				getChangeManager().undo();
			}

		};

		this.undoAction.setEnabled(changeManager.canUndo());

		this.redoAction = new AbstractAction("Redo") {
			@Override
			public void actionPerformed(ActionEvent e) {
				getChangeManager().redo();
			}
		};
		this.redoAction.setEnabled(changeManager.canRedo());

		this.zoomInAction = new AbstractAction("Zoom In") {
			@Override
			public void actionPerformed(ActionEvent e) {
				zoomIn();
			}
		};
		this.zoomInAction.setEnabled(canZoomIn());

		this.zoomOutAction = new AbstractAction("Zoom Out") {
			@Override
			public void actionPerformed(ActionEvent e) {
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

	public boolean isGridEnabled() {
		return gridEnabled;
	}

	public void setGridEnabled(boolean gridEnabled) {
		if (gridEnabled != this.gridEnabled) {
			this.setChanged();
		}
		this.gridEnabled = gridEnabled;
		this.notifyObservers();
	}

	public boolean hasSegmentChanged(int x, int y) {
		if (x < 0 || y < 0 || x >= mapController.getMapWidth()
				|| y >= mapController.getMapHeight()) {
			throw new IllegalArgumentException(
					"Segment position out of bounds.");
		}

		return hasSegmentChanged(mapController.getPositionSegment(x, y));
	}

	public boolean hasSegmentChanged(Segment segment) {
		return segmentChanged[segment.getX()][segment.getY()];
	}

	public void setSegmentChanged(int x, int y, boolean hasChanged) {
		if (x < 0 || y < 0 || x >= mapController.getMapWidth()
				|| y >= mapController.getMapHeight()) {
			throw new IllegalArgumentException(
					"Segment position out of bounds.");
		}

		setSegmentChanged(mapController.getPositionSegment(x, y), hasChanged);
	}

	public void setSegmentChanged(Segment segment, boolean hasChanged) {
		segmentChanged[segment.getX()][segment.getY()] = hasChanged;
	}

	public boolean isHoverPreviewEnabled() {
		return hoverPreviewEnabled;
	}

	public void setHoverPreviewEnabled(boolean hoverPreviewEnabled) {
		if (hoverPreviewEnabled != this.hoverPreviewEnabled) {
			this.setChanged();
		}
		this.hoverPreviewEnabled = hoverPreviewEnabled;
		this.notifyObservers();
	}

	public void save() {
		this.getBaseEditor().saveMap(mapController.getMap(), segmentChanged);
	}
}
