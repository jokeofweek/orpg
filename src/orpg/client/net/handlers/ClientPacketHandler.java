package orpg.client.net.handlers;

import orpg.client.data.ClientReceivedPacket;
import orpg.client.net.BaseClient;
import orpg.editor.BaseEditor;

public interface ClientPacketHandler {
	
	void handle(ClientReceivedPacket packet, BaseClient baseClient);

}
