package orpg.server.event;

import com.artemis.Entity;

import orpg.server.BaseServer;
import orpg.server.systems.MovementSystem;

public class EntityLeaveMapEvent extends MovementEvent {

	private Entity entity;

	public EntityLeaveMapEvent(Entity entity) {
		this.entity = entity;
	}

	@Override
	public void process(BaseServer baseServer,
			MovementSystem movementSystem) {
		movementSystem.leaveMap(entity);
	}

}
