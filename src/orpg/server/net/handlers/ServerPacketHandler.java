package orpg.server.net.handlers;

import orpg.server.BaseServer;
import orpg.server.data.ServerReceivedPacket;

public interface ServerPacketHandler {

	public void handle(ServerReceivedPacket packet, BaseServer baseServer);

}
