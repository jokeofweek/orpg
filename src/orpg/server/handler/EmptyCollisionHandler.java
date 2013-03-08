package orpg.server.handler;

import orpg.server.BaseServer;

import com.artemis.Entity;

public class EmptyCollisionHandler implements CollisionHandler {

	private static EmptyCollisionHandler instance = new EmptyCollisionHandler();

	public static EmptyCollisionHandler getInstance() {
		return instance;
	}

	private EmptyCollisionHandler() {
	}

	@Override
	public void onCollision(BaseServer baseServer, Entity componentOwner,
			Entity collidingEntity) {
	}

}
