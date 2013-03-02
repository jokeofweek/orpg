package orpg.editor.net.handlers;

import orpg.client.data.ClientReceivedPacket;
import orpg.editor.BaseEditor;
import orpg.shared.data.Map;

public class EditorMapDataPacketHandler implements EditorPacketHandler {

	@Override
	public void handle(ClientReceivedPacket packet, BaseEditor baseEditor) {
		packet.getByteBuffer().decompress();
		Map map = packet.getByteBuffer().getMapDescriptor();
		map.updateSegment(packet.getByteBuffer().getSegment(), false);
		baseEditor.closeMapSelectWindow();
		baseEditor.editMap(map);
	}

}
