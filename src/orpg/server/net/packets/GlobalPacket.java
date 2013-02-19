package orpg.server.net.packets;

import orpg.server.data.DestinationType;

public abstract class GlobalPacket extends ServerPacket {


	@Override
	public DestinationType getDestinationType() {
		return DestinationType.GLOBAL;
	}

	@Override
	public Object getDestinationObject() {
		// TODO Auto-generated method stub
		return null;
	}


}
