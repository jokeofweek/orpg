package orpg.client.net.handlers;

import orpg.client.BaseClient;
import orpg.client.data.ClientReceivedPacket;

public interface ClientPacketHandler {
	
	void handle(ClientReceivedPacket packet, BaseClient client);

}
