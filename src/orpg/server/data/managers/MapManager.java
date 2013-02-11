package orpg.server.data.managers;

import java.io.File;
import java.io.IOException;

import orpg.server.BaseServer;
import orpg.server.config.ConfigurationManager;
import orpg.server.data.FileSystem;
import orpg.shared.Constants;
import orpg.shared.data.Map;
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
	public boolean load() {
		this.maps = new Map[baseServer.getConfigManager().getTotalMaps()];

		// Load the maps, creating them if necessary
		Map emptyMapTemplate;
		File file;
		InputByteBuffer fileBuffer;
		for (int i = 0; i < maps.length; i++) {
			file = new File(Constants.SERVER_MAPS_PATH + "map_" + (i + 1) + ".map");
			if (!file.exists()) {
				// Save the map based on the empty template
				emptyMapTemplate = new Map(i + 1, (short) 1, (short) 1, true);
				try {
					FileSystem.save(emptyMapTemplate);
				} catch (IOException e) {
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
					fileBuffer = new InputByteBuffer(file);
					this.maps[i] = FileSystem.loadMap(i + 1);
				} catch (IOException e) {
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

	public Map getMap(int id) {
		if (id <= 0 || id > baseServer.getConfigManager().getTotalMaps()) {
			throw new IllegalArgumentException("No map with number " + id);
		}

		return maps[id - 1];
	}

	public Map[] getMaps() {
		return this.maps;
	}

	public void updateMap(Map map) {
		if (map.getId() <= 0
				|| map.getId() > baseServer.getConfigManager().getTotalMaps()) {
			throw new IllegalArgumentException(
					"Tried to update map with non-existant number "
							+ map.getId());
		}
		this.maps[map.getId() - 1] = map;
	}
}
