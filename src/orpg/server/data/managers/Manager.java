package orpg.server.data.managers;

public interface Manager<K> {

	/**
	 * This attemps to load the data for the manager.
	 * 
	 * @return true if the manager was succesfully loaded.
	 */
	public boolean load();

}
