package orpg.shared.data.component;

import orpg.server.BaseServer;
import orpg.shared.data.annotations.Attachable;
import orpg.shared.data.annotations.Editable;
import orpg.shared.data.component.SerializableComponent.Serializer;
import orpg.shared.net.serialize.InputByteBuffer;
import orpg.shared.net.serialize.OutputByteBuffer;
import orpg.shared.net.serialize.ValueSerializer;

import com.artemis.Entity;

@Attachable(name="Simple Collidable", description="A simple collision handler.")
public class BasicCollidable extends Collidable {

	@Editable(name = "Passable?", description = "Can entities pass through this?")
	public boolean passable;

	public static final BasicCollidable BLOCKING = new BasicCollidable(
			false);
	public static final BasicCollidable NONBLOCKING = new BasicCollidable(
			true);

	public BasicCollidable() {
	}

	public BasicCollidable(boolean passable) {
		this.passable = passable;
	}

	@Override
	public void onCollision(BaseServer baseServer, Entity componentOwner,
			Entity collidingEntity) {
	}

	public void setPassable(boolean passable) {
		this.passable = passable;
	}

	@Override
	public boolean isPassable(Entity entity) {
		return this.passable;
	}

	@Override
	public SerializeableComponentType getType() {
		return SerializeableComponentType.BASIC_COLLIDABLE;
	}

	public static class Serializer implements
			ValueSerializer<SerializableComponent> {

		private static Serializer instance = new Serializer();

		private Serializer() {
		};

		public static Serializer getInstance() {
			return instance;
		}

		@Override
		public void put(OutputByteBuffer out, SerializableComponent obj) {
			out.putBoolean(((BasicCollidable) obj).passable);
		}

		@Override
		public BasicCollidable get(InputByteBuffer in) {
			boolean passable = in.getBoolean();
			if (!passable) {
				return BLOCKING;
			} else {
				return NONBLOCKING;
			}
		}

	}

}
