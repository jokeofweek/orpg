package orpg.server.event;

import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.artemis.utils.ImmutableBag;

import orpg.server.BaseServer;
import orpg.server.ServerSessionManager;
import orpg.server.systems.MovementSystem;
import orpg.shared.Constants;
import orpg.shared.data.Map;

public class RefreshMapPlayersEvent extends MovementEvent {

	private int mapId;

	public RefreshMapPlayersEvent(int mapId) {
		this.mapId = mapId;
	}

	@Override
	public void process(BaseServer baseServer,
			MovementSystem movementSystem) {
		Map map = baseServer.getMapController().get(this.mapId);
		ImmutableBag<Entity> entities;
		GroupManager groupManager = baseServer.getWorld().getManager(
				GroupManager.class);
		ServerSessionManager sessionManager = baseServer
				.getServerSessionManager();

		for (short x = 0; x < map.getSegmentsWide(); x++) {
			for (short y = 0; y < map.getSegmentsHigh(); y++) {
				if (map.getSegment(x, y) != null) {
					entities = groupManager.getEntities(String.format(
							Constants.GROUP_MAP_PLAYERS, mapId, x, y));
					for (int i = 0; i < entities.size(); i++) {
						movementSystem.refreshMap(sessionManager
								.getEntitySession(entities.get(i)));
					}
				}
			}
		}
	}
}
