package orpg.client.net;

import java.util.HashMap;

import orpg.client.BaseClient;
import orpg.client.data.ClientReceivedPacket;
import orpg.client.net.handlers.ClientPacketHandler;
import orpg.client.net.handlers.ErrorPacketHandler;
import orpg.client.net.handlers.LoginOkHandler;
import orpg.client.net.handlers.MapDataHandler;
import orpg.shared.net.AbstractClient;
import orpg.shared.net.PacketProcessThread;
import orpg.shared.net.ServerPacketType;

public class ClientProcessThread extends PacketProcessThread {

	private HashMap<ServerPacketType, ClientPacketHandler> handlers;

	public ClientProcessThread() {
		this.setupHandlers();
	}

	private void setupHandlers() {
		this.handlers = new HashMap<ServerPacketType, ClientPacketHandler>();
		this.handlers.put(ServerPacketType.ERROR, new ErrorPacketHandler());
		this.handlers.put(ServerPacketType.LOGIN_OK, new LoginOkHandler());
		this.handlers.put(ServerPacketType.CLIENT_MAP_DATA,
				new MapDataHandler());
	}

	@Override
	public void handlePacket(ClientReceivedPacket packet) {
		ClientPacketHandler handler = handlers.get(packet.getType());
		if (handler != null) {
			handler.handle(packet, (BaseClient) getClient());
		} else {
			System.err.println("[CLIENT] No handler setup for packet "
					+ packet.getType());
		}
	}

}
