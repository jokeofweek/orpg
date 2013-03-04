package orpg.shared.net.serialize;

public interface ValueSerializer<K> {

	public void put(OutputByteBuffer out, K obj);
	
	public K read(InputByteBuffer in);
	
}
