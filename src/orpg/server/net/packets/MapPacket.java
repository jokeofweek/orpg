package orpg.server.net.packets;

import orpg.server.data.DestinationType;
import orpg.shared.data.Map;

public abstract class MapPacket extends ServerPacket {

	private Map map;

	public MapPacket(Map map) {
		this.map = map;
	}

	@Override
	public DestinationType getDestinationType() {
		return DestinationType.MAP;
	}

	@Override
	public Object getDestinationObject() {
		return this.map;
	}
}
