package orpg.shared.net.serialize;

import orpg.shared.component.SynchronizeableComponent;
import orpg.shared.component.SynchronizeableComponentType;

/**
 * This class is responsible for taking a generic synchronizeable component and
 * writing it to a byte buffer such that it can be read back using the proper
 * serializer.
 * 
 * @author Dominic Charley-Roy
 */
public class SynchronizeableComponentSerializer implements
		ValueSerializer<SynchronizeableComponent> {

	private static SynchronizeableComponentSerializer instance = new SynchronizeableComponentSerializer();

	private SynchronizeableComponentSerializer() {
	};

	public static SynchronizeableComponentSerializer getInstance() {
		return instance;
	}

	@Override
	public void put(OutputByteBuffer out, SynchronizeableComponent obj) {
		out.putByte((byte) obj.getType().ordinal());
		obj.getType().getSerializer().put(out, obj);
	}

	@Override
	public SynchronizeableComponent get(InputByteBuffer in) {
		SynchronizeableComponentType type = SynchronizeableComponentType.values()[in.getByte()];
		return type.getSerializer().get(in);
	}

}
