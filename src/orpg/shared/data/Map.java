package orpg.shared.data;

import orpg.shared.Constants;

public class Map {

	private int width;
	private int height;
	private int segmentWidth;
	private int segmentHeight;
	private short[][][] tiles;

	public Map(int segmentsWide, int segmentsHigh) {
		this(Constants.MAP_SEGMENT_WIDTH, Constants.MAP_SEGMENT_HEIGHT, segmentsWide, segmentsHigh);
	}

	public Map(int segmentWidth, int segmentHeight, int segmentsWide,
			int segmentsHigh) {
		this.segmentHeight = segmentHeight;
		this.segmentWidth = segmentWidth;
		this.width = segmentWidth * segmentsWide;
		this.height = segmentHeight * segmentsHigh;
		this.tiles = new short[MapLayer.values().length][height][width];
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getSegmentWidth() {
		return this.segmentWidth;
	}

	public int getSegmentHeight() {
		return segmentHeight;
	}

	public int getSegmentsWide() {
		return this.width / this.segmentWidth;
	}

	public int getSegmentsHigh() {
		return this.height / this.segmentHeight;
	}

	public short[][][] getTiles() {
		return this.tiles;
	}
}
