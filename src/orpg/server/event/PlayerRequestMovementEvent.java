package orpg.server.event;

import java.util.Map;

import com.artemis.Entity;
import com.artemis.managers.GroupManager;

import orpg.server.BaseServer;
import orpg.server.ServerSession;
import orpg.server.data.SessionType;
import orpg.server.net.packets.ClientSyncEntityPropertiesPacket;
import orpg.server.systems.MovementSystem;
import orpg.shared.Constants;
import orpg.shared.data.Direction;
import orpg.shared.data.component.Moveable;
import orpg.shared.data.component.Position;

public class PlayerRequestMovementEvent extends MovementEvent {

	private ServerSession session;
	private Direction direction;

	public PlayerRequestMovementEvent(ServerSession session, Direction direction) {
		this.direction = direction;
		this.session = session;
	}

	@Override
	public void process(BaseServer baseServer, MovementSystem movementSystem) {
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

		if (baseServer.getMapController().get(position.getMap())
				.isWalkable(x, y)) {

			int oldX = position.getX();
			int oldY = position.getY();

			position.setX(x);
			position.setY(y);
			moveable.setDirection(direction);
			moveable.setMoveProcessed(false);
			moveable.setMoving(true);
			movementSystem.updateEntitySegment(entity, position.getMap(), oldX,
					oldY);

			baseServer.sendPacket(new ClientSyncEntityPropertiesPacket(session, entity,
					true, Moveable.class));

		} else {
			session.preventativeDisconnect("Position modification.");
		}
	}
}
