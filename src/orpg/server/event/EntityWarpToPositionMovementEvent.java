package orpg.server.event;

import com.artemis.Entity;

import orpg.server.BaseServer;
import orpg.server.systems.MovementSystem;

public class EntityWarpToPositionMovementEvent implements MovementEvent {

	private Entity entity;
	private int mapId;
	private int x;
	private int y;

	public EntityWarpToPositionMovementEvent(Entity entity, int mapId,
			int x, int y) {
		this.entity = entity;
		this.mapId = mapId;
		this.x = x;
		this.y = y;
	}

	@Override
	public void process(BaseServer baseServer,
			MovementSystem movementSystem) {
		movementSystem.warpToMap(entity, mapId, x, y);
	}

}
