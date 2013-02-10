package orpg.shared.data;

import orpg.shared.Constants;

public class Map {
	private int id;
	private short segmentsWide;
	private short segmentsHigh;
	private short segmentWidth;
	private short segmentHeight;
	private int width;
	private int height;
	private Segment[][] segments;

	public Map(int id, short segmentsWide, short segmentsHigh,
			boolean createSegments) {
		this(id, Constants.MAP_SEGMENT_WIDTH, Constants.MAP_SEGMENT_HEIGHT,
				segmentsWide, segmentsHigh, createSegments);
	}

	public Map(int id, short segmentWidth, short segmentHeight,
			short segmentsWide, short segmentsHigh, boolean createSegments) {
		this.id = id;
		this.segmentsWide = segmentsWide;
		this.segmentsHigh = segmentsHigh;
		this.segmentWidth = segmentWidth;
		this.segmentHeight = segmentHeight;
		this.width = segmentWidth * segmentsWide;
		this.height = segmentHeight * segmentsHigh;
		this.segments = new Segment[segmentsWide][segmentsHigh];
		if (createSegments) {
			for (int x = 0; x < segmentsWide; x++) {
				for (int y = 0; y < segmentsHigh; y++) {
					this.segments[x][y] = new Segment(x, y, segmentWidth,
							segmentHeight);
				}
			}
		}
	}

	public Map(int id, short segmentWidth, short segmentHeight,
			Segment[][] segments) {
		this.id = id;
		this.segmentsWide = (short) segments.length;
		this.segmentsHigh = (short) segments[0].length;
		this.segmentWidth = segmentWidth;
		this.segmentHeight = segmentHeight;
		this.width = segmentWidth * segments.length;
		this.height = segmentHeight * segments[0].length;
		this.segments = segments;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public short getSegmentsWide() {
		return segmentsWide;
	}

	public void setSegmentsWide(short segmentsWide) {
		this.segmentsWide = segmentsWide;
	}

	public short getSegmentsHigh() {
		return segmentsHigh;
	}

	public void setSegmentsHigh(short segmentsHigh) {
		this.segmentsHigh = segmentsHigh;
	}

	public short getSegmentWidth() {
		return segmentWidth;
	}

	public void setSegmentWidth(short segmentWidth) {
		this.segmentWidth = segmentWidth;
	}

	public short getSegmentHeight() {
		return segmentHeight;
	}

	public void setSegmentHeight(short segmentHeight) {
		this.segmentHeight = segmentHeight;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
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

	public void updateSegment(Segment segment) {

		if (segment.getX() < 0 || segment.getY() < 0
				|| segment.getX() >= segments.length
				|| segment.getY() >= segments[0].length) {
			throw new IllegalArgumentException("Invalid segment position.");
		}
		this.segments[segment.getX()][segment.getY()] = segment;
	}

	public Segment getPositionSegment(int x, int y) {
		if (x < 0 || y < 0 || x >= this.segmentWidth * this.segments.length
				|| y >= this.segmentHeight * this.segments[0].length) {
			throw new IllegalArgumentException("Invalid segment position.");
		}

		return this.segments[x / this.segmentWidth][y / this.segmentHeight];
	}

	public int mapXToSegmentX(int x) {
		return x % this.segmentWidth;
	}

	public int mapYToSegmentY(int y) {
		return y % this.segmentHeight;
	}

}
