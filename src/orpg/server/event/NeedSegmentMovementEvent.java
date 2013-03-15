package orpg.server.event;

import com.artemis.Entity;

import orpg.server.BaseServer;
import orpg.server.ServerSession;
import orpg.server.data.SessionType;
import orpg.server.net.packets.ClientSegmentDataPacket;
import orpg.server.systems.MovementSystem;
import orpg.shared.data.Map;
import orpg.shared.data.Segment;
import orpg.shared.data.component.Position;

public class NeedSegmentMovementEvent implements MovementEvent {

	private ServerSession session;
	private int mapId;
	private short segmentX;
	private short segmentY;
	private int revision;
	private long revisionTime;

	public NeedSegmentMovementEvent(ServerSession session, int mapId,
			short segmentX, short segmentY, int revision, long revisionTime) {
		this.session = session;
		this.mapId = mapId;
		this.segmentX = segmentX;
		this.segmentY = segmentY;
		this.revision = revision;
		this.revisionTime = revisionTime;
	}

	@Override
	public void process(BaseServer baseServer,
			MovementSystem movementSystem) {
		if (session.getSessionType() != SessionType.GAME) {
			return;
		}

		Position position = baseServer.getWorld()
				.getMapper(Position.class).get(session.getEntity());

		// Drop the request if map id doesn't match
		if (position.getMap() != mapId) {
			return;
		}

		// Load the segment
		Segment segment = baseServer.getMapController().getSegment(mapId,
				segmentX, segmentY);

		// Match the revisions to see if we need to send

		boolean revisionsMatch = segment.revisionMatches(revision,
				revisionTime);

		// Send the segment to the player.
		baseServer.sendPacket(new ClientSegmentDataPacket(session, mapId,
				segment, revisionsMatch));

		// If the player was changing maps before, then we update.
		if (session.isWaitingForMap()) {
			session.setWaitingForMap(false);
		}

	}

}
