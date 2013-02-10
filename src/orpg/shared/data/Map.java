package orpg.shared.data;

import orpg.shared.Constants;

public class Map {

	private int id;
	private int width;
	private int height;
	private short segmentWidth;
	private short segmentHeight;
	private Segment[][] segments;

	public Map(short segmentsWide, short segmentsHigh) {
		this(Constants.MAP_SEGMENT_WIDTH, Constants.MAP_SEGMENT_HEIGHT,
				segmentsWide, segmentsHigh);
	}

	public Map(short segmentWidth, short segmentHeight, short segmentsWide,
			short segmentsHigh) {
		this.segmentHeight = segmentHeight;
		this.segmentWidth = segmentWidth;
		this.width = segmentWidth * segmentsWide;
		this.height = segmentHeight * segmentsHigh;
		this.segments = new Segment[segmentsWide][segmentsHigh];
		for (int x = 0; x < segmentsWide; x++) {
			for (int y = 0; y < segmentsHigh; y++) {
				this.segments[x][y] = new Segment(x, y, segmentWidth,
						segmentHeight);
			}
		}
	}

	public Map(short segmentWidth, short segmentHeight, Segment[][] segments) {
		this.segmentWidth = segmentWidth;
		this.segmentHeight = segmentHeight;
		this.width = this.segmentWidth * segments.length;
		this.height = this.segmentHeight * segments[0].length;
		this.segments = segments;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public short getSegmentWidth() {
		return this.segmentWidth;
	}

	public short getSegmentHeight() {
		return segmentHeight;
	}

	public short getSegmentsWide() {
		return (short) this.segments.length;
	}

	public short getSegmentsHigh() {
		return (short) this.segments[0].length;
	}

	public Segment[][] getSegments() {
		return this.segments;
	}

	public Segment getSegment(int x, int y) {

		if (x < 0 || y < 0 || x >= segments.length || y >= segments[0].length) {
			throw new IllegalArgumentException("Invalid segment position.");
		}

		return this.segments[x][y];
	}

	public Segment getPositionSegment(int x, int y) {
		if (x < 0 || y < 0 || x >= segmentWidth * segments.length
				|| y >= segmentHeight * segments[0].length) {
			throw new IllegalArgumentException("Invalid segment position.");
		}

		return this.segments[x / this.segmentWidth][y / this.segmentHeight];
	}

	public int mapXToSegmentX(int x) {
		return x % segmentWidth;
	}

	public int mapYToSegmentY(int y) {
		return y % segmentHeight;
	}

}
