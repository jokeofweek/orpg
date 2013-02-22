package orpg.server.data.managers;

public interface Manager<K> {

	/**
	 * This attemps to do any initial loading for the manager.
	 * 
	 * @return true if the manager was succesfully setup.
	 */
	public boolean setup();

	/**
	 * This fetches the given ID tagged object from the manager.
	 * 
	 * @param id
	 *            the id to use.
	 * @return the object with that ID.
	 * @throws IllegalArgumentException
	 *             if no object exists with that id
	 */
	public K get(int id) throws IllegalArgumentException;

	public void update(K obj);
}
