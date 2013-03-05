package orpg.shared.component;

import orpg.shared.net.serialize.InputByteBuffer;
import orpg.shared.net.serialize.OutputByteBuffer;
import orpg.shared.net.serialize.ValueSerializer;

public class Renderable extends SynchronizeableComponent {

	public short renderReference;
	
	public short getRenderReference() {
		return renderReference;
	}
	public void setRenderReference(short renderReference) {
		this.renderReference = renderReference;
	}
	
	@Override
	public SynchronizeableComponentType getType() {
		return SynchronizeableComponentType.RENDERABLE;
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
			Renderable r = (Renderable) obj;
			out.putShort(r.getRenderReference());
		}

		@Override
		public Renderable get(InputByteBuffer in) {
			Renderable r = new Renderable();
			r.setRenderReference(in.getShort());
			return r;
		}
	}
}
