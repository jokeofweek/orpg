package orpg.shared.component;

import orpg.server.net.packets.ClientSyncEntity;
import orpg.shared.net.serialize.SerializableValue;
import orpg.shared.net.serialize.ValueSerializer;

import com.artemis.Component;

/**
 * This is a special type of component which is meant to be sent from the server
 * to the client. It will be sent when the entity is sent to the client as well
 * as when the entity is synchronized. You can also specify that this component
 * be synchronized via the {@link ClientSyncEntity} packet.
 * 
 * @author Dominic Charley-Roy
 */
public abstract class SynchronizeableComponent extends Component implements
		SerializableValue<SynchronizeableComponent> {

	public abstract SynchronizeableComponentType getType();

	@Override
	public final ValueSerializer<SynchronizeableComponent> getSerializer() {
		return getType().getSerializer();
	}

}
