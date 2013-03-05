package orpg.shared.component;

import orpg.shared.net.serialize.InputByteBuffer;
import orpg.shared.net.serialize.OutputByteBuffer;
import orpg.shared.net.serialize.ValueSerializer;

public class Position extends SynchronizeableComponent {

	private int x;
	private int y;

	public Position() {
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public SynchronizeableComponentType getType() {
		return SynchronizeableComponentType.POSITION;
	}

	public static class Serializer implements ValueSerializer<SynchronizeableComponent> {

		private static Serializer instance = new Serializer();

		public static Serializer getInstance() {
			return instance;
		}

		private Serializer() {
		}

		@Override
		public void put(OutputByteBuffer out, SynchronizeableComponent obj) {
			Position pos = (Position) obj;
			out.putInt(pos.getX());
			out.putInt(pos.getY());
		}

		@Override
		public Position get(InputByteBuffer in) {
			Position pos = new Position();
			pos.setX(in.getInt());
			pos.setY(in.getInt());
			return pos;
		}
	}

}
