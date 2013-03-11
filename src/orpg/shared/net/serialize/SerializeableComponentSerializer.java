package orpg.shared.net.serialize;

import orpg.shared.data.component.SerializeableComponent;
import orpg.shared.data.component.SerializeableComponentType;
import orpg.shared.data.component.SynchronizeableComponent;

/**
 * This class is responsible for taking a generic serializeable component and
 * writing it to a byte buffer such that it can be read back using the proper
 * serializer.
 * 
 * @author Dominic Charley-Roy
 */
public class SerializeableComponentSerializer implements
		ValueSerializer<SerializeableComponent> {

	private static SerializeableComponentSerializer instance = new SerializeableComponentSerializer();

	private SerializeableComponentSerializer() {
	};

	public static SerializeableComponentSerializer getInstance() {
		return instance;
	}

	@Override
	public void put(OutputByteBuffer out, SerializeableComponent obj) {
		out.putByte((byte) obj.getType().ordinal());
		obj.getType().getSerializer().put(out, obj);
	}

	@Override
	public SerializeableComponent get(InputByteBuffer in) {
		SerializeableComponentType type = SerializeableComponentType.values()[in.getByte()];
		return type.getSerializer().get(in);
	}

}
