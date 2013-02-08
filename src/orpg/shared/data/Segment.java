package orpg.shared.data;

public class Segment {
	
	private short[][][] tiles;
	
	public Segment(int width, int height) {
		this.tiles = new short[MapLayer.values().length][height][width];
	}
	
	public short[][][] getTiles() {
		return tiles;
	}

}
