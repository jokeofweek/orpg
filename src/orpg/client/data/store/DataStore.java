package orpg.client.data.store;

import orpg.shared.data.Segment;
import orpg.shared.data.store.DataStoreException;

public interface DataStore {

	public Segment loadSegment(int mapId, int segmentX, int segmentY)
			throws DataStoreException;

	public void saveSegment(int mapId, Segment segment)
			throws DataStoreException;

}
