package orpg.server.net.packets;

import orpg.server.ServerSession;
import orpg.server.data.DestinationType;

public abstract class GlobalExceptForPacket extends ServerPacket {

	private ServerSession session;
	
	public GlobalExceptForPacket(ServerSession session) {
		this.session = session;
	}

	@Override
	public DestinationType getDestinationType() {
		return DestinationType.GLOBAL_EXCEPT_FOR;
	}

	@Override
	public Object getDestinationObject() {
		// TODO Auto-generated method stub
		return session;
	}


}
