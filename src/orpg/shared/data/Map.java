package orpg.shared.data;

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
			segment.setPlayers(oldSegment.getPlayers());

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

		return !isBlocked(x, y);
	}

	public boolean isBlocked(int x, int y) {
		Segment segment = this.getPositionSegment(x, y);
		if (segment == null) {
			return false;
		} else {
			return (segment.getBlocked()[getXRelativeToSegment(x)][getYRelativeToSegment(y)]);
		}
	}

	public void setBlocked(int x, int y, boolean isBlocked) {
		Segment segment = this.getPositionSegment(x, y);
		if (segment != null) {
			segment.getBlocked()[getXRelativeToSegment(x)][getYRelativeToSegment(y)] = isBlocked;
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

	/**
	 * This adds a character to the map, putting it in the correct segment. Note
	 * that this should not be called directly, as no tests are done to make
	 * sure a segment is loaded. Rather, the {@link MapController} should be
	 * used.
	 * 
	 * @param character
	 *            the character to add.
	 * @return true if the character was added, or false if the segment is not
	 *         yet loaded.
	 */
	public boolean addPlayer(AccountCharacter character) {
		Segment segment = getPositionSegment(character.getX(), character.getY());
		if (segment != null) {
			segment.addPlayer(character);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This updates the position of the character in the map.
	 * 
	 * @param character
	 *            the character with the updated position
	 * @param oldX
	 *            the old X position of the character
	 * @param oldY
	 *            the old Y position of the character
	 */
	public void updatePlayer(AccountCharacter character, int oldX, int oldY) {
		Segment oldSegment = getPositionSegment(oldX, oldY);
		Segment newSegment = getPositionSegment(character.getX(),
				character.getY());

		if (oldSegment != newSegment) {
			oldSegment.removePlayer(character);
			newSegment.addPlayer(character);
		}
	}

	public AccountCharacter findPlayer(String name) {
		for (Segment[] segmentRow : getSegments()) {
			for (Segment segment : segmentRow) {
				if (segment != null) {
					if (segment.getPlayers().containsKey(name)) {
						return segment.getPlayers().get(name);
					}
				}
			}
		}
		return null;
	}

	/**
	 * This synchronizes a character's state with the character stored in the
	 * map. It replaces the instance in the map with the passed instance, to
	 * allow modification via {@link BaseClient#getAccountCharacter()}.
	 * 
	 * @param character
	 *            the character to sync.
	 */
	public void syncPlayer(AccountCharacter character) {
		AccountCharacter mapCharacter = findPlayer(character.getName());

		// If they are already the same instance, return early
		if (mapCharacter == character) {
			return;
		}

		character.setX(mapCharacter.getX());
		character.setY(mapCharacter.getY());
		character.setDirection(mapCharacter.getDirection());

		getPositionSegment(mapCharacter.getX(), mapCharacter.getY()).addPlayer(
				character);
	}

	/**
	 * This removes a character from the map. Note that this should not be
	 * called directly, as no tests are done to make sure a segment is loaded.
	 * Rather, the {@link MapController} should be used.
	 * 
	 * @param character
	 *            the character to remove.
	 * @return true if the character was removed, or false if the segment is not
	 *         yet loaded.
	 * @throws IllegalArgumentException
	 *             if the character is not on this map.
	 */

	public boolean removePlayer(AccountCharacter character)
			throws IllegalArgumentException {
		// Sanity test
		if (character.getMap() != this) {
			throw new IllegalArgumentException("Character is not on map "
					+ this + ", so could not be removed.");
		}
		Segment segment = getPositionSegment(character.getX(), character.getY());
		if (segment != null) {
			segment.removePlayer(character);
			return true;
		} else {
			return false;
		}
	}
}
