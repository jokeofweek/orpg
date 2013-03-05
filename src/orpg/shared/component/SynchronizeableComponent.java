package orpg.shared.component;

import orpg.shared.net.serialize.SerializableValue;
import orpg.shared.net.serialize.ValueSerializer;

import com.artemis.Component;

public abstract class SynchronizeableComponent extends Component implements
		SerializableValue<SynchronizeableComponent> {

	public abstract SynchronizeableComponentType getType();
	
	@Override
	public final ValueSerializer<SynchronizeableComponent> getSerializer() {
		return getType().getSerializer();
	}

}
