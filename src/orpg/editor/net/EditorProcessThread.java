package orpg.editor.net;

import orpg.client.data.ClientReceivedPacket;
import orpg.client.data.ClientSentPacket;
import orpg.client.net.ClientProcessThread;
import orpg.editor.BaseEditor;
import orpg.shared.net.ClientPacketType;
import orpg.shared.net.InputByteBuffer;

public class EditorProcessThread extends ClientProcessThread {

	@Override
	public void handlePacket(ClientReceivedPacket p) {
		switch (p.getType()) {
		case CONNECTED:
			getOutputQueue().add(
					new ClientSentPacket(ClientPacketType.EDITOR_LOGIN));
			break;
		case EDITOR_LOGIN_OK:
			getOutputQueue().add(
					new ClientSentPacket(ClientPacketType.EDITOR_READY));
			break;
		case EDITOR_MAP_LIST:
			InputByteBuffer in = p.getByteBuffer();
			int totalMaps = in.getInt();
			((BaseEditor) getBaseClient()).showMapSelectWindow(totalMaps);
			break;
		case EDITOR_MAP_DATA:
			handleEditorMapData(p);
		}

	}

	private void handleEditorMapData(ClientReceivedPacket p) {
		((BaseEditor) getBaseClient()).editMap(p.getByteBuffer().getMap());
	}

}
