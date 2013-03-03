package orpg.server.data.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import orpg.server.BaseServer;
import orpg.server.data.Account;
import orpg.server.data.store.DataStore;
import orpg.shared.data.AutoTileType;
import orpg.shared.data.store.DataStoreException;

public class AutoTileController {

	private BaseServer baseServer;
	private DataStore dataStore;
	private Map<Short, AutoTileType> autoTiles;

	public AutoTileController(BaseServer baseServer, DataStore dataStore) {
		this.baseServer = baseServer;
		this.dataStore = dataStore;
	}

	public boolean setup() {
		// Try to load the autotiles.
		try {
			this.autoTiles = dataStore.loadAutoTiles();
		} catch (DataStoreException e) {
			baseServer
					.getConfigManager()
					.getErrorLogger()
					.log(Level.SEVERE,
							"Could not load autotiles file. Reason: "
									+ e.getMessage());
			return false;
		}
		return true;

	}

	public AutoTileType get(short tile) {
		return this.autoTiles.get(tile);
	}

	public void update(short tile, AutoTileType type) {
		this.autoTiles.put(tile, type);
	}

	public Map<Short, AutoTileType> getAutoTiles() {
		return this.autoTiles;
	}

	public void remove(short tile) {
		this.autoTiles.remove(tile);
	}

	public synchronized void save() {
		try {
			dataStore.saveAutoTiles(autoTiles);
		} catch (DataStoreException e) {
			baseServer
					.getConfigManager()
					.getErrorLogger()
					.log(Level.SEVERE,
							"Could not save autotiles. Reason : "
									+ e.getMessage());
		}
	}
}
