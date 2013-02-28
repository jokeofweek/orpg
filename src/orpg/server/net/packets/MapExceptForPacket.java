package orpg.server.net.packets;

import orpg.server.ServerSession;
import orpg.server.data.DestinationType;
import orpg.shared.data.Map;
import orpg.shared.data.Pair;

public abstract class MapExceptForPacket extends ServerPacket {

	private Pair<ServerSession, Map> destinationObject;

	public MapExceptForPacket(ServerSession serverSession, Map map) {
		this.destinationObject = new Pair<ServerSession, Map>(
				serverSession, map);
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
