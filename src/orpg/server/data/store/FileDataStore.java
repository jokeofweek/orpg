package orpg.server.data.store;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import orpg.server.BaseServer;
import orpg.shared.Constants;
import orpg.shared.data.Map;
import orpg.shared.net.InputByteBuffer;
import orpg.shared.net.OutputByteBuffer;

public class FileDataStore implements DataStore {

	private BaseServer baseServer;

	public FileDataStore(BaseServer baseSever) {
		this.baseServer = baseSever;
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
					Constants.SERVER_MAPS_PATH + "map_" + map.getId()
							+ ".map"));
			out.write(buffer.getBytes());
			out.close();

			for (int x = 0; x < map.getSegmentsWide(); x++) {
				for (int y = 0; y < map.getSegmentsHigh(); y++) {
					buffer.reset();
					buffer.putSegment(map.getSegment(x, y));
					out = new BufferedOutputStream(new FileOutputStream(
							String.format(Constants.SERVER_MAPS_PATH
									+ "map_%d_%d_%d.map", map.getId(), x,
									y)));
					out.write(buffer.getBytes());
					out.close();
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
	public Map loadMap(int id) throws IllegalArgumentException,
			DataStoreException {
		File mapFile = new File(Constants.SERVER_MAPS_PATH + "map_" + id
				+ ".map");
		if (id < 1 || id > baseServer.getConfigManager().getTotalMaps()
				|| !mapFile.exists()) {
			throw new IllegalArgumentException(
					"No map exists with the number " + id + ".");
		}

		try {
			// First load the descriptor by reading the file to completion.
			InputByteBuffer in = new InputByteBuffer(mapFile);
			Map map = in.getMapDescriptor();

			// Load each segment
			for (int x = 0; x < map.getSegmentsWide(); x++) {
				for (int y = 0; y < map.getSegmentsHigh(); y++) {
					map.updateSegment(new InputByteBuffer(new File(
							Constants.SERVER_MAPS_PATH + "map_" + id + "_"
									+ x + "_" + y + ".map")).getSegment());
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
	 * @see orpg.server.data.store.DataStore#accountExists(java.lang.String)
	 */
	@Override
	public boolean accountExists(String name) {
		return new File(Constants.SERVER_ACCOUNTS_PATH + name + ".ini")
				.exists();
	}

}
