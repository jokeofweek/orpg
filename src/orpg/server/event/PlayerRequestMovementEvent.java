package orpg.server.event;

import java.util.ArrayList;
import java.util.List;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import orpg.server.BaseServer;
import orpg.server.ServerSession;
import orpg.server.data.SessionType;
import orpg.server.net.packets.ClientMovePacket;
import orpg.server.net.packets.ClientSyncEntityPropertiesPacket;
import orpg.server.systems.MovementSystem;
import orpg.shared.Constants;
import orpg.shared.data.Direction;
import orpg.shared.data.Map;
import orpg.shared.data.Pair;
import orpg.shared.data.component.Collidable;
import orpg.shared.data.component.Moveable;
import orpg.shared.data.component.Position;

public class PlayerRequestMovementEvent extends MovementEvent {

	private ServerSession session;
	private Direction direction;

	public PlayerRequestMovementEvent(ServerSession session,
			Direction direction) {
		this.direction = direction;
		this.session = session;
	}

	@Override
	public void process(BaseServer baseServer,
			MovementSystem movementSystem) {
		// Safety test
		if (session.getSessionType() == SessionType.LOGGED_OUT) {
			return;
		}

		Entity entity = session.getEntity();
		Position position = entity.getWorld().getMapper(Position.class)
				.get(entity);
		Moveable moveable = entity.getWorld().getMapper(Moveable.class)
				.get(entity);

		// Determine new position
		int x = position.getX();
		int y = position.getY();

		switch (direction) {
		case UP:
			y--;
			break;
		case DOWN:
			y++;
			break;
		case LEFT:
			x--;
			break;
		case RIGHT:
			x++;
			break;
		}

		Map map = baseServer.getMapController().get(position.getMap());

		if (map.isWalkable(x, y)) {

			// Check for any collisions, building a list of
			// collision handlers to process if we are succesful.
			ComponentMapper<Collidable> collideableMapper = baseServer
					.getWorld().getMapper(Collidable.class);
			ComponentMapper<Position> positionMapper = baseServer
					.getWorld().getMapper(Position.class);

			ImmutableBag<Entity> segmentEntities = baseServer
					.getWorld()
					.getManager(GroupManager.class)
					.getEntities(
							String.format(Constants.GROUP_SEGMENT,
									map.getId(), map.getSegmentX(x),
									map.getSegmentY(y)));

			Position otherPosition;
			Collidable collideable;
			Entity other;
			List<Pair<Entity, Collidable>> handlers = new ArrayList<Pair<Entity, Collidable>>();
			boolean passable = true;

			for (int i = 0; i < segmentEntities.size(); i++) {
				other = segmentEntities.get(i);
				collideable = collideableMapper.getSafe(other);
				if (collideable != null) {
					otherPosition = positionMapper.getSafe(other);
					if (otherPosition != null && otherPosition.getX() == x
							&& otherPosition.getY() == y) {
						if (!collideable.isPassable()) {
							passable = false;
							break;
						} else {
							handlers.add(new Pair<Entity, Collidable>(
									other, collideable));
						}
					}
				}
			}

			if (passable) {
				int oldX = position.getX();
				int oldY = position.getY();

				position.setX(x);
				position.setY(y);
				moveable.setDirection(direction);

				movementSystem.updateEntitySegment(entity,
						position.getMap(), oldX, oldY);

				baseServer.sendPacket(new ClientMovePacket(session,
						position.getMap(), entity, direction, true));

				// Iterate through the handlers
				for (Pair<Entity, Collidable> handler : handlers) {
					handler.getSecond().onCollision(baseServer,
							handler.getFirst(), entity);
				}
			}

		} else {
			session.preventativeDisconnect("Position modification.");
		}
	}
}
