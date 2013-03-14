package orpg.server.event;

import orpg.server.BaseServer;
import orpg.server.systems.MapProcessSystem;

public interface MapEvent {

	public void process(BaseServer baseServer, MapProcessSystem mapProcessSystem);
}
