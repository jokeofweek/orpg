package orpg.editor.net;

import java.util.HashMap;

import orpg.client.data.ClientReceivedPacket;
import orpg.client.data.ClientSentPacket;
import orpg.editor.BaseEditor;
import orpg.editor.net.handlers.ConnectedPacketHandler;
import orpg.editor.net.handlers.EditorPacketHandler;
import orpg.editor.net.handlers.EditorLoginOkPacketHandler;
import orpg.editor.net.handlers.EditorMapDataPacketHandler;
import orpg.editor.net.handlers.EditorMapListPacketHandler;
import orpg.shared.net.ClientPacketType;
import orpg.shared.net.InputByteBuffer;
import orpg.shared.net.PacketProcessThread;
import orpg.shared.net.ServerPacketType;

public class EditorProcessThread extends PacketProcessThread {

	private HashMap<ServerPacketType, EditorPacketHandler> handlers;

	public EditorProcessThread() {
		setupHandlers();
	}

	private void setupHandlers() {
		this.handlers = new HashMap<ServerPacketType, EditorPacketHandler>();
		handlers.put(ServerPacketType.CONNECTED, new ConnectedPacketHandler());
		handlers.put(ServerPacketType.EDITOR_LOGIN_OK,
				new EditorLoginOkPacketHandler());
		handlers.put(ServerPacketType.EDITOR_MAP_LIST,
				new EditorMapListPacketHandler());
		handlers.put(ServerPacketType.EDITOR_MAP_DATA,
				new EditorMapDataPacketHandler());
	}

	@Override
	public void handlePacket(ClientReceivedPacket packet) {
		EditorPacketHandler handler = handlers.get(packet.getType());
		if (handler != null) {
			handler.handle(packet, (BaseEditor) getBaseClient());
		} else {
			System.err.println("[EDITOR] No handler setup for packet "
					+ packet.getType());
		}
	}

}
