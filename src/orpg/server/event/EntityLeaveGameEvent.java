package orpg.server.event;

import com.artemis.Entity;

import orpg.server.BaseServer;
import orpg.server.ServerSession;
import orpg.server.data.entity.EntityFactory;
import orpg.server.systems.MovementSystem;

public class EntityLeaveGameEvent implements MovementEvent {

	private Entity entity;

	public EntityLeaveGameEvent(Entity entity) {
		this.entity = entity;
	}

	@Override
	public void process(BaseServer baseServer,
			MovementSystem movementSystem) {
		// Save the account
		ServerSession session = baseServer.getServerSessionManager()
				.getEntitySession(entity);
		baseServer.getEntityFactory().updateEntityAccountCharacter(
				session.getAccount().getName(), entity);
		baseServer.getAccountController().save(
				session.getAccount().getName());

		movementSystem.leaveMap(entity);
		entity.deleteFromWorld();
		baseServer.getServerSessionManager().removeSessionEntity(entity);
	}

}
