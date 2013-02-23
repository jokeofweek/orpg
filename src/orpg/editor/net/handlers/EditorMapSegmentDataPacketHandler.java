package orpg.editor.net.handlers;

import orpg.client.data.ClientReceivedPacket;
import orpg.editor.BaseEditor;
import orpg.editor.controller.MapController;
import orpg.shared.data.Segment;

public class EditorMapSegmentDataPacketHandler implements EditorPacketHandler {

	@Override
	public void handle(ClientReceivedPacket packet, BaseEditor baseEditor) {
		packet.getByteBuffer().decompress();
		int mapId = packet.getByteBuffer().getInt();
		Segment segment = packet.getByteBuffer().getSegment();

		// Notify the map controller that we received the segment
		MapController controller = baseEditor.getMapController(mapId);
		if (controller != null) {
			controller.getRequestManager().receivedResponse(segment);
		}
	}

}
