package orpg.shared.net.serialize;

public interface SerializeableValue<K extends SerializeableValue> {

	public ValueSerializer<K> getSerializer();
	
}
