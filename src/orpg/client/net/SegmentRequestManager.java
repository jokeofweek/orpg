package orpg.client.net;

import java.util.HashSet;

import orpg.client.BaseClient;
import orpg.client.net.packets.NeedSegmentPacket;
import orpg.shared.data.AccountCharacter;
import orpg.shared.data.Map;
import orpg.shared.data.Segment;
import orpg.shared.data.store.DataStoreException;

public class SegmentRequestManager {

	private BaseClient baseClient;
	private HashSet<String> pendingRequests;
	private Object handlingRequestLock;

	public SegmentRequestManager(BaseClient baseClient) {
		this.baseClient = baseClient;
		this.pendingRequests = new HashSet<String>();
		this.handlingRequestLock = new Object();
	}

	public void requestSegment(short x, short y) {
		// Make sure the segment is valid
		if (x < 0 || y < 0 || x >= baseClient.getMap().getSegmentsWide()
				|| y >= baseClient.getMap().getSegmentsHigh()) {
			return;
		}

		// If we've already requested the segment, don't do anything
		String requestKey = dataToRequestKey(baseClient.getMap().getId(),
				x, y);
		if (!this.pendingRequests.contains(requestKey)) {
			// Make sure we don't already have the segment
			if (baseClient.getMap().getSegment(x, y) == null) {

				// Load the segment into the local map if possible
				int revision = 0;
				long revisionTime = 0l;
				try {
					Segment segment = baseClient
							.getDataStore()
							.loadSegment(baseClient.getMap().getId(), x, y);
					if (segment != null) {
						baseClient.getLocalMap().updateSegment(segment,
								false);
						revision = segment.getRevision();
						revisionTime = segment.getRevisionTime();
					}
				} catch (DataStoreException e) {
				}

				synchronized (handlingRequestLock) {
					this.pendingRequests.add(requestKey);
					this.baseClient.sendPacket(new NeedSegmentPacket(
							baseClient.getMap().getId(), x, y, revision,
							revisionTime));
				}
			}
		}
	}

	public void receivedResponse(int mapId, Segment segment) {
		synchronized (handlingRequestLock) {
			// Notify observers, and then remove the request key
			pendingRequests.remove(dataToRequestKey(mapId, segment.getX(),
					segment.getY()));
		}
	}

	public void reset() {
		synchronized (handlingRequestLock) {
			pendingRequests.clear();
		}
	}

	private String dataToRequestKey(int mapId, int x, int y) {
		return mapId + ":" + x + ":" + y;
	}

	public void requestSurroundingSegments(int x, int y) {
		Map map = baseClient.getMap();
		short segmentX = map.getSegmentX(x);
		short segmentY = map.getSegmentX(y);
		requestSurroundingSegmentsOfSegment(segmentX, segmentY);
	}

	public void requestSurroundingSegmentsOfSegment(short segmentX,
			short segmentY) {
		requestSegment((short) (segmentX - 1), segmentY);
		requestSegment((short) (segmentX + 1), segmentY);
		requestSegment(segmentX, (short) (segmentY - 1));
		requestSegment(segmentX, (short) (segmentY + 1));

		requestSegment((short) (segmentX - 1), (short) (segmentY - 1));
		requestSegment((short) (segmentX - 1), (short) (segmentY + 1));
		requestSegment((short) (segmentX + 1), (short) (segmentY - 1));
		requestSegment((short) (segmentX + 1), (short) (segmentY + 1));
	}
}
