package orpg.shared.data;

import javax.swing.JOptionPane;

import com.artemis.Entity;

import orpg.client.BaseClient;
import orpg.server.data.controllers.MapController;
import orpg.shared.Constants;

public class Map {

	public static final short LOADING_TILE = -1;

	private int id;
	private short segmentsWide;
	private short segmentsHigh;
	private short segmentWidth;
	private short segmentHeight;
	private int width;
	private int height;
	private String name;

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
		this.name = "";
		if (createSegments) {
			for (short x = 0; x < segmentsWide; x++) {
				for (short y = 0; y < segmentsHigh; y++) {
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
		this.name = "";
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

	public Segment getSegment(short x, short y) {
		if (x < 0 || y < 0 || x >= segments.length || y >= segments[0].length) {
			throw new IllegalArgumentException("Invalid segment position.");
		}

		return this.segments[x][y];
	}

	public void updateSegment(Segment segment, boolean updateRevision) {
		if (segment.getX() < 0 || segment.getY() < 0
				|| segment.getX() >= segments.length
				|| segment.getY() >= segments[0].length) {
			throw new IllegalArgumentException("Invalid segment position.");
		}

		// Copy over players
		Segment oldSegment = this.segments[segment.getX()][segment.getY()];
		if (oldSegment != null) {
			// Update the map revision
			if (updateRevision) {
				segment.synchronizeAndUpdateRevision(oldSegment);
			}
		}

		this.segments[segment.getX()][segment.getY()] = segment;
	}

	public void updateDescriptor(Map map) {
		this.name = map.getName();

		// TODO: Wait until implemented resizing segments.
		/*
		 * this.segmentsWide = map.getSegmentsWide(); this.segmentsHigh =
		 * map.getSegmentsHigh(); this.segmentWidth = map.getSegmentWidth();
		 * this.segmentHeight = map.getSegmentHeight(); this.width =
		 * segmentWidth * segments.length; this.height = segmentHeight *
		 * segments[0].length;
		 */
	}

	public Segment getPositionSegment(int x, int y)
			throws IllegalArgumentException {
		if (x < 0 || y < 0 || x >= this.segmentWidth * this.segments.length
				|| y >= this.segmentHeight * this.segments[0].length) {
			throw new IllegalArgumentException("Invalid segment position.");
		}

		return this.segments[x / this.segmentWidth][y / this.segmentHeight];
	}

	public short getSegmentX(int x) {
		return (short) (x / this.segmentWidth);
	}

	public short getSegmentY(int y) {
		return (short) (y / this.segmentHeight);
	}

	public short getXRelativeToSegment(int x) {
		return (short) (x % this.segmentWidth);
	}

	public short getYRelativeToSegment(int y) {
		return (short) (y % this.segmentHeight);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isWalkable(int x, int y) {
		// Check bounds
		if (x < 0 || y < 0 || x >= this.segmentsWide * this.segmentWidth
				|| y >= this.segmentsHigh * this.segmentHeight) {
			return false;
		}

		return !hasFlag(x, y, TileFlag.BLOCKED);
	}

	public boolean hasFlag(int x, int y, TileFlag flag) {
		Segment segment = this.getPositionSegment(x, y);
		if (segment == null) {
			return false;
		} else {
			return ((segment.getFlags()[getXRelativeToSegment(x)][getYRelativeToSegment(y)]) & flag
					.getMask()) != 0;
		}
	}

	public void setFlag(int x, int y, TileFlag flag, boolean enabled) {
		Segment segment = this.getPositionSegment(x, y);
		if (segment != null) {
			if (enabled) {
				segment.getFlags()[getXRelativeToSegment(x)][getYRelativeToSegment(y)] |= flag
						.getMask();
			} else {
				segment.getFlags()[getXRelativeToSegment(x)][getYRelativeToSegment(y)] &= ~flag
						.getMask();
			}
		}
	}

	/**
	 * This fetches the autotile cached value at a given position and layer.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return the autotile cached value at a given position and layer, else
	 *         {@link Map#LOADING_TILE} if the tile is not available.
	 */
	public int getAutoTileCacheValue(int x, int y, int z) {
		Segment segment = this.getPositionSegment(x, y);
		if (segment == null || segment.getAutoTileCache() == null) {
			return LOADING_TILE;
		} else {
			return segment.getAutoTileCache()[z][getXRelativeToSegment(x)][getYRelativeToSegment(y)];
		}
	}

	/**
	 * This fetches the tile at a given position and layer.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return the tile at a given position and layer, else
	 *         {@link Map#LOADING_TILE} if the tile is not available.
	 */
	public short getTile(int x, int y, int z) {
		Segment segment = this.getPositionSegment(x, y);
		if (segment == null) {
			return LOADING_TILE;
		} else {
			return segment.getTiles()[z][getXRelativeToSegment(x)][getYRelativeToSegment(y)];
		}
	}

}
