package orpg.server.data.store;

import orpg.shared.data.Map;

public interface DataStore {

	/**
	 * This saves a map to the data store.
	 * 
	 * @param map
	 *            the map to save
	 * @throws DataStoreException
	 *             if the save could not be completed.
	 */
	public void saveMap(Map map) throws DataStoreException;

	/**
	 * This loads a map from the data store based on the id.
	 * 
	 * @param id
	 *            the id of the map to load
	 * @return the loaded map.
	 * @throws IllegalArgumentException
	 *             if there is no map with the id.
	 * @throws DataStoreException
	 *             if the load could not be completed.
	 */
	public Map loadMap(int id) throws IllegalArgumentException,
			DataStoreException;

}
