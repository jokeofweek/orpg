package orpg.editor.net.handlers;

import orpg.client.data.ClientReceivedPacket;
import orpg.editor.BaseEditor;
import orpg.shared.data.Pair;
import orpg.shared.net.serialize.InputByteBuffer;

public class EditorMapListPacketHandler implements EditorPacketHandler {

	@Override
	public void handle(ClientReceivedPacket packet, BaseEditor baseEditor) {
		InputByteBuffer in = packet.getByteBuffer();
		int totalMaps = in.getInt();
		Pair<Integer, String>[] mapNames = new Pair[totalMaps];

		for (int i = 0; i < totalMaps; i++) {
			mapNames[i] = new Pair<Integer, String>(i + 1, in.getString());
		}

		baseEditor.setAutoTiles(packet.getByteBuffer().getAutoTiles());

		baseEditor.showMapSelectWindow(mapNames);
	}

}
