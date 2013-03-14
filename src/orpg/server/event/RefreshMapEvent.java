package orpg.server.event;

import orpg.server.BaseServer;
import orpg.server.systems.MapProcessSystem;

public class RefreshMapEvent implements MapEvent {

	private int mapId;

	public RefreshMapEvent(int mapId) {
		this.mapId = mapId;
	}

	@Override
	public void process(BaseServer baseServer,
			MapProcessSystem mapProcessSystem) {
		mapProcessSystem.refreshMapEntities(mapId);
	}

}
