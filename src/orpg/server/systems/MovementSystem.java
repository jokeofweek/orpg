package orpg.server.systems;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import orpg.client.BaseClient;
import orpg.client.net.packets.MoveRequestPacket;
import orpg.server.BaseServer;
import orpg.server.data.components.EventProcessor;
import orpg.server.event.MovementEvent;
import orpg.shared.Constants;
import orpg.shared.data.Direction;
import orpg.shared.data.Map;
import orpg.shared.data.component.IsPlayer;
import orpg.shared.data.component.Moveable;
import orpg.shared.data.component.Position;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.managers.GroupManager;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;

public class MovementSystem extends EntityProcessingSystem {

	private static final int EVENTS_PER_CYCLE = 20;

	private BaseServer baseServer;
	private GroupManager groupManager;
	private ConcurrentLinkedQueue<MovementEvent> events;
	private ComponentMapper<Position> positionMapper;

	public MovementSystem(BaseServer baseServer) {
		super(Aspect.getAspectForAll(EventProcessor.class));
		this.baseServer = baseServer;
		this.groupManager = baseServer.getWorld()
				.getManager(GroupManager.class);
		this.positionMapper = baseServer.getWorld().getMapper(Position.class);
		this.events = new ConcurrentLinkedQueue<MovementEvent>();
	}

	public void addEvent(MovementEvent event) {
		this.events.add(event);
	}

	public void updateEntitySegment(Entity entity, int oldMapId, int oldX,
			int oldY) {
		Position position = positionMapper.getSafe(entity);

		if (position == null) {
			return;
		}

		boolean isPlayer = groupManager.inInGroup(entity,
				Constants.GROUP_PLAYERS);

		boolean requiresMapJoin = false;
		boolean requiresSegmentJoin = false;

		Map oldMap = baseServer.getMapController().get(oldMapId);
		Map map = baseServer.getMapController().get(position.getMap());

		// If we changed maps, remove our old groups
		if (map != oldMap) {
			groupManager.remove(entity,
					String.format(Constants.GROUP_MAP, oldMapId));
			groupManager
					.remove(entity, String.format(Constants.GROUP_SEGMENT,
							oldMapId, oldMap.getSegmentX(oldX),
							oldMap.getSegmentY(oldY)));

			// If this particular entity is a player, must remove them from
			// segment players as well
			if (isPlayer) {
				groupManager.remove(
						entity,
						String.format(Constants.GROUP_MAP_PLAYERS,
								position.getMap()));
			}

			// Requires both joins
			requiresMapJoin = true;
			requiresSegmentJoin = true;
		} else if (map.getPositionSegment(position.getX(), position.getY()) != map
				.getPositionSegment(oldX, oldY)) {
			// If we changed segments, must remove the group segment group
			groupManager
					.remove(entity, String.format(Constants.GROUP_SEGMENT,
							oldMapId, oldMap.getSegmentX(oldX),
							oldMap.getSegmentY(oldY)));

			// Just require a segment join
			requiresSegmentJoin = true;
		}

		if (requiresMapJoin) {
			groupManager.add(entity,
					String.format(Constants.GROUP_MAP, map.getId()));

			if (isPlayer) {
				groupManager
						.add(entity,
								String.format(Constants.GROUP_MAP_PLAYERS,
										map.getId()));
			}
		}

		if (requiresSegmentJoin) {
			groupManager.add(
					entity,
					String.format(Constants.GROUP_SEGMENT, map.getId(),
							map.getSegmentX(position.getX()),
							map.getSegmentY(position.getY())));
		}
	}

	@Override
	protected void process(Entity e) {
		int remaining = EVENTS_PER_CYCLE;
		MovementEvent event;
		while (remaining >= 0 && (event = events.poll()) != null) {
			event.process(baseServer, this);
			remaining--;
		}
	}

}
