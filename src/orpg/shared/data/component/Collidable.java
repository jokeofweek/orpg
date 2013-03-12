package orpg.shared.data.component;

import orpg.server.BaseServer;
import com.artemis.Entity;

public abstract class Collidable extends SynchronizebleComponent {
	
	public abstract void onCollision(BaseServer baseServer, Entity componentOwner, Entity collidingEntity);

	public abstract boolean isPassable(Entity entity);

}
