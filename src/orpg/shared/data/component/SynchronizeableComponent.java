package orpg.shared.data.component;

import orpg.server.net.packets.ClientSyncEntityPropertiesPacket;
import orpg.shared.net.serialize.SerializableValue;
import orpg.shared.net.serialize.ValueSerializer;

import com.artemis.Component;

/**
 * This is a special extension of {@link SerializeableComponent} for components
 * which are to be sent to the client when either a client requests the map
 * entities or via a {@link ClientSyncEntityPropertiesPacket}.
 * 
 * @author Dominic Charley-Roy
 */
public abstract class SynchronizeableComponent extends SerializeableComponent {

}
