package orpg.client.net;

import java.util.HashMap;

import orpg.client.BaseClient;
import orpg.client.data.ClientReceivedPacket;
import orpg.client.net.handlers.ClientPacketHandler;
import orpg.client.net.handlers.ErrorPacketHandler;
import orpg.client.net.handlers.InGameHandler;
import orpg.client.net.handlers.LoginOkHandler;
import orpg.client.net.handlers.NewMapHandler;
import orpg.client.net.handlers.SegmentDataHandler;
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
		this.handlers.put(ServerPacketType.CLIENT_IN_GAME, new InGameHandler());
		this.handlers.put(ServerPacketType.CLIENT_NEW_MAP, new NewMapHandler());
		this.handlers.put(ServerPacketType.CLIENT_SEGMENT_DATA,
				new SegmentDataHandler());
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
