package orpg.shared.data.component;

import orpg.shared.net.serialize.InputByteBuffer;
import orpg.shared.net.serialize.OutputByteBuffer;
import orpg.shared.net.serialize.ValueSerializer;

/**
 * This component represents the spatial position of an entity on a map.
 * 
 * @author Dominic Charley-Roy
 */
public class Position extends SynchronizeableComponent {

	private int map;
	private int x;
	private int y;

	public Position(int map, int x, int y) {
		this.map = map;
		this.x = x;
		this.y = y;
	}

	public int getMap() {
		return map;
	}

	public void setMap(int map) {
		this.map = map;
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
	public SerializeableComponentType getType() {
		return SerializeableComponentType.POSITION;
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
			Position pos = (Position) obj;
			out.putInt(pos.getMap());
			out.putInt(pos.getX());
			out.putInt(pos.getY());
		}

		@Override
		public Position get(InputByteBuffer in) {
			Position pos = new Position(in.getInt(), in.getInt(),
					in.getInt());
			return pos;
		}
	}

}
