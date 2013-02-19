package orpg.client.net.handlers;

import orpg.client.data.ClientReceivedPacket;
import orpg.client.net.BaseClient;

public interface ClientPacketHandler {
	
	void handle(ClientReceivedPacket packet, BaseClient baseClient);

}
