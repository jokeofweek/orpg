package orpg.server.event;

public interface EventProcessor<K>  {

	public void addEvent(K event);
	
}
