package orpg.editor.controller;

import java.util.Observable;
import java.util.Observer;

import orpg.editor.BaseEditor;
import orpg.shared.data.Map;
import orpg.shared.data.MapLayer;
import orpg.shared.data.Segment;

public class MapController extends Observable implements Observer {

	private Map map;
	private EditorSegmentRequestManager requestManager;

	public MapController(BaseEditor baseEditor, Map map) {
		this.map = map;
		this.requestManager = new EditorSegmentRequestManager(baseEditor, map);
		this.requestManager.addObserver(this);

		// Make requests for outer segments
		this.requestManager.requestSegment(1, 0);
		this.requestManager.requestSegment(0, 1);
		this.requestManager.requestSegment(1, 1);
	}

	public Map getMap() {
		return map;
	}

	public String getMapName() {
		return map.getName();
	}

	public void setMapName(String name) {
		this.map.setName(name);
	}

	public int getMapHeight() {
		return this.map.getHeight();
	}

	public int getMapWidth() {
		return this.map.getHeight();
	}

	public EditorSegmentRequestManager getRequestManager() {
		return requestManager;
	}

	public Segment[][] getSegments() {
		return this.map.getSegments();
	}

	public Segment getPositionSegment(int x, int y) {
		return this.map.mapPositionToSegment(x, y);
	}

	public int mapXToSegmentX(int x) {
		return this.map.mapXToSegmentX(x);
	}

	public int mapYToSegmentY(int y) {
		return this.map.mapYToSegmentY(y);
	}

	public short getTile(int x, int y, int z) {
		Segment segment = this.getPositionSegment(x, y);
		if (segment == null) {
			return 2;
		} else {
			return segment.getTiles()[z][mapXToSegmentX(x)][mapYToSegmentY(y)];
		}
	}

	/**
	 * This updates a given tile value at a coordinate point and layer, and will
	 * notify all observers.
	 * 
	 * @param x
	 *            the x-coordinate in the map to update
	 * @param y
	 *            the y-coordinate in the map to update
	 * @param layer
	 *            the layer to update
	 * @param tile
	 *            the new tile value.
	 */
	public void updateTile(int x, int y, MapLayer layer, short tile) {
		batchUpdateTile(x, y, layer, tile);
		triggerTileUpdate();
	}

	/**
	 * This updates a given tile value at a coordinate point and layer, and will
	 * <b>not</b> notify observers. It permits batch tile updating. In order to
	 * notify observers, a call to {@link #triggerTileUpdate()} must be done.
	 * 
	 * @param x
	 *            the x-coordinate in the map to update
	 * @param y
	 *            the y-coordinate in the map to update
	 * @param layer
	 *            the layer to update
	 * @param tile
	 *            the new tile value.
	 */
	public void batchUpdateTile(int x, int y, MapLayer layer, short tile) {
		this.map.mapPositionToSegment(x, y).getTiles()[layer.ordinal()][mapXToSegmentX(x)][mapYToSegmentY(y)] = tile;
	}

	/**
	 * This notifies observers that tile values have been changed.
	 */
	public void triggerTileUpdate() {
		this.setChanged();
		this.notifyObservers();
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o == requestManager) {
			// If we received a requested segment, update the map and notify
			// observers
			if (arg instanceof Segment) {
				map.updateSegment((Segment) arg);
				triggerTileUpdate();
			}
		}
	}

}
