package orpg.server.data.store;

import orpg.server.data.Account;
import orpg.shared.data.AccountCharacter;
import orpg.shared.data.Map;
import orpg.shared.data.Segment;

/**
 * @author Dom
 * 
 */
/**
 * @author Dom
 * 
 */
public interface DataStore {

	/**
	 * Checks whether a map exists with a given id.
	 * 
	 * @param id
	 *            the id of the map.
	 * @return true if there is a map with passed id.
	 */
	public boolean mapExists(int id);

	/**
	 * Checks whether a segment exists for a given map. Note that this does not
	 * distinguish between whether the <b>map</b> exists and the <b>segment</b>
	 * exists, so if the map does not exist false will be returned.
	 * 
	 * @param id
	 *            the id of the map.
	 * @param x
	 *            the x position of the segment
	 * @param y
	 *            the y position of the segment
	 * @return true if the segment exists.
	 */
	public boolean segmentExists(int id, int x, int y);

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
	 * @param loadSegments
	 *            whether segments should be loaded, or left null to be lazily
	 *            loaded when needed.
	 * @return the loaded map.
	 * @throws IllegalArgumentException
	 *             if there is no map with the id.
	 * @throws DataStoreException
	 *             if the load could not be completed.
	 */
	public Map loadMap(int id, boolean loadSegments)
			throws IllegalArgumentException, DataStoreException;

	/**
	 * This loads a map segment from the data stored based on the id.
	 * 
	 * @param id
	 *            the id of the map
	 * @param x
	 *            the segment's x value
	 * @param y
	 *            the segment's y value
	 * @return the map segment.
	 * @throws IllegalArgumentException
	 *             if there is no map with that id
	 * @throws IndexOutOfBoundsException
	 *             if the map does not have a segment with that position.
	 * @throws DataStoreException
	 *             if the load could not be completed
	 */
	public Segment loadSegment(int id, int x, int y)
			throws IllegalArgumentException, IndexOutOfBoundsException,
			DataStoreException;

	/**
	 * Determines whether an account exists with a given user name.
	 * 
	 * @param name
	 *            the name of the account
	 * @return true if there is an account in the data store with that name,
	 *         else false.
	 */
	public boolean accountExists(String name);

	/**
	 * This creates an account and calls {@link #saveAccount(Account)}
	 * afterwards. <b>Note that this method assumes
	 * {@link #accountExists(String)} has already been called.</b> The reasoning
	 * behind this is to make sure it gets called regardless of data store
	 * implementation.
	 * 
	 * @param account
	 *            the account to save.
	 * @throws IllegalArgumentException
	 *             if the account name is not valid.
	 * @throws DataStoreException
	 *             if there is an error while creating said account.
	 */
	public void createAccount(Account account) throws IllegalArgumentException,
			DataStoreException;

	/**
	 * This saves an account's status.
	 * 
	 * @param account
	 *            the account to save
	 * @throws DataStoreException
	 *             if there is an error saving said account.
	 */
	public void saveAccount(Account account) throws DataStoreException;

	/**
	 * This determines whether there exists a character with a given name.
	 * 
	 * @param name
	 *            the name to check
	 * @return true if there is an active character with that name.
	 */
	public boolean characterExists(String name);

	/**
	 * This creates a character and associates it with a given account. It then
	 * saves the entire account via {@link #saveAccount(Account)}. <b>Note that
	 * this method assumes {@link #characterExists(String)} has already been
	 * called.</b> The reasoning behind this is to make sure it gets called
	 * regardless of data store implementation.
	 * 
	 * @param parent
	 *            the account which owns the character.
	 * @param character
	 *            the character to save.
	 * @throws IllegalArgumentException
	 *             if the character name is not valid.
	 * @throws DataStoreException
	 *             if there is an error while creating said character.
	 */
	public void createCharacter(Account parent, AccountCharacter character)
			throws IllegalArgumentException, DataStoreException;

	/**
	 * This attemps to load an account with a given name.
	 * 
	 * @param name
	 *            the name of the account to load.
	 * @return the {@link Account} with the given name.
	 * @throws IllegalArgumentException
	 *             if no account exists with that name.
	 * @throws DataStoreException
	 *             if there is an error loading that account from the datastore.
	 */
	public Account loadAccount(String name) throws IllegalArgumentException,
			DataStoreException;

}
