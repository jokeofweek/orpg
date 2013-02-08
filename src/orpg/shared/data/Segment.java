package orpg.shared.data;

public class Segment {

	private int x;
	private int y;
	private short[][][] tiles;
	private short width;
	private short height;

	public Segment(int x, int y, short width, short height) {
		this(x, y, width, height,
				new short[MapLayer.values().length][height][width]);
	}

	public Segment(int x, int y, short width, short height, short[][][] tiles) {
		// Ensure tiles has right amount of layers
		if (tiles.length != MapLayer.values().length) {
			throw new IllegalArgumentException("Segment layers do not match server layers.");
		}
		// Ensure tiles match width/height
		if (tiles[0].length != height || tiles[0][0].length != width) {
			throw new IllegalArgumentException(
					"Segment width and height do not match passed tiles.");
		}
		
		this.tiles = tiles;
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
	}

	public short getWidth() {
		return width;
	}

	public short getHeight() {
		return height;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public short[][][] getTiles() {
		return tiles;
	}

}
