package orpg.shared.data.component;

import orpg.server.handler.CollisionHandler;
import orpg.shared.net.serialize.InputByteBuffer;
import orpg.shared.net.serialize.OutputByteBuffer;
import orpg.shared.net.serialize.ValueSerializer;

import com.artemis.Component;
import com.artemis.ComponentType;

public class Collideable extends SynchronizeableComponent {

	private boolean passable;
	private CollisionHandler collisionHandler;

	public void setCollisionHandler(CollisionHandler collisionHandler) {
		this.collisionHandler = collisionHandler;
	}

	public CollisionHandler getCollisionHandler() {
		return collisionHandler;
	}

	public void setPassable(boolean passable) {
		this.passable = passable;
	}

	public boolean isPassable() {
		return passable;
	}

	@Override
	public SerializeableComponentType getType() {
		return SerializeableComponentType.COLLIDEABLE;
	}

	public static class Serializer implements
			ValueSerializer<SerializeableComponent> {

		private static Serializer instance = new Serializer();

		public static Serializer getInstance() {
			return instance;
		}

		private Serializer() {
		}

		@Override
		public void put(OutputByteBuffer out, SerializeableComponent obj) {
			// Only put the passable value
			out.putBoolean(((Collideable) obj).isPassable());
		}

		@Override
		public Collideable get(InputByteBuffer in) {
			Collideable collideable = new Collideable();
			collideable.setPassable(in.getBoolean());
			return collideable;
		}

	}

}
