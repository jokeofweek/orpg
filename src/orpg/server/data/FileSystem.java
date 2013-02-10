package orpg.server.data;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import orpg.shared.Constants;
import orpg.shared.data.Map;
import orpg.shared.net.InputByteBuffer;
import orpg.shared.net.OutputByteBuffer;

public class FileSystem {

	public static void save(Map map) throws IOException {
		OutputByteBuffer buffer = new OutputByteBuffer();
		buffer.putMapDescriptor(map);
		BufferedOutputStream out = null;

		try {
			out = new BufferedOutputStream(new FileOutputStream(
					Constants.SERVER_MAPS_PATH + "map_" + map.getId() + ".map"));
			out.write(buffer.getBytes());
			out.close();

			for (int x = 0; x < map.getSegmentsWide(); x++) {
				for (int y = 0; y < map.getSegmentsHigh(); y++) {
					buffer.reset();
					buffer.putSegment(map.getSegment(x, y));
					out = new BufferedOutputStream(new FileOutputStream(
							String.format(Constants.SERVER_MAPS_PATH
									+ "map_%d_%d_%d.map", map.getId(), x, y)));
					out.write(buffer.getBytes());
					out.close();
				}
			}
		} catch (IOException e) {
			// If there was an exception, close the resource and then re-throw.
			if (out != null) {
				try {
					out.close();
				} catch (IOException n) {
					throw e;
				}
			}
			throw e;
		}
	}

	/**
	 * This loads a map, including all its segments.
	 * 
	 * @param number
	 *            the ID of the map to load.
	 * @return the loaded map.
	 * @throws IllegalArgumentException
	 *             if no map exists with that ID
	 * @throws IOException
	 *             if an error occurs while reading the file.
	 */
	public static Map loadMap(int number) throws IllegalArgumentException,
			IOException {
		File mapFile = new File(Constants.SERVER_MAPS_PATH + "map_" + number
				+ ".map");
		if (!mapFile.exists()) {
			throw new IllegalArgumentException("No map exists with the number "
					+ number + ".");
		}

		// First load the descriptor by reading the file to completion.
		Map map = new InputByteBuffer(mapFile).getMapDescriptor();

		// Load each segment
		for (int x = 0; x < map.getSegmentsWide(); x++) {
			for (int y = 0; y < map.getSegmentsHigh(); y++) {
				map.updateSegment(new InputByteBuffer(new File(
						Constants.SERVER_MAPS_PATH + "map_" + number + "_" + x
								+ "_" + y + ".map")).getSegment());
			}
		}
		
		return map;
	}
}
