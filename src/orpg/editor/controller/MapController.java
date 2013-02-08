package orpg.editor.controller;

import java.util.Observable;

import orpg.shared.data.Map;
import orpg.shared.data.MapLayer;
import orpg.shared.data.Segment;

public class MapController extends Observable {

	private Map map;

	public MapController(Map map) {
		this.map = map;
	}

	public int getMapHeight() {
		return this.map.getHeight();
	}

	public int getMapWidth() {
		return this.map.getHeight();
	}

	public Segment[][] getSegments() {
		return this.map.getSegments();
	}

	public Segment getSegment(int x, int y) {
		return this.map.getSegment(x, y);
	}

	public int mapXToSegmentX(int x) {
		return this.map.mapXToSegmentX(x);
	}

	public int mapYToSegmentY(int y) {
		return this.map.mapYToSegmentY(y);
	}

	public short getTile(int z, int y, int x) {
		return this.getSegment(x, y).getTiles()[z][mapYToSegmentY(y)][mapXToSegmentX(x)];
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
		this.map.getSegment(x, y).getTiles()[layer.ordinal()][mapYToSegmentY(y)][mapXToSegmentX(x)] = tile;
	}

	/**
	 * This notifies observers that tile values have been changed.
	 */
	public void triggerTileUpdate() {
		this.setChanged();
		this.notifyObservers();
	}

}
