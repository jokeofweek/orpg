package orpg.server.systems;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import orpg.client.BaseClient;
import orpg.client.net.packets.MoveRequestPacket;
import orpg.server.BaseServer;
import orpg.server.ServerSession;
import orpg.server.data.components.SystemEventProcessor;
import orpg.server.event.EventProcessor;
import orpg.server.event.MovementEvent;
import orpg.server.net.packets.ClientJoinMapPacket;
import orpg.server.net.packets.ClientLeftMapPacket;
import orpg.server.net.packets.ClientNewMapPacket;
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

public class MovementSystem extends EntityProcessingSystem implements EventProcessor<MovementEvent> {

	private static final int EVENTS_PER_CYCLE = 20;

	private BaseServer baseServer;
	private GroupManager groupManager;
	private ConcurrentLinkedQueue<MovementEvent> events;
	private ComponentMapper<Position> positionMapper;

	public MovementSystem(BaseServer baseServer) {
		super(Aspect.getAspectForAll(SystemEventProcessor.class));
		this.baseServer = baseServer;
		this.groupManager = baseServer.getWorld()
				.getManager(GroupManager.class);
		this.positionMapper = baseServer.getWorld().getMapper(Position.class);
		this.events = new ConcurrentLinkedQueue<MovementEvent>();
	}

	public void addEvent(MovementEvent event) {
		this.events.add(event);
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


	/**
	 * 
	 * This joins an entity to a given map, updating the entity's position.
	 * 
	 * @param entity
	 *            the entity to join
	 * @param mapId
	 *            the id of the map we are joining
	 * @param x
	 *            the x position on the map
	 * @param y
	 *            the y position on the map
	 * @throws IllegalArgumentException
	 *             if no map exists with that id
	 * @throws IndexOutOfBoundsException
	 *             if the x and y are out of bounds.
	 */
	public void joinMap(Entity entity, int mapId, int x, int y)
			throws IllegalArgumentException, IndexOutOfBoundsException {
		// Make sure the map is valid
		Map map = baseServer.getMapController().get(mapId);

		// Ensure the segment is loaded and spawn entities if necessary
		boolean needsSpawn = map.getPositionSegment(x, y) == null;

		baseServer.getMapController().getSegment(map.getId(),
				map.getSegmentX(x), map.getSegmentY(y));

		if (needsSpawn) {
			world.getSystem(MapProcessSystem.class).spawnSegmentEntities(mapId,
					map.getSegmentX(x), map.getSegmentY(y));
		}

		// Update the player
		Position position = positionMapper.get(entity);
		position.setMap(mapId);
		position.setX(x);
		position.setY(y);

		GroupManager groupManager = baseServer.getWorld().getManager(
				GroupManager.class);
		groupManager.add(entity,
				String.format(Constants.GROUP_MAP, position.getMap()));
		groupManager.add(
				entity,
				String.format(Constants.GROUP_SEGMENT, position.getMap(),
						map.getSegmentX(position.getX()),
						map.getSegmentY(position.getY())));

		// If this particular entity is a player, must remove them from segment
		// players as well
		ServerSession session = null;

		if (groupManager.inInGroup(entity, Constants.GROUP_PLAYERS)) {
			groupManager.add(
					entity,
					String.format(Constants.GROUP_MAP_PLAYERS,
							position.getMap()));

			session = baseServer.getServerSessionManager().getEntitySession(
					entity);

			if (session != null) {
				refreshMap(session);
			}
		}

		// Notify the other players that this player has joined the map
		baseServer.sendPacket(new ClientJoinMapPacket(session, map.getId(),
				entity));
	}

	/**
	 * This notifies an entity's given map that the entity is leaving said map.
	 * 
	 * @param entity
	 *            the entity that is leaving the map
	 */
	public void leaveMap(Entity entity) {
		// Remove from the old groups
		GroupManager groupManager = baseServer.getWorld().getManager(
				GroupManager.class);
		Position position = positionMapper.get(entity);

		// If it is a player, get the server session
		ServerSession session = null;
		if (groupManager.inInGroup(entity, Constants.GROUP_PLAYERS)) {
			session = baseServer.getServerSessionManager().getEntitySession(
					entity);
		}

		Map map = baseServer.getMapController().get(position.getMap());

		// Notify the other players that this player has left the map
		baseServer.sendPacket(new ClientLeftMapPacket(session, map.getId(),
				entity));

		groupManager.remove(entity,
				String.format(Constants.GROUP_MAP, position.getMap()));
		groupManager.remove(
				entity,
				String.format(Constants.GROUP_SEGMENT, position.getMap(),
						map.getSegmentX(position.getX()),
						map.getSegmentY(position.getY())));

		// If this particular entity is a player, must remove them from segment
		// players as well
		if (groupManager.inInGroup(entity, Constants.GROUP_PLAYERS)) {
			groupManager.remove(
					entity,
					String.format(Constants.GROUP_MAP_PLAYERS,
							position.getMap()));
		}
	}

	/**
	 * This notifies a session that their map should be refreshed.
	 * 
	 * @param session
	 *            the session to notify.
	 */
	public void refreshMap(ServerSession session) {
		// Send the player the new map info and then we await for the need map
		session.setWaitingForMap(true);
		baseServer.sendPacket(new ClientNewMapPacket(session));
	}

	/**
	 * This warps a specific entity to a given map and position, taking care of
	 * leaving the entity's old map if there was one.
	 * 
	 * @param entity
	 * @param mapId
	 * @param x
	 * @param y
	 */
	public void warpToMap(Entity entity, int mapId, int x, int y) {
		// Check to make sure the session isn't currently waiting for a map.

		if (!baseServer.getWorld().getManager(GroupManager.class)
				.inInGroup(entity, Constants.GROUP_PLAYERS)
				|| !baseServer.getServerSessionManager()
						.getEntitySession(entity).isWaitingForMap()) {

			leaveMap(entity);
		}

		joinMap(entity, mapId, x, y);
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
}
