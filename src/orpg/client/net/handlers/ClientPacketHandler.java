package orpg.client.net.handlers;

import orpg.client.BaseClient;
import orpg.client.data.ClientReceivedPacket;
import orpg.shared.net.AbstractClient;

public interface ClientPacketHandler {
	
	void handle(ClientReceivedPacket packet, BaseClient client);

}
