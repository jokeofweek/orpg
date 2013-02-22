package orpg.server.data.managers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import orpg.server.BaseServer;
import orpg.server.data.store.DataStoreException;
import orpg.shared.Constants;
import orpg.shared.data.Map;
import orpg.shared.data.Pair;
import orpg.shared.net.InputByteBuffer;

public class MapManager implements Manager<Map> {

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
				emptyMapTemplate = new Map(i + 1, (short) 3, (short) 3,
						true);
				try {
					baseServer.getDataStore().saveMap(emptyMapTemplate);
				} catch (DataStoreException e) {
					baseServer
							.getConsole()
							.out()
							.println(
									"Could not create empty map "
											+ (i + 1) + ". Reason: "
											+ e.getMessage());
					return false;
				}
				this.maps[i] = emptyMapTemplate;

			} else {
				// The file exists, so load the map
				try {
					this.maps[i] = baseServer.getDataStore()
							.loadMap(i + 1);
				} catch (DataStoreException e) {
					baseServer
							.getConsole()
							.out()
							.println(
									"Could not load map " + (i + 1)
											+ ". Reason: "
											+ e.getMessage());
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
	public Map get(int id) {
		if (id <= 0 || id > baseServer.getConfigManager().getTotalMaps()) {
			throw new IllegalArgumentException("No map with number " + id);
		}

		return maps[id - 1];
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

	public void update(Map map) {
		if (map.getId() <= 0
				|| map.getId() > baseServer.getConfigManager()
						.getTotalMaps()) {
			throw new IllegalArgumentException(
					"Tried to update map with non-existant number "
							+ map.getId());
		}
		this.maps[map.getId() - 1] = map;
	}
}
