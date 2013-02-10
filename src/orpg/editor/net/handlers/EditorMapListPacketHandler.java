package orpg.editor.net.handlers;

import orpg.client.data.ClientReceivedPacket;
import orpg.editor.BaseEditor;
import orpg.shared.net.InputByteBuffer;

public class EditorMapListPacketHandler implements EditorPacketHandler {

	@Override
	public void handle(ClientReceivedPacket packet, BaseEditor baseEditor) {
		InputByteBuffer in = packet.getByteBuffer();
		int totalMaps = in.getInt();
		baseEditor.showMapSelectWindow(totalMaps);
	}

}
