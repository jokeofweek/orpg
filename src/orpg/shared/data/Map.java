package orpg.shared.data;

public class Map {

	private int width;
	private int height;
	private short[][][] tiles;

	public Map(int width, int height) {
		this.width = width;
		this.height = height;
		this.tiles = new short[height][width][MapLayer.values().length];
	}

	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public short[][][] getTiles() {
		return this.tiles;
	}
}
