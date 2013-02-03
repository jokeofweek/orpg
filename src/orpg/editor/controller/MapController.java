package orpg.editor.controller;

import java.util.Observable;

import orpg.shared.data.Map;
import orpg.shared.data.MapLayer;

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
	
	public short[][][] getMapTiles() {
		return this.map.getTiles();
	}

	public void updateTile(int x, int y, MapLayer layer, short tile) {
		this.map.getTiles()[y][x][layer.ordinal()] = tile;
		this.setChanged();
		this.notifyObservers("tileUpdate[" + x + "," + y + ","
				+ layer.ordinal() + "]");
	}

}
