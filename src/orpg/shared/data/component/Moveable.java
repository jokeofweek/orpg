package orpg.shared.data.component;

import java.util.HashMap;

import orpg.shared.data.Direction;
import orpg.shared.data.annotations.Attachable;
import orpg.shared.data.annotations.Editable;
import orpg.shared.net.serialize.InputByteBuffer;
import orpg.shared.net.serialize.OutputByteBuffer;
import orpg.shared.net.serialize.ValueSerializer;

@Attachable(name = "Direction", description = "This give the entity a notion of direction.")
public class Moveable extends SynchronizebleComponent {

	private boolean isMoving;
	private boolean isMoveProcessed;

	@Editable(name = "Direction", description = "The direction the entity is facing.")
	private Direction direction;

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public boolean isMoving() {
		return isMoving;
	}

	public void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
		this.setMoveProcessed(false);
	}

	public boolean isMoveProcessed() {
		return isMoveProcessed;
	}

	public void setMoveProcessed(boolean isMoveProcessed) {
		this.isMoveProcessed = isMoveProcessed;
	}

	@Override
	public SerializeableComponentType getType() {
		return SerializeableComponentType.MOVEABLE;
	}

	public static class Serializer implements
			ValueSerializer<SerializableComponent> {

		private static Serializer instance = new Serializer();

		public static Serializer getInstance() {
			return instance;
		}

		private Serializer() {
		}

		@Override
		public void put(OutputByteBuffer out, SerializableComponent obj) {
			// Make a byte from the direction, and then left shift and add a bit
			// for the boolean. This is done to put all move data in one byte.
			Moveable r = (Moveable) obj;
			byte v = (byte) ((r.getDirection().ordinal() << 2)
					| (r.isMoving ? 2 : 0) | (r.isMoveProcessed ? 1 : 0));

			out.putByte(v);
		}

		@Override
		public Moveable get(InputByteBuffer in) {
			byte v = in.getByte();
			Moveable moveable = new Moveable();
			moveable.setMoving((v & 2) == 2);
			moveable.setMoveProcessed((v & 1) == 1);
			moveable.setDirection(Direction.values()[v >> 2]);
			return moveable;
		}
	}
}
