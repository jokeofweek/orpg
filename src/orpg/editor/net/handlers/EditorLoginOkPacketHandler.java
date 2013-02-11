package orpg.editor.net.handlers;

import orpg.client.data.ClientSentPacket;
import orpg.editor.net.packets.EditorReadyPacket;
import orpg.shared.net.ClientPacketType;

public class EditorLoginOkPacketHandler implements EditorPacketHandler {

	public void handle(orpg.client.data.ClientReceivedPacket packet,
			orpg.editor.BaseEditor baseEditor) {
		baseEditor.getOutputQueue().add(new EditorReadyPacket());

	}

}
