package orpg.shared.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import orpg.shared.net.serialize.SerializableValue;
import orpg.shared.net.serialize.ValueSerializer;

public class Segment {

	private short x;
	private short y;
	private int revision;
	private long revisionTime;
	private short[][][] tiles;
	private short width;
	private short height;
	private byte flags[][];
	private List<ComponentList> entities;

	// Transient data
	private Object revisionLock;
	private int[][][] autoTileCache;

	public Segment(short x, short y, short width, short height) {
		this(x, y, width, height,
				new short[MapLayer.values().length][width][height],
				new byte[width][height], 0, System.currentTimeMillis(),
				new ArrayList<ComponentList>());
	}

	public Segment(short x, short y, short width, short height,
			short[][][] tiles, byte[][] flags, int revision, long revisionTime,
			List<ComponentList> entities) {
		// Ensure tiles has right amount of layers
		if (tiles.length != MapLayer.values().length) {
			throw new IllegalArgumentException(
					"Segment layers do not match server layers.");
		}
		// Ensure tiles match width/height
		if (tiles[0].length != width || tiles[0][0].length != height) {
			throw new IllegalArgumentException(
					"Segment width and height do not match passed tiles.");
		}

		if (flags.length != width || flags[0].length != height) {
			throw new IllegalArgumentException(
					"Segment width and height do not match passed flags.");
		}

		this.tiles = tiles;
		this.flags = flags;
		this.x = x;
		this.y = y;
		this.revision = revision;
		this.revisionTime = revisionTime;
		this.height = height;
		this.width = width;
		this.revisionLock = new Object();
		this.entities = entities;
	}

	public short getWidth() {
		return width;
	}

	public short getHeight() {
		return height;
	}

	public List<ComponentList> getEntities() {
		return entities;
	}

	public int getRevision() {
		synchronized (revisionLock) {
			return this.revision;
		}
	}

	public long getRevisionTime() {
		synchronized (revisionLock) {
			return this.revisionTime;
		}
	}

	public boolean revisionMatches(int revision, long revisionTime) {
		synchronized (revisionLock) {
			return this.revision == revision
					&& this.revisionTime == revisionTime;
		}
	}

	/**
	 * This synchronizes the segemnt's revision data with another segment, and
	 * then updates it.
	 * 
	 * @param other
	 */
	public void synchronizeAndUpdateRevision(Segment other) {
		synchronized (revisionLock) {
			this.revision = other.getRevision() + 1;
			this.revisionTime = System.currentTimeMillis();
		}
	}

	public short getX() {
		return x;
	}

	public short getY() {
		return y;
	}

	public short[][][] getTiles() {
		return tiles;
	}

	public byte[][] getFlags() {
		return flags;
	}

	public int[][][] getAutoTileCache() {
		return autoTileCache;
	}

	public void setAutoTileCache(int[][][] autoTileCache) {
		this.autoTileCache = autoTileCache;
	}
}
