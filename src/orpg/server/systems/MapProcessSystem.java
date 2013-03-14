package orpg.server.systems;

import java.util.logging.Level;

import orpg.server.BaseServer;
import orpg.server.data.components.EventProcessor;
import orpg.shared.Constants;
import orpg.shared.data.ComponentList;
import orpg.shared.data.Map;
import orpg.shared.data.Segment;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.artemis.systems.IntervalEntitySystem;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;

public class MapProcessSystem extends IntervalEntitySystem {

	private BaseServer baseServer;

	public MapProcessSystem(BaseServer baseServer, float interval) {
		super(Aspect.getAspectForAll(EventProcessor.class), interval);
		this.baseServer = baseServer;
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> arg0) {
		// Iterate through all maps
	}

	public void spawnSegmentEntities(int mapId, short segmentX, short segmentY) {
		Map map = baseServer.getMapController().get(mapId);
		if (segmentX < 0 || segmentY < 0 || segmentX >= map.getSegmentsWide()
				|| segmentY >= map.getSegmentsHigh()) {
			throw new IllegalArgumentException(
					"Could not spawn segment entities for map " + mapId
							+ " segment [" + segmentX + "][" + segmentY + "]");
		}

		// Iterate through all segment entities if it is loaded, spawning them
		Segment segment = map.getSegment(segmentX, segmentY);
		if (segment == null) {
			baseServer
					.getConfigManager()
					.getErrorLogger()
					.log(Level.INFO,
							String.format(
									"Tried to spawn segment entities for map %d [%d][%d] but segment was null.",
									mapId, segmentX, segmentY));
			return;
		}

		Entity entity;
		GroupManager groupManager = world.getManager(GroupManager.class);

		for (ComponentList componentList : segment.getEntities()) {
			entity = world.createEntity();
			for (Component component : componentList.getComponents()) {
				entity.addComponent(component);
			}

			// Tag the entity with segment info
			groupManager.add(entity, String.format(Constants.GROUP_MAP, mapId));
			groupManager.add(entity, String.format(Constants.GROUP_SEGMENT,
					mapId, segmentX, segmentY));
			world.addEntity(entity);
		}

	}

	public void refreshMapEntities(int mapId) {
		Map map = baseServer.getMapController().get(mapId);

		if (map == null) {
			return;
		}

		// Destroy all entities which are not currently players.
		GroupManager groupManager = world.getManager(GroupManager.class);
		ImmutableBag<Entity> oldEntities = groupManager.getEntities(String
				.format(Constants.GROUP_MAP, mapId));
		for (int i = 0; i < oldEntities.size(); i++) {
			if (!groupManager.inInGroup(oldEntities.get(i),
					Constants.GROUP_PLAYERS)) {
				oldEntities.get(i).deleteFromWorld();
			}
		}

		// Spawn all segment entities
		for (short x = 0; x < map.getSegmentsWide(); x++) {
			for (short y = 0; y < map.getSegmentsHigh(); y++) {
				if (map.getSegment(x, y) != null) {
					spawnSegmentEntities(mapId, x, y);
				}
			}
		}
	}

}
