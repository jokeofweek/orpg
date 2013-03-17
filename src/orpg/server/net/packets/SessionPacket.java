package orpg.server.net.packets;

import orpg.server.ServerSession;
import orpg.server.net.DestinationType;

public abstract class SessionPacket extends ServerPacket {

	private ServerSession session;

	public SessionPacket(ServerSession session) {
		super();
		this.session = session;
	}

	@Override
	public DestinationType getDestinationType() {
		return DestinationType.SINGLE_SESSION;
	}

	@Override
	public Object getDestinationObject() {
		return this.session;
	}
}
