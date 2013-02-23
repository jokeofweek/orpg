package orpg.editor.controller;

import java.util.HashSet;
import java.util.Observable;

import orpg.editor.BaseEditor;
import orpg.editor.net.packets.EditorRequestSegmentPacket;
import orpg.shared.data.Map;
import orpg.shared.data.Segment;

public class EditorSegmentRequestManager extends Observable {

	private BaseEditor baseEditor;
	private Map map;
	private HashSet<String> pendingRequests;
	private Object handlingRequestLock;

	public EditorSegmentRequestManager(BaseEditor baseEditor, Map map) {
		this.baseEditor = baseEditor;
		this.pendingRequests = new HashSet<String>();
		this.map = map;
		this.handlingRequestLock = new Object();
	}

	public void requestSegment(int x, int y) {
		// If we've already requested the segment, don't do anything
		String requestKey = dataToRequestKey(map.getId(), x, y);
		if (!this.pendingRequests.contains(requestKey)) {
			// Make sure we don't already have the segment
			if (map.getSegment(x, y) == null) {
				synchronized (handlingRequestLock) {
					this.pendingRequests.add(requestKey);
					//this.baseEditor.sendPacket(new EditorRequestSegmentPacket(
					//		map.getId(), x, y));
				}
			}
		}
	}

	public void receivedResponse(Segment segment) {
		synchronized (handlingRequestLock) {
			// Notify observers, and then remove the request key
			setChanged();
			notifyObservers(segment);

			pendingRequests.remove(dataToRequestKey(map.getId(),
					segment.getX(), segment.getY()));
		}
	}

	private String dataToRequestKey(int mapId, int x, int y) {
		return mapId + ":" + x + ":" + y;
	}
}
