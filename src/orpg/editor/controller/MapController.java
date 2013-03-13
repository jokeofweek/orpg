package orpg.editor.controller;

import java.util.Observable;
import java.util.Observer;

import com.artemis.Component;

import orpg.editor.BaseEditor;
import orpg.shared.data.ComponentList;
import orpg.shared.data.Map;
import orpg.shared.data.MapLayer;
import orpg.shared.data.Segment;
import orpg.shared.data.TileFlag;
import orpg.shared.data.component.Position;

public class MapController extends Observable implements Observer {

	private Map map;
	private EditorSegmentRequestManager requestManager;

	public MapController(BaseEditor baseEditor, Map map) {
		this.map = map;
		this.requestManager = new EditorSegmentRequestManager(baseEditor, map);
		this.requestManager.addObserver(this);

		// Make requests for outer segments
		this.requestManager.requestSegment((short) 1, (short) 0);
		this.requestManager.requestSegment((short) 0, (short) 1);
		this.requestManager.requestSegment((short) 1, (short) 1);
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

	/**
	 * This fetches the segment in which a map-wide x/y position lies in.
	 * 
	 * @param x
	 *            the map-wide x position.
	 * @param y
	 *            the map-wide y position.
	 * @return the segment which contains the x/y position.
	 * @throws IllegalArgumentException
	 *             if the position is not in the map bounds
	 */
	public Segment getPositionSegment(int x, int y)
			throws IllegalArgumentException {
		return this.map.getPositionSegment(x, y);
	}

	/**
	 * This gets the x position of the segment based on a map-wide x position.
	 * For example if segments are 50 tiles wide, and you pass 73, it'll return
	 * x=1.
	 * 
	 * @param x
	 *            the map-wide x position
	 * @return the x of the segment in which this tile lies.
	 */
	public short getSegmentX(int x) {
		return map.getSegmentX(x);
	}

	/**
	 * This gets the y position of the segment based on a map-wide y position.
	 * For example if segments are 50 tiles high, and you pass 73, it'll return
	 * y=1.
	 * 
	 * @param y
	 *            the map-wide y position
	 * @return the y of the segment in which this tile lies.
	 */
	public short getSegmentY(int y) {
		return map.getSegmentY(y);
	}

	/**
	 * This maps a map-wide x position into an x position relative to its
	 * segment. For example if segments are 50 tiles wide, and you pass 73,
	 * it'll return 23.
	 * 
	 * @param x
	 *            the map-wide x position
	 * @return the x of the tile relative to the segment it lies in.
	 */
	public int getXRelativeToSegment(int x) {
		return this.map.getXRelativeToSegment(x);
	}

	/**
	 * This maps a map-wide y position into an y position relative to its
	 * segment. For example if segments are 50 tiles high, and you pass 73,
	 * it'll return 23.
	 * 
	 * @param y
	 *            the map-wide y position
	 * @return the y of the tile relative to the segment it lies in.
	 */
	public int getYRelativeToSegment(int y) {
		return this.map.getYRelativeToSegment(y);
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
		this.map.getPositionSegment(x, y).getTiles()[layer.ordinal()][getXRelativeToSegment(x)][getYRelativeToSegment(y)] = tile;
	}

	/**
	 * This notifies observers that tile values have been changed.
	 */
	public void triggerTileUpdate() {
		this.setChanged();
		this.notifyObservers();
	}

	/**
	 * This fetches the tile at a given position and layer.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return the tile at a given position and layer, else
	 *         {@link Map#LOADING_TILE} if the tile is currently loading.
	 */
	public short getTile(int x, int y, int z) {
		return map.getTile(x, y, z);
	}

	public boolean hasFlag(int x, int y, TileFlag flag) {
		return map.hasFlag(x, y, flag);
	}

	public void setFlag(int x, int y, TileFlag flag, boolean value) {
		if (map.hasFlag(x, y, flag) != value) {
			setChanged();
		}
		map.setFlag(x, y, flag, value);
		notifyObservers();
	}

	public ComponentList findEntity(int x, int y) {
		Segment segment = getPositionSegment(x, y);
		Position position;

		for (ComponentList entity : segment.getEntities()) {
			if ((position = entity.getPosition()) != null) {
				if (position.getX() == x && position.getY() == y) {
					return entity;
				}
			}
		}

		return null;
	}

	public void addEntity(ComponentList entity) {
		// Add the entity based on the position component
		Position position = entity.getPosition();
		if (position != null) {
			getPositionSegment(position.getX(), position.getY()).getEntities()
					.add(entity);
			setChanged();
			notifyObservers();
		}
	}

	public boolean removeEntity(ComponentList entity) {

		// Remove the entity based on the position component
		Position position = entity.getPosition();
		boolean found = false;
		if (position != null) {
			found = getPositionSegment(position.getX(), position.getY())
					.getEntities().remove(entity);
			setChanged();
			notifyObservers();

		}

		return found;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o == requestManager) {
			// If we received a requested segment, update the map and notify
			// observers
			if (arg instanceof Segment) {
				map.updateSegment((Segment) arg, false);
				triggerTileUpdate();
			}
		}
	}

}
