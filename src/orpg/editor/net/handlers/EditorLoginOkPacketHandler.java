package orpg.editor.net.handlers;

import orpg.editor.net.packets.EditorReadyPacket;

public class EditorLoginOkPacketHandler implements EditorPacketHandler {

	public void handle(orpg.client.data.ClientReceivedPacket packet,
			orpg.editor.BaseEditor baseEditor) {
		baseEditor.sendPacket(new EditorReadyPacket());
	}

}
