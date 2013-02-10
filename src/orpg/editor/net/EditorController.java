package orpg.editor.net;

import orpg.client.data.ClientSentPacket;
import orpg.client.net.BaseClient;
import orpg.editor.BaseEditor;
import orpg.shared.data.MapSaveData;
import orpg.shared.data.Segment;
import orpg.shared.net.ClientPacketType;
import orpg.shared.net.OutputByteBuffer;

public class EditorController {

	private BaseEditor baseEditor;

	public EditorController(BaseEditor baseEditor) {
		this.baseEditor = baseEditor;
	}

	public void saveMapSegments(Segment[] segments) {
		OutputByteBuffer out = new OutputByteBuffer();
		out.putMapSaveData(new MapSaveData(segments));
		this.baseEditor.getOutputQueue().add(
				new ClientSentPacket(ClientPacketType.EDITOR_MAP_SAVE, out
						.getBytes()));
	}

}
