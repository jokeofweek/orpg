package orpg.shared.data.component;

import orpg.server.net.packets.ClientSyncEntityPropertiesPacket;
import orpg.shared.net.serialize.InputByteBuffer;
import orpg.shared.net.serialize.OutputByteBuffer;
import orpg.shared.net.serialize.SerializeableValue;
import orpg.shared.net.serialize.ValueSerializer;

import com.artemis.Component;

/**
 * This is a special type of component which can be serialized using
 * {@link InputByteBuffer} and {@link OutputByteBuffer}.
 * 
 * @author Dominic Charley-Roy
 */
public abstract class SerializeableComponent extends Component implements
		SerializeableValue<SerializeableComponent> {

	public abstract SerializeableComponentType getType();

	@Override
	public final ValueSerializer<SerializeableComponent> getSerializer() {
		return Serializer.getInstance();
	}

	public static class Serializer implements
			ValueSerializer<SerializeableComponent> {

		private static Serializer instance = new Serializer();

		private Serializer() {
		};

		public static Serializer getInstance() {
			return instance;
		}

		@Override
		public void put(OutputByteBuffer out, SerializeableComponent obj) {
			out.putByte((byte) obj.getType().ordinal());
			obj.getType().getSerializer().put(out, obj);
		}

		@Override
		public SerializeableComponent get(InputByteBuffer in) {
			SerializeableComponentType type = SerializeableComponentType
					.values()[in.getByte()];
			return type.getSerializer().get(in);
		}

	}
}
