package orpg.shared.data;

import java.util.HashMap;

public class Segment {

	private int x;
	private int y;
	private short[][][] tiles;
	private short width;
	private short height;
	private boolean blocked[][];
	private HashMap<String, AccountCharacter> players;

	public Segment(int x, int y, short width, short height) {
		this(x, y, width, height,
				new short[MapLayer.values().length][width][height],
				new boolean[width][height]);
	}

	public Segment(int x, int y, short width, short height, short[][][] tiles,
			boolean[][] blocked) {
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
		this.height = height;
		this.width = width;
		this.players = new HashMap<String, AccountCharacter>();
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
