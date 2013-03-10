package orpg.shared.data.component;

import orpg.shared.net.serialize.InputByteBuffer;
import orpg.shared.net.serialize.OutputByteBuffer;
import orpg.shared.net.serialize.ValueSerializer;

/**
 * This component represents a name associated with the entity.
 * 
 * @author Dominic Charley-Roy
 */
public class Named extends SynchronizeableComponent {

	private String name;

	public Named(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public SynchronizeableComponentType getType() {
		return SynchronizeableComponentType.NAMED;
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
			Named r = (Named) obj;
			out.putString(r.getName());
		}

		@Override
		public Named get(InputByteBuffer in) {
			return new Named(in.getString());
		}
	}
}
