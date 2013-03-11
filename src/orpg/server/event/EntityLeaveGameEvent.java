package orpg.server.event;

import com.artemis.Entity;

import orpg.server.BaseServer;
import orpg.server.systems.MovementSystem;

public class EntityLeaveGameEvent extends MovementEvent {

	private Entity entity;

	public EntityLeaveGameEvent(Entity entity) {
		this.entity = entity;
	}

	@Override
	public void process(BaseServer baseServer,
			MovementSystem movementSystem) {
		movementSystem.leaveMap(entity);
		entity.deleteFromWorld();
		baseServer.getServerSessionManager().removeSessionEntity(entity);
	}

}
