package orpg.shared.data.component;

import orpg.shared.data.annotations.Attachable;
import orpg.shared.data.annotations.Editable;
import orpg.shared.net.serialize.InputByteBuffer;
import orpg.shared.net.serialize.OutputByteBuffer;
import orpg.shared.net.serialize.ValueSerializer;

/**
 * This component represents a name associated with the entity.
 * 
 * @author Dominic Charley-Roy
 */
@Attachable(name="Named", description="This component assigns a name to an entity.")
public class Named extends SynchronizebleComponent {

	@Editable(name = "Name", description = "The name of the entity.")
	public String name;

	public Named() {
	}

	public Named(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public SerializeableComponentType getType() {
		return SerializeableComponentType.NAMED;
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
			Named r = (Named) obj;
			out.putString(r.getName());
		}

		@Override
		public Named get(InputByteBuffer in) {
			return new Named(in.getString());
		}
	}
}
