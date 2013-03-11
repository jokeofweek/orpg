package orpg.shared.data.component;

import orpg.shared.data.annotations.Editable;
import orpg.shared.net.serialize.InputByteBuffer;
import orpg.shared.net.serialize.OutputByteBuffer;
import orpg.shared.net.serialize.ValueSerializer;

/**
 * This component represents a name associated with the entity.
 * 
 * @author Dominic Charley-Roy
 */
public class Named extends SynchronizeableComponent {
	static {
		EditableComponentDescriptor descriptor = new EditableComponentDescriptor(
				"Named",
				"This component allows you to assign a name to an entity.",
				Named.class);
		EditableComponentManager.getInstance().register(descriptor);
	}

	@Editable(name = "Name", description = "The name of the entity.")
	private String name;

	public Named(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public SerializeableComponentType getType() {
		return SerializeableComponentType.NAMED;
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
			Named r = (Named) obj;
			out.putString(r.getName());
		}

		@Override
		public Named get(InputByteBuffer in) {
			return new Named(in.getString());
		}
	}
}
