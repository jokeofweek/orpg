package orpg.server.data.managers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import orpg.server.BaseServer;
import orpg.server.data.store.DataStoreException;
import orpg.server.net.packets.ClientNewMapPacket;
import orpg.shared.Constants;
import orpg.shared.data.AccountCharacter;
import orpg.shared.data.Map;
import orpg.shared.data.Pair;
import orpg.shared.data.Segment;
import orpg.shared.net.InputByteBuffer;

/**
 * @author Dom
 * 
 */
public class MapManager implements Manager<Map, Integer> {

	private Map[] maps;
	private BaseServer baseServer;

	public MapManager(BaseServer baseServer) {
		this.baseServer = baseServer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see orpg.server.data.managers.Manager#load()
	 */
	public boolean setup() {
		this.maps = new Map[baseServer.getConfigManager().getTotalMaps()];

		// Load the maps, creating them if necessary
		Map emptyMapTemplate;
		File file;

		for (int i = 0; i < maps.length; i++) {
			file = new File(Constants.SERVER_MAPS_PATH + "map_" + (i + 1)
					+ ".map");
			if (!file.exists()) {
				// Save the map based on the empty template
				emptyMapTemplate = new Map(i + 1, (short) 3, (short) 3, true);
				try {
					baseServer.getDataStore().saveMap(emptyMapTemplate);
				} catch (DataStoreException e) {
					baseServer
							.getConsole()
							.out()
							.println(
									"Could not create empty map " + (i + 1)
											+ ". Reason: " + e.getMessage());
					return false;
				}
				this.maps[i] = emptyMapTemplate;

			} else {
				// The file exists, so load the map
				try {
					this.maps[i] = baseServer.getDataStore().loadMap(i + 1,
							false);
				} catch (DataStoreException e) {
					baseServer
							.getConsole()
							.out()
							.println(
									"Could not load map " + (i + 1)
											+ ". Reason: " + e.getMessage());
					return false;
				}
			}
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see orpg.server.data.managers.Manager#get(int)
	 */
	public Map get(Integer id) throws IllegalArgumentException {
		if (id <= 0 || id > baseServer.getConfigManager().getTotalMaps()) {
			throw new IllegalArgumentException("No map with number " + id);
		}

		return maps[id - 1];
	}

	/**
	 * This fetches a segment for a given map, loading it if necessary. <b>Note
	 * that null may be returned if there is an error loading the segment.</b>
	 * 
	 * @param id
	 *            the map id.
	 * @param x
	 *            the segment x
	 * @param y
	 *            the segment y
	 * @return the segment.
	 * @throws IllegalArgumentException
	 *             if there is no map with that id
	 * @throws IndexOutOfBoundsException
	 *             if there is no segment with that position on that map.
	 */
	public Segment getSegment(int id, int x, int y)
			throws IllegalArgumentException, IndexOutOfBoundsException {
		if (id <= 0 || id > baseServer.getConfigManager().getTotalMaps()) {
			throw new IllegalArgumentException("No map with number " + id);
		}

		// Offset the id
		id = id - 1;

		if (x < 0 || x >= this.maps[id].getSegmentsWide() || y < 0
				|| y >= this.maps[id].getSegmentsHigh()) {
			throw new IndexOutOfBoundsException("Invalid segment position.");
		}

		if (this.maps[id].getSegment(x, y) == null) {
			// Load the segment if it's not already loaded
			try {
				this.maps[id].updateSegment(baseServer.getDataStore()
						.loadSegment(id + 1, x, y));
			} catch (DataStoreException e) {
				baseServer
						.getConfigManager()
						.getErrorLogger()
						.log(Level.SEVERE,
								"Could not load segment for map " + id + "["
										+ x + "][" + y + "]. Error reason: "
										+ e.getMessage());
			}
		}

		return this.maps[id].getSegment(x, y);
	}

	/**
	 * @return a list of the map names, ordered by ID.
	 */
	public List<String> getNameList() {
		List<String> names = new ArrayList<String>(this.maps.length);
		for (Map map : this.maps) {
			names.add(map.getName());
		}
		return names;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see orpg.server.data.managers.Manager#update(java.lang.Object)
	 */
	public void update(Map map) {
		if (map.getId() <= 0
				|| map.getId() > baseServer.getConfigManager().getTotalMaps()) {
			throw new IllegalArgumentException(
					"Tried to update map with non-existant number "
							+ map.getId());
		}
		this.maps[map.getId() - 1] = map;
	}

	/**
	 * 
	 * This joins a character to a given map, updating the characters position.
	 * 
	 * @param character
	 *            the character to join
	 * @param mapId
	 *            the id of the map we are joining
	 * @param x
	 *            the x position on the map
	 * @param y
	 *            the y position on the map
	 * @throws IllegalArgumentException
	 *             if no map exists with that id
	 * @throws IndexOutOfBoundsException
	 *             if the x and y are out of bounds.
	 */
	private void joinMap(AccountCharacter character, int mapId, int x, int y)
			throws IllegalArgumentException, IndexOutOfBoundsException {
		// Make sure the map is valid
		Map map = get(mapId);

		// Ensure the segment is loaded
		getSegment(map.getId(), map.getSegmentX(x), map.getSegmentY(y));

		map.addPlayer(character);

		// Update the player
		character.setMap(map);
		character.setX(x);
		character.setY(y);

		// Send the player the new map info and then we await for the need map
		baseServer.sendPacket(new ClientNewMapPacket(baseServer
				.getServerSessionManager()
				.getInGameSession(character.getName())));
	}

	/**
	 * This notifies the map the a character is currently on that they are
	 * leaving, and removes the character from the map.
	 * 
	 * @param character
	 *            the character that is leaving the map
	 */
	private void leaveMap(AccountCharacter character) {
		character.getMap().removePlayer(character);
	}

	/**
	 * This warps a specific character to a given map and position, taking care
	 * of leaving the player's old map if there was one.
	 * 
	 * @param character
	 * @param mapId
	 * @param x
	 * @param y
	 */
	public void warpToMap(AccountCharacter character, int mapId, int x, int y) {
		// We must check here if character is changing map as well, as it would
		// be true when logging in.
		if (character.getMap() != null && !character.isChangingMap()) {
			if (mapId == character.getMap().getId()) {
				// Then we don't need to send map ID, just update the position
				// TODO: Update position
				return;
			} else {
				leaveMap(character);
			}
		}

		character.setChangingMap(true);

		joinMap(character, mapId, x, y);

	}

}
