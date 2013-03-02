package orpg.shared.data;

import java.util.HashMap;

public class Segment {

	private int x;
	private int y;
	private int revision;
	private long revisionTime;
	private short[][][] tiles;
	private short width;
	private short height;
	private boolean blocked[][];
	private HashMap<String, AccountCharacter> players;
	private Object revisionLock;

	public Segment(int x, int y, short width, short height) {
		this(x, y, width, height,
				new short[MapLayer.values().length][width][height],
				new boolean[width][height], 0, System.currentTimeMillis());
	}

	public Segment(int x, int y, short width, short height, short[][][] tiles,
			boolean[][] blocked, int revision, long revisionTime) {
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
		if (blocked.length != width || blocked[0].length != height) {
			throw new IllegalArgumentException(
					"Segment width and height do not match passed simple attrbiutes.");
		}

		this.tiles = tiles;
		this.blocked = blocked;
		this.x = x;
		this.y = y;
		this.revision = revision;
		this.revisionTime = revisionTime;
		this.height = height;
		this.width = width;
		this.players = new HashMap<String, AccountCharacter>();
		this.revisionLock = new Object();
	}

	public short getWidth() {
		return width;
	}

	public short getHeight() {
		return height;
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

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public short[][][] getTiles() {
		return tiles;
	}

	public boolean[][] getBlocked() {
		return blocked;
	}

	public HashMap<String, AccountCharacter> getPlayers() {
		return players;
	}

	public void setPlayers(HashMap<String, AccountCharacter> players) {
		this.players = players;
	}

	public void addPlayer(AccountCharacter accountCharacter) {
		players.put(accountCharacter.getName(), accountCharacter);
	}

	public void removePlayer(AccountCharacter accountCharacter) {
		players.remove(accountCharacter.getName());
	}
}
