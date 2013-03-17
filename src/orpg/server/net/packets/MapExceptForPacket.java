package orpg.server.net.packets;

import orpg.server.ServerSession;
import orpg.server.net.DestinationType;
import orpg.shared.data.Map;
import orpg.shared.data.Pair;

public abstract class MapExceptForPacket extends ServerPacket {

	private Pair<ServerSession, Integer> destinationObject;

	public MapExceptForPacket(ServerSession serverSession, int mapId) {
		this.destinationObject = new Pair<ServerSession, Integer>(
				serverSession, mapId);
	}

	@Override
	public DestinationType getDestinationType() {
		return DestinationType.MAP_EXCEPT_FOR;
	}

	@Override
	public Object getDestinationObject() {
		return this.destinationObject;
	}
}
