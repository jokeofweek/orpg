package orpg.shared.data.component;

import orpg.shared.data.annotations.Attachable;
import orpg.shared.data.annotations.Editable;
import orpg.shared.net.serialize.InputByteBuffer;
import orpg.shared.net.serialize.OutputByteBuffer;
import orpg.shared.net.serialize.ValueSerializer;

/**
 * This component signifies that a given entity is renderable, and keeps a
 * reference to the render ID associated with the entity. An example of this is
 * a sprite number.
 * 
 * @author Dominic Charley-Roy
 */
@Attachable(name="Renderable", description="This entity will be rendered to the screen.")
public class Renderable extends SynchronizebleComponent {

	@Editable(name = "Render Reference")
	public short renderReference;

	public Renderable() {
	}

	public Renderable(short renderReference) {
		this.renderReference = renderReference;
	}

	public short getRenderReference() {
		return renderReference;
	}

	public void setRenderReference(short renderReference) {
		this.renderReference = renderReference;
	}

	@Override
	public SerializeableComponentType getType() {
		return SerializeableComponentType.RENDERABLE;
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
			Renderable r = (Renderable) obj;
			out.putShort(r.getRenderReference());
		}

		@Override
		public Renderable get(InputByteBuffer in) {
			Renderable r = new Renderable(in.getShort());
			return r;
		}
	}
}
