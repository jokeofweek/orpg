package orpg.shared.net.serialize;

public interface SerializableValue<K extends SerializableValue> {

	public ValueSerializer<K> getSerializer();
	
}
