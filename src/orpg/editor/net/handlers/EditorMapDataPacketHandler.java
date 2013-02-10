package orpg.editor.net.handlers;

import orpg.client.data.ClientReceivedPacket;
import orpg.editor.BaseEditor;
import orpg.editor.MapEditorWindow;
import orpg.shared.data.Map;

public class EditorMapDataPacketHandler implements EditorPacketHandler {

	@Override
	public void handle(ClientReceivedPacket packet, BaseEditor baseEditor) {
		Map map = packet.getByteBuffer().getMap();
		baseEditor.editMap(map);

	}

}
