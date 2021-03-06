package orpg.server.data.store;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.logging.Level;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;

import orpg.server.BaseServer;
import orpg.server.data.Account;
import orpg.shared.Constants;
import orpg.shared.data.AccountCharacter;
import orpg.shared.data.AutoTileType;
import orpg.shared.data.ChatChannel;
import orpg.shared.data.Direction;
import orpg.shared.data.Map;
import orpg.shared.data.Segment;
import orpg.shared.data.Validator;
import orpg.shared.data.store.DataStoreException;
import orpg.shared.net.serialize.InputByteBuffer;
import orpg.shared.net.serialize.OutputByteBuffer;

public class FileDataStore implements DataStore {

	private BaseServer baseServer;

	private static final String CHARACTERS_FILE = Constants.SERVER_ACCOUNTS_PATH
			+ "characters.txt";

	private HashSet<String> characterNames;

	private IDFile accountIDFile;

	public FileDataStore(BaseServer baseSever) {
		this.baseServer = baseSever;
		this.loadCharacterNames();
	}

	@Override
	public boolean setup() {
		// Load the account ID file
		File file = new File(Constants.SERVER_DATA_PATH + "accounts/account.id");
		try {
			this.accountIDFile = new IDFile(file);
		} catch (IOException e) {
			baseServer
					.getConfigManager()
					.getErrorLogger()
					.log(Level.SEVERE,
							"Error creating accounts ID file. Reason: "
									+ e.getMessage());
			return false;
		}

		// Create the autotile file if it doesn't already exist.
		File autotilesFile = new File(Constants.SERVER_DATA_PATH
				+ "autotiles.ini");
		if (!autotilesFile.exists()) {
			try {
				autotilesFile.createNewFile();
				Wini ini = new Wini(autotilesFile);
				ini.put("count", "count", 0);
				ini.store();
			} catch (IOException e) {
				baseServer
						.getConfigManager()
						.getErrorLogger()
						.log(Level.SEVERE,
								"Could not create autotiles file. Reason: "
										+ e.getMessage());
			}
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see orpg.server.data.store.DataStore#shutdown()
	 */
	@Override
	public void shutdown() {
		// Nothing to do for file data store
	}

	private void loadCharacterNames() {
		this.characterNames = new HashSet<String>();

		Scanner in = null;

		try {
			in = new Scanner(new File(CHARACTERS_FILE));
			while (in.hasNext()) {
				characterNames.add(in.nextLine());
			}
		} catch (FileNotFoundException e) {
			baseServer
					.getConfigManager()
					.getErrorLogger()
					.log(Level.WARNING,
							"No character names file found. Creating blank file at path "
									+ CHARACTERS_FILE + ".");
			try {
				new File(CHARACTERS_FILE).createNewFile();
			} catch (IOException e1) {
				baseServer
						.getConfigManager()
						.getErrorLogger()
						.log(Level.SEVERE,
								"Could not create characters file. Cannot proceed safely, shutting down server.");
				System.exit(1);
			}
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see orpg.server.data.store.DataStore#mapExists(int)
	 */
	@Override
	public boolean mapExists(int id) {
		File mapFile = new File(Constants.SERVER_MAPS_PATH + "map_" + id
				+ ".map");
		return (id >= 1 && id <= baseServer.getConfigManager().getTotalMaps() && mapFile
				.exists());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see orpg.server.data.store.DataStore#saveMap(orpg.shared.data.Map)
	 */
	@Override
	public void saveMap(Map map) throws DataStoreException {
		OutputByteBuffer buffer = new OutputByteBuffer();
		buffer.putMapDescriptor(map);
		BufferedOutputStream out = null;

		try {
			out = new BufferedOutputStream(new FileOutputStream(
					Constants.SERVER_MAPS_PATH + "map_" + map.getId() + ".map"));
			out.write(buffer.getBytes());
			out.close();

			for (short x = 0; x < map.getSegmentsWide(); x++) {
				for (short y = 0; y < map.getSegmentsHigh(); y++) {
					if (map.getSegment(x, y) != null) {
						buffer.reset();
						buffer.putSegment(map.getSegment(x, y));
						out = new BufferedOutputStream(
								new FileOutputStream(String.format(
										Constants.SERVER_MAPS_PATH
												+ "map_%d_%d_%d.map",
										map.getId(), x, y)));
						out.write(buffer.getBytes());
						out.close();
					}
				}
			}
		} catch (IOException e) {
			throw new DataStoreException(e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see orpg.server.data.store.DataStore#loadMap(int)
	 */
	@Override
	public Map loadMap(int id, boolean loadSegments)
			throws IllegalArgumentException, DataStoreException {
		File mapFile = new File(Constants.SERVER_MAPS_PATH + "map_" + id
				+ ".map");
		if (!mapExists(id)) {
			throw new IllegalArgumentException("No map exists with the number "
					+ id + ".");
		}

		try {
			// First load the descriptor by reading the file to completion.
			InputByteBuffer in = new InputByteBuffer(mapFile);
			Map map = in.getMapDescriptor();

			if (loadSegments) {
				// Load each segment
				for (int x = 0; x < map.getSegmentsWide(); x++) {
					for (int y = 0; y < map.getSegmentsHigh(); y++) {
						map.updateSegment(new InputByteBuffer(new File(
								Constants.SERVER_MAPS_PATH + "map_" + id + "_"
										+ x + "_" + y + ".map")).getSegment(),
								false);
					}
				}
			}
			return map;
		} catch (IOException e) {
			throw new DataStoreException(e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see orpg.server.data.store.DataStore#segmentExists(int, int, int)
	 */
	@Override
	public boolean segmentExists(int id, int x, int y) {
		return (new File(Constants.SERVER_MAPS_PATH + "map_" + id + "_" + x
				+ "_" + y + ".map").exists());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see orpg.server.data.store.DataStore#loadSegment(int, int, int)
	 */
	@Override
	public Segment loadSegment(int id, int x, int y)
			throws IllegalArgumentException, IndexOutOfBoundsException,
			DataStoreException {
		// First make sure the map exists
		if (!mapExists(id)) {
			throw new IllegalArgumentException("No map exists with the number "
					+ id + ".");
		}

		// Next make sure the segment exists
		if (!segmentExists(id, x, y)) {
			throw new IndexOutOfBoundsException("No segment for map " + id
					+ " at segment position (" + x + "," + y + ")");
		}

		// Try to load the segment
		try {
			Segment segment = new InputByteBuffer(new File(
					Constants.SERVER_MAPS_PATH + "map_" + id + "_" + x + "_"
							+ y + ".map")).getSegment();
			return segment;
		} catch (IOException e) {
			throw new DataStoreException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see orpg.server.data.store.DataStore#accountExists(java.lang.String)
	 */
	@Override
	public boolean accountExists(String name) {
		return getAccountFile(name).exists();
	}

	private File getAccountFile(String accountName) {
		return new File(Constants.SERVER_ACCOUNTS_PATH + accountName + ".ini");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * orpg.server.data.store.DataStore#createAccount(orpg.server.data.Account)
	 */
	@Override
	public synchronized void createAccount(Account account)
			throws IllegalArgumentException, DataStoreException {
		// Just a last condition check to prevent messing up the file system.
		if (!Validator.isAccountNameValid(account.getName())) {
			throw new IllegalArgumentException("Invalid account name.");
		}

		try {
			account.setId(accountIDFile.getNextID());
		} catch (IOException e) {
			throw new DataStoreException(e);
		}

		File accountFile = getAccountFile(account.getName());
		try {
			accountFile.createNewFile();
		} catch (IOException e) {
			throw new DataStoreException(e);
		}

		this.saveAccount(account);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * orpg.server.data.store.DataStore#saveAccount(orpg.server.data.Account)
	 */
	@Override
	public void saveAccount(Account account) throws DataStoreException {
		synchronized (account) {
			try {
				Wini ini = new Wini(getAccountFile(account.getName()));

				// Store account details
				ini.put("account", "id", account.getId());
				ini.put("account", "name", account.getName());
				ini.put("account", "email", account.getEmail());
				ini.put("account", "salt", account.getSalt());
				ini.put("account", "hash", account.getPasswordHash());
				ini.put("account", "chars", account.getCharacters().size());

				int charPos = 0;
				for (AccountCharacter character : account.getCharacters()) {
					saveAccountCharacter(ini, charPos++, character);
				}

				ini.store();
			} catch (IOException e) {
				throw new DataStoreException(e);
			}
		}
	}

	private void saveAccountCharacter(Wini ini, int charPos,
			AccountCharacter character) {
		String key = "char" + charPos;

		ini.put(key, "id", character.getId());
		ini.put(key, "name", character.getName());
		ini.put(key, "map", character.getMap().getId());
		ini.put(key, "x", character.getX());
		ini.put(key, "y", character.getY());
		ini.put(key, "dir", character.getDirection().ordinal());
		ini.put(key, "sprite", character.getSprite());

		// Save the chat channels
		Set<ChatChannel> subscriptions = character
				.getChatChannelSubscriptions();
		for (ChatChannel channel : ChatChannel.values()) {
			ini.put(key, "channel_" + channel,
					subscriptions.contains(channel) ? 1 : 0);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see orpg.server.data.store.DataStore#loadAccount(java.lang.String)
	 */
	@Override
	public synchronized Account loadAccount(String name)
			throws IllegalArgumentException, DataStoreException {
		// Test for existence
		if (!accountExists(name)) {
			throw new IllegalArgumentException("Account does not exist.");
		}

		try {
			Wini ini = new Wini(getAccountFile(name));

			Account account = new Account();
			account.setId(ini.get("account", "id", int.class));
			account.setName(ini.get("account", "name"));
			account.setEmail(ini.get("account", "email"));
			account.setSalt(ini.get("account", "salt"));
			account.setPasswordHash(ini.get("account", "hash"));

			int totalChars = ini.get("account", "chars", int.class);
			List<AccountCharacter> characters = new ArrayList<AccountCharacter>(
					totalChars);
			for (int i = 0; i < totalChars; i++) {
				characters.add(loadAccountCharacter(ini, i));
			}
			account.setCharacters(characters);

			return account;

		} catch (IOException e) {
			throw new DataStoreException(e);
		}

	}

	private AccountCharacter loadAccountCharacter(Wini ini, int charPos) {
		String key = "char" + charPos;
		AccountCharacter character = new AccountCharacter();

		character.setId(ini.get(key, "id", int.class));
		character.setName(ini.get(key, "name"));
		character.setMap(baseServer.getMapController().get(
				ini.get(key, "map", int.class)));
		character.setX(ini.get(key, "x", int.class));
		character.setY(ini.get(key, "y", int.class));
		character.setDirection(Direction.values()[ini
				.get(key, "dir", int.class)]);
		character.setSprite(ini.get(key, "sprite", short.class));

		// Load set of channel subscriptions
		Set<ChatChannel> subscriptions = new HashSet<ChatChannel>();
		for (ChatChannel channel : ChatChannel.values()) {
			if (ini.get(key, "channel_" + channel, int.class) == 1) {
				subscriptions.add(channel);
			}
		}
		character.setChatChannelSubscriptions(subscriptions);

		return character;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see orpg.server.data.store.DataStore#characterExists(java.lang.String)
	 */
	@Override
	public boolean characterExists(String name) {
		return characterNames.contains(name);
	}

	@Override
	public void createCharacter(Account owner, AccountCharacter character)
			throws IllegalArgumentException, DataStoreException {
		// Validate the character name
		if (!Validator.isCharacterNameValid(character.getName())) {
			throw new IllegalArgumentException("Invalid character name.");
		}

		// Add to the file first in case of some kind of failure to prevent
		// missing out
		FileWriter out = null;
		PrintWriter printOut = null;
		try {
			out = new FileWriter(CHARACTERS_FILE, true);
			printOut = new PrintWriter(new BufferedWriter(out));
			printOut.println(character.getName());
		} catch (IOException e) {
			baseServer
					.getConfigManager()
					.getErrorLogger()
					.log(Level.SEVERE,
							"Could not add character name to character file. Reason: "
									+ e.getMessage());
			throw new DataStoreException("Error registering character name.", e);
		} finally {
			if (printOut != null) {
				printOut.close();
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}

		owner.getCharacters().add(character);

		// Add the name of the character to the list of character names
		this.characterNames.add(character.getName());

		// Save the account
		saveAccount(owner);

	}

	private static class IDFile {
		private File file;

		public IDFile(File file) throws IOException {
			// Test to make sure the file exists, if not create and write a 1
			if (!file.exists()) {
				file.createNewFile();
				FileWriter out = null;
				try {
					out = new FileWriter(file);
					out.write("1");
				} finally {
					if (out != null) {
						out.close();
					}
				}
			}

			this.file = file;
		}

		public synchronized int getNextID() throws IOException {
			Scanner scan = null;
			int id;
			try {
				scan = new Scanner(file);
				id = scan.nextInt();
				FileWriter out = null;
				try {
					out = new FileWriter(file);
					out.write((id + 1) + "");
				} finally {
					if (out != null) {
						out.close();
					}
				}
			} catch (IOException e) {
				throw e;
			} finally {
				scan.close();
				if (scan != null) {
					scan.close();
				}
			}
			return id;
		}
	}

	@Override
	public java.util.Map<Short, AutoTileType> loadAutoTiles()
			throws DataStoreException {
		HashMap<Short, AutoTileType> autotiles = null;

		try {
			File autotilesFile = new File(Constants.SERVER_DATA_PATH
					+ "autotiles.ini");
			Wini ini = new Wini(autotilesFile);
			int count = ini.get("count", "count", int.class);
			autotiles = new HashMap<Short, AutoTileType>(count);

			for (int i = 0; i < count; i++) {
				autotiles.put(ini.get("entry" + i, "tile", short.class),
						AutoTileType.values()[ini.get("entry" + i, "type",
								int.class)]);
			}
		} catch (IOException e) {
			throw new DataStoreException(e);
		}

		return autotiles;
	}

	@Override
	public void saveAutoTiles(java.util.Map<Short, AutoTileType> autotiles)
			throws DataStoreException {
		try {

			File autotilesFile = new File(Constants.SERVER_DATA_PATH
					+ "autotiles.ini");
			Wini ini = new Wini(autotilesFile);
			ini.clear();

			ini.put("count", "count", autotiles.size());
			int i = 0;
			for (Entry<Short, AutoTileType> entry : autotiles.entrySet()) {
				ini.put("entry" + i, "tile", entry.getKey());
				ini.put("entry" + i, "type", entry.getValue().ordinal());
				i++;
			}

			ini.store();
		} catch (IOException e) {
			throw new DataStoreException(e);
		}

	}
}
