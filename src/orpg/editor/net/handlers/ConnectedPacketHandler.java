package orpg.editor.net.handlers;

import orpg.client.data.ClientReceivedPacket;
import orpg.client.net.packets.LoginPacket;
import orpg.editor.BaseEditor;

public class ConnectedPacketHandler implements EditorPacketHandler {

	@Override
	public void handle(ClientReceivedPacket packet, BaseEditor baseEditor) {
		baseEditor.sendPacket(
				new LoginPacket("test", "tester".toCharArray(), true));
	}

}
