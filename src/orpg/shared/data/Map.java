package orpg.shared.data;

import orpg.shared.Constants;

public class Map {

	private int width;
	private int height;
	private int segmentWidth;
	private int segmentHeight;
	private Segment[][] segments;

	public Map(int segmentsWide, int segmentsHigh) {
		this(Constants.MAP_SEGMENT_WIDTH, Constants.MAP_SEGMENT_HEIGHT,
				segmentsWide, segmentsHigh);
	}

	public Map(short segmentWidth, short segmentHeight, int segmentsWide,
			int segmentsHigh) {
		this.segmentHeight = segmentHeight;
		this.segmentWidth = segmentWidth;
		this.width = segmentWidth * segmentsWide;
		this.height = segmentHeight * segmentsHigh;
		this.segments = new Segment[segmentsHigh][segmentsWide];
		for (int y = 0; y < segmentsHigh; y++) {
			for (int x = 0; x < segmentsWide; x++) {
				this.segments[y][x] = new Segment(x, y, segmentWidth,
						segmentHeight);
			}
		}
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

	public Segment[][] getSegments() {
		return this.segments;
	}

	public Segment getSegment(int x, int y) {
		if (x < 0 || y < 0 || x >= segmentWidth * segments[0].length
				|| y >= segmentHeight * segments.length) {
			throw new IllegalArgumentException("Invalid segment position.");
		}

		return segments[y / this.segmentHeight][x / this.segmentWidth];
	}

	public int mapXToSegmentX(int x) {
		return x % segmentWidth;
	}

	public int mapYToSegmentY(int y) {
		return y % segmentHeight;
	}

}
