package orpg.shared.component;

import java.util.HashMap;

import orpg.shared.data.Direction;
import orpg.shared.net.serialize.InputByteBuffer;
import orpg.shared.net.serialize.OutputByteBuffer;
import orpg.shared.net.serialize.ValueSerializer;

/**
 * This component contains directional data and usually represents the direction
 * an entity is facing.
 */
public class Directioned extends SynchronizeableComponent {

	private static HashMap<Direction, Directioned> instances = new HashMap<Direction, Directioned>(
			4);

	static {
		for (Direction dir : Direction.values()) {
			instances.put(dir, new Directioned(dir));
		}
	}

	public static Directioned getInstances(Direction direction) {
		return instances.get(direction);
	}

	private Direction direction;

	private Directioned(Direction direction) {
		this.direction = direction;
	}

	public Direction getDirection() {
		return direction;
	}

	@Override
	public SynchronizeableComponentType getType() {
		return SynchronizeableComponentType.DIRECTIONED;
	}

	public static class Serializer implements
			ValueSerializer<SynchronizeableComponent> {

		private static Serializer instance = new Serializer();

		public static Serializer getInstance() {
			return instance;
		}

		private Serializer() {
		}

		@Override
		public void put(OutputByteBuffer out, SynchronizeableComponent obj) {
			Directioned r = (Directioned) obj;
			out.putByte((byte) r.getDirection().ordinal());
		}

		@Override
		public Directioned get(InputByteBuffer in) {
			return Directioned.getInstances(Direction.values()[in
					.getByte()]);
		}
	}
}
