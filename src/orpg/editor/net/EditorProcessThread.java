package orpg.editor.net;

import orpg.client.data.ClientReceivedPacket;
import orpg.client.data.ClientSentPacket;
import orpg.client.net.ClientProcessThread;
import orpg.editor.MapEditorWindow;
import orpg.shared.net.ClientPacketType;

public class EditorProcessThread extends ClientProcessThread {

	@Override
	public void handlePacket(ClientReceivedPacket p) {
		switch (p.getType()) {
		case CONNECTED:
			getOutputQueue().add(new ClientSentPacket(ClientPacketType.LOGIN_EDITOR))	;
			break;
		case LOGIN_EDITOR_OK:
			new MapEditorWindow(getBaseClient());
			getOutputQueue().add(new ClientSentPacket(ClientPacketType.PING));
			break;
		}

	}

}
