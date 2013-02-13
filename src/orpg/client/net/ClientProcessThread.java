package orpg.client.net;

import java.util.HashMap;

import orpg.client.ClientWindow;
import orpg.client.data.ClientReceivedPacket;
import orpg.client.net.handlers.ClientPacketHandler;
import orpg.client.net.handlers.ErrorPacketHandler;
import orpg.editor.BaseEditor;
import orpg.editor.net.handlers.EditorPacketHandler;
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
	}

	@Override
	public void handlePacket(ClientReceivedPacket packet) {
		ClientPacketHandler handler = handlers.get(packet.getType());
		if (handler != null) {
			handler.handle(packet, getBaseClient());
		} else {
			System.err.println("[CLIENT] No handler setup for packet "
					+ packet.getType());
		}
	}

}
