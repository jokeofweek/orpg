package orpg.editor.net.handlers;

import orpg.client.data.ClientReceivedPacket;
import orpg.editor.BaseEditor;

public interface EditorPacketHandler {

	void handle(ClientReceivedPacket packet, BaseEditor baseEditor);
	
}
