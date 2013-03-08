package orpg.server.data.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.managers.PlayerManager;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.sun.org.apache.bcel.internal.generic.NEW;

import orpg.server.BaseServer;
import orpg.server.ServerSession;
import orpg.server.data.store.DataStore;
import orpg.shared.data.component.Named;
import orpg.shared.data.component.Position;
import orpg.shared.data.store.DataStoreException;
import orpg.server.net.packets.ClientJoinMapPacket;
import orpg.server.net.packets.ClientLeftMapPacket;
import orpg.server.net.packets.ClientNewMapPacket;
import orpg.shared.Constants;
import orpg.shared.data.AccountCharacter;
import orpg.shared.data.Map;
import orpg.shared.data.Segment;

/**
 * @author Dom
 * 
 */
public class MapController implements Controller<Map, Integer> {

	private ComponentMapper<Position> positionMapper;
	private ComponentMapper<Named> namedMapper;

	private Map[] maps;
	private BaseServer baseServer;
	private DataStore dataStore;

	public MapController(BaseServer baseServer, DataStore dataStore) {
		this.baseServer = baseServer;
		this.dataStore = dataStore;
		this.positionMapper = baseServer.getWorld().getMapper(Position.class);
		this.namedMapper = baseServer.getWorld().getMapper(Named.class);
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
					dataStore.saveMap(emptyMapTemplate);
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
					this.maps[i] = dataStore.loadMap(i + 1, false);
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
	public Segment getSegment(int id, short x, short y)
			throws IllegalArgumentException, IndexOutOfBoundsException {
		if (id <= 0 || id > baseServer.getConfigManager().getTotalMaps()) {
			throw new IllegalArgumentException("No map with number " + id);
		}

		// Offset the id
		id--;

		if (x < 0 || x >= this.maps[id].getSegmentsWide() || y < 0
				|| y >= this.maps[id].getSegmentsHigh()) {
			throw new IndexOutOfBoundsException("Invalid segment position.");
		}

		if (this.maps[id].getSegment(x, y) == null) {
			// Load the segment if it's not already loaded
			try {
				this.maps[id].updateSegment(
						dataStore.loadSegment(id + 1, x, y), false);
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
	 * This attempts to save a map.
	 * 
	 * @param map
	 *            the map to save
	 * @return true if the save was succesful, else false.
	 */
	public boolean save(Map map) {
		try {
			dataStore.saveMap(map);
			update(map);
			return true;
		} catch (DataStoreException e) {
			baseServer
					.getConfigManager()
					.getErrorLogger()
					.log(Level.SEVERE,
							"Could not save map " + map.getId()
									+ " in editor. Reason: " + e.getMessage());
			return false;
		}
	}

	/**
	 * 
	 * This joins an entity to a given map, updating the entity's position.
	 * 
	 * @param entity
	 *            the entity to join
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
	public void joinMap(Entity entity, int mapId, int x, int y)
			throws IllegalArgumentException, IndexOutOfBoundsException {
		// Make sure the map is valid
		Map map = get(mapId);

		// Ensure the segment is loaded
		getSegment(map.getId(), map.getSegmentX(x), map.getSegmentY(y));

		// Update the player
		Position position = positionMapper.get(entity);
		position.setMap(mapId);
		position.setX(x);
		position.setY(y);

		GroupManager groupManager = baseServer.getWorld().getManager(
				GroupManager.class);
		groupManager.add(entity,
				String.format(Constants.GROUP_MAP, position.getMap()));
		groupManager.add(
				entity,
				String.format(Constants.GROUP_SEGMENT, position.getMap(),
						map.getSegmentX(position.getX()),
						map.getSegmentY(position.getY())));

		// If this particular entity is a player, must remove them from segment
		// players as well
		ServerSession session = null;

		if (groupManager.inInGroup(entity, Constants.GROUP_PLAYERS)) {
			groupManager.add(
					entity,
					String.format(Constants.GROUP_MAP_PLAYERS,
							position.getMap()));

			session = baseServer.getServerSessionManager().getEntitySession(
					entity);

			if (session != null) {
				refreshMap(session);
			}
		}

		// Notify the other players that this player has joined the map
		baseServer.sendPacket(new ClientJoinMapPacket(session, map.getId(),
				entity));
	}

	/**
	 * This notifies an entity's given map that the entity is leaving said map.
	 * 
	 * @param entity
	 *            the entity that is leaving the map
	 */
	public void leaveMap(Entity entity) {
		// Remove from the old groups
		GroupManager groupManager = baseServer.getWorld().getManager(
				GroupManager.class);
		Position position = positionMapper.get(entity);

		// If it is a player, get the server session
		ServerSession session = null;
		if (groupManager.inInGroup(entity, Constants.GROUP_PLAYERS)) {
			session = baseServer.getServerSessionManager().getEntitySession(
					entity);
		}

		Map map = get(position.getMap());

		// Notify the other players that this player has left the map
		baseServer.sendPacket(new ClientLeftMapPacket(session, map.getId(),
				entity));

		groupManager.remove(entity,
				String.format(Constants.GROUP_MAP, position.getMap()));
		groupManager.remove(
				entity,
				String.format(Constants.GROUP_SEGMENT, position.getMap(),
						map.getSegmentX(position.getX()),
						map.getSegmentY(position.getY())));

		// If this particular entity is a player, must remove them from segment
		// players as well
		if (groupManager.inInGroup(entity, Constants.GROUP_PLAYERS)) {
			groupManager.remove(
					entity,
					String.format(Constants.GROUP_MAP_PLAYERS,
							position.getMap()));
		}
	}

	/**
	 * This warps a specific entity to a given map and position, taking care of
	 * leaving the entity's old map if there was one.
	 * 
	 * @param entity
	 * @param mapId
	 * @param x
	 * @param y
	 */
	public void warpToMap(Entity entity, int mapId, int x, int y) {
		// Check to make sure the session isn't currently waiting for a map.

		if (!baseServer.getWorld().getManager(GroupManager.class)
				.inInGroup(entity, Constants.GROUP_PLAYERS)
				|| !baseServer.getServerSessionManager()
						.getEntitySession(entity).isWaitingForMap()) {

			leaveMap(entity);
		}

		joinMap(entity, mapId, x, y);
	}

	/**
	 * This notifies a session that their map should be refreshed.
	 * 
	 * @param session
	 *            the session to notify.
	 */
	public void refreshMap(ServerSession session) {
		// Send the player the new map info and then we await for the need map
		session.setWaitingForMap(true);
		baseServer.sendPacket(new ClientNewMapPacket(session));
	}

}
