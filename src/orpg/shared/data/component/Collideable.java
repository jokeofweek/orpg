package orpg.shared.data.component;

import orpg.server.BaseServer;
import orpg.shared.data.annotations.Editable;
import orpg.shared.net.serialize.InputByteBuffer;
import orpg.shared.net.serialize.OutputByteBuffer;
import orpg.shared.net.serialize.ValueSerializer;

import com.artemis.Component;
import com.artemis.ComponentType;
import com.artemis.Entity;

public abstract class Collideable extends SynchronizeableComponent {
	
	public abstract void onCollision(BaseServer baseServer, Entity componentOwner, Entity collidingEntity);

	public abstract boolean isPassable(Entity entity);

}
