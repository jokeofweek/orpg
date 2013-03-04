package orpg.shared.net;

public interface SerializableValue<K extends SerializableValue> {

	public ValueSerializer<K> getSerializer();
	
}
