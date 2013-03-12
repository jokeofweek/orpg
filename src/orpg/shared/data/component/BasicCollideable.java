package orpg.shared.data.component;

import orpg.server.BaseServer;
import orpg.shared.data.annotations.Editable;
import orpg.shared.data.component.SerializeableComponent.Serializer;
import orpg.shared.net.serialize.InputByteBuffer;
import orpg.shared.net.serialize.OutputByteBuffer;
import orpg.shared.net.serialize.ValueSerializer;

import com.artemis.Entity;

public class BasicCollideable extends Collideable {

	@Editable(name = "Passable?", description = "Can entities pass through this?")
	public boolean passable;

	public static final BasicCollideable BLOCKING = new BasicCollideable(
			false);
	public static final BasicCollideable NONBLOCKING = new BasicCollideable(
			true);

	public BasicCollideable() {
	}

	public BasicCollideable(boolean passable) {
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
		return SerializeableComponentType.BASIC_COLLIDEABLE;
	}

	public static class Serializer implements ValueSerializer<SerializeableComponent> {

		private static Serializer instance = new Serializer();

		private Serializer() {
		};

		public static Serializer getInstance() {
			return instance;
		}
		
		@Override
		public void put(OutputByteBuffer out, SerializeableComponent obj) {
			out.putBoolean(((BasicCollideable) obj).passable);
		}

		@Override
		public Collideable get(InputByteBuffer in) {
			boolean passable = in.getBoolean();
			if (!passable) {
				return BLOCKING;
			} else {
				return NONBLOCKING;
			}
		}

	}

}
