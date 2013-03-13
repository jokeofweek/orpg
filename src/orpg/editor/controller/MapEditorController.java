package orpg.editor.controller;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.Action;

import orpg.editor.BaseEditor;
import orpg.editor.EditorWindow;
import orpg.editor.data.MapEditorTab;
import orpg.editor.data.TileRange;
import orpg.editor.data.change.EditorChangeManager;
import orpg.editor.map.tool.AttributePencilTool;
import orpg.editor.map.tool.PencilTool;
import orpg.editor.map.tool.Tool;
import orpg.shared.data.Map;
import orpg.shared.data.MapLayer;
import orpg.shared.data.Segment;
import orpg.shared.data.TileFlag;

public class MapEditorController extends EditorController<Map> implements
		Observer {

	private TileRange tileRange;
	private EditorChangeManager changeManager;
	private MapController mapController;

	private MapLayer currentLayer;
	private TileFlag currentFlag;
	private Tool currentTool;
	private MapEditorTab currentTab;
	private HashMap<MapEditorTab, Tool> tabTools;

	private Action undoAction;
	private Action redoAction;
	private Action zoomInAction;
	private Action zoomOutAction;

	private boolean gridEnabled;
	private boolean hoverPreviewEnabled;
	private boolean[][] segmentChanged;

	private static final int SCALE_FACTORS[] = new int[] { 1, 2, 4, 8 };
	private int scaleFactorPosition;
	private EditorWindow<Map> editorWindow;

	public MapEditorController(BaseEditor baseEditor,
			EditorWindow<Map> editorWindow, MapController mapController) {
		super(baseEditor);

		this.editorWindow = editorWindow;
		this.tileRange = new TileRange();
		this.changeManager = new EditorChangeManager();
		this.changeManager.addObserver(this);
		this.mapController = mapController;

		this.currentFlag = TileFlag.BLOCKED;
		this.currentLayer = MapLayer.GROUND;
		this.currentTool = PencilTool.getInstance();

		// Setup the tab tools and current tab
		this.tabTools = new HashMap<MapEditorTab, Tool>();
		for (MapEditorTab tab : MapEditorTab.values()) {
			tabTools.put(tab, PencilTool.getInstance());
		}

		// Update tool
		this.tabTools.put(MapEditorTab.ATTRIBUTES,
				AttributePencilTool.getInstance());

		this.setCurrentTab(MapEditorTab.TILES);

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

	public Tool getCurrentTool() {
		return currentTool;
	}

	public void setCurrentTool(Tool currentTool) {
		this.currentTool = currentTool;
	}

	public TileFlag getCurrentTileFlag() {
		return currentFlag;
	}

	public void setCurrentTileFlag(TileFlag tileFlag) {
		if (this.currentFlag != tileFlag) {
			this.setChanged();
		}
		this.currentFlag = tileFlag;
		this.notifyObservers();
	}

	public void setCurrentTab(MapEditorTab editorTab) {
		// To prevent infinite loops
		if (this.currentTab == editorTab) {
			return;
		}

		// Update the tool of the old tab
		if (this.currentTab != null) {
			this.tabTools.put(this.currentTab, currentTool);
		}

		this.currentTab = editorTab;
		this.currentTool = this.tabTools.get(editorTab);
		this.setChanged();
		this.notifyObservers();
	}

	public MapEditorTab getCurrentTab() {
		return currentTab;
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

	@Override
	public List<String> validate() {
		return editorWindow.validate(baseEditor);
	}

	@Override
	public void beforeSave() {
		editorWindow.beforeSave(baseEditor);
	}
}
