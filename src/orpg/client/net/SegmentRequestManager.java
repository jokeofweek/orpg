package orpg.client.net;

import java.util.HashSet;

import orpg.client.BaseClient;
import orpg.client.net.packets.NeedSegmentPacket;
import orpg.shared.data.AccountCharacter;
import orpg.shared.data.Map;
import orpg.shared.data.Segment;

public class SegmentRequestManager {

	private BaseClient baseClient;
	private HashSet<String> pendingRequests;
	private Object handlingRequestLock;

	public SegmentRequestManager(BaseClient baseClient) {
		this.baseClient = baseClient;
		this.pendingRequests = new HashSet<String>();
		this.handlingRequestLock = new Object();
	}

	public void requestSegment(int x, int y) {
		// Make sure the segment is valid
		if (x < 0 || y < 0 || x >= baseClient.getMap().getSegmentsWide()
				|| y >= baseClient.getMap().getSegmentsHigh()) {
			return;
		}

		// If we've already requested the segment, don't do anything
		String requestKey = dataToRequestKey(baseClient.getMap().getId(), x, y);
		if (!this.pendingRequests.contains(requestKey)) {
			// Make sure we don't already have the segment
			if (baseClient.getMap().getSegment(x, y) == null) {
				synchronized (handlingRequestLock) {
					this.pendingRequests.add(requestKey);
					this.baseClient.sendPacket(new NeedSegmentPacket(baseClient
							.getMap().getId(), x, y));
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

	public void requestSurroundingSegments(AccountCharacter character) {
		Map map = character.getMap();
		int segmentX = map.getSegmentX(character.getX());
		int segmentY = map.getSegmentX(character.getY());

		requestSegment(segmentX - 1, segmentY);
		requestSegment(segmentX + 1, segmentY);
		requestSegment(segmentX, segmentY - 1);
		requestSegment(segmentX, segmentY + 1);

		requestSegment(segmentX - 1, segmentY - 1);
		requestSegment(segmentX - 1, segmentY + 1);
		requestSegment(segmentX + 1, segmentY - 1);
		requestSegment(segmentX + 1, segmentY + 1);
	}
}
