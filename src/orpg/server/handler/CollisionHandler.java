package orpg.server.handler;

import orpg.server.BaseServer;

import com.artemis.Entity;

public interface CollisionHandler {

	public void onCollision(BaseServer baseServer, Entity componentOwner, Entity collidingEntity);
	
}
