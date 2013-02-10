package orpg.server.data;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import orpg.shared.Constants;
import orpg.shared.data.Map;
import orpg.shared.net.OutputByteBuffer;

public class FileSystem {

	public static void save(Map map) throws IOException {
		OutputByteBuffer buffer = new OutputByteBuffer();
		buffer.putInt(map.getId());
		buffer.putShort(map.getSegmentWidth());
		buffer.putShort(map.getSegmentHeight());
		buffer.putShort(map.getSegmentsWide());
		buffer.putShort(map.getSegmentsHigh());

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
}
