package orpg.editor.net.handlers;

import orpg.client.data.ClientReceivedPacket;
import orpg.client.data.ClientSentPacket;
import orpg.client.net.packets.LoginPacket;
import orpg.editor.BaseEditor;
import orpg.shared.net.ClientPacketType;

public class ConnectedPacketHandler implements EditorPacketHandler {

	@Override
	public void handle(ClientReceivedPacket packet, BaseEditor baseEditor) {
		baseEditor.getOutputQueue().add(new LoginPacket());
	}

}
