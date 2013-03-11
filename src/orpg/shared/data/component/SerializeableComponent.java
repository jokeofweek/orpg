package orpg.shared.data.component;

import orpg.server.net.packets.ClientSyncEntityPropertiesPacket;
import orpg.shared.net.serialize.InputByteBuffer;
import orpg.shared.net.serialize.OutputByteBuffer;
import orpg.shared.net.serialize.SerializableValue;
import orpg.shared.net.serialize.SerializeableComponentSerializer;
import orpg.shared.net.serialize.ValueSerializer;

import com.artemis.Component;

/**
 * This is a special type of component which can be serialized using
 * {@link InputByteBuffer} and {@link OutputByteBuffer}.
 * 
 * @author Dominic Charley-Roy
 */
public abstract class SerializeableComponent extends Component implements
		SerializableValue<SerializeableComponent> {

	public abstract SerializeableComponentType getType();

	@Override
	public final ValueSerializer<SerializeableComponent> getSerializer() {
		return SerializeableComponentSerializer.getInstance();
	}

}
