package orpg.client.data.store;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import orpg.client.BaseClient;
import orpg.shared.Constants;
import orpg.shared.data.Segment;
import orpg.shared.data.store.DataStoreException;
import orpg.shared.net.InputByteBuffer;
import orpg.shared.net.OutputByteBuffer;

public class FileDataStore implements DataStore {

	private BaseClient baseClient;

	public FileDataStore(BaseClient baseClient) {
		this.baseClient = baseClient;
	}

	private String getMapFileName(int mapId, int segmentX, int segmentY) {
		return Constants.CLIENT_DATA_PATH + "maps/map_" + mapId + "_"
				+ segmentX + "_" + segmentY + ".map";
	}

	@Override
	public Segment loadSegment(int mapId, int segmentX, int segmentY)
			throws DataStoreException {

		File segmentFile = new File(getMapFileName(mapId, segmentX, segmentY));

		if (!segmentFile.exists()) {
			return null;
		}

		try {
			InputByteBuffer in = new InputByteBuffer(segmentFile);
			return in.getSegment(false);
		} catch (IOException e) {
			throw new DataStoreException(e);
		}
	}

	@Override
	public void saveSegment(int mapId, Segment segment)
			throws DataStoreException {
		OutputByteBuffer out = new OutputByteBuffer();
		out.putSegment(segment, false);

		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(getMapFileName(mapId,
					segment.getX(), segment.getY()));
			fileOut.write(out.getBytes());
		} catch (IOException e) {
			throw new DataStoreException(e);
		} finally {
			if (fileOut != null) {
				try {
					fileOut.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
