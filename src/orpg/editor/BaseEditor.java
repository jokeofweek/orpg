package orpg.editor;

import java.net.Socket;

import orpg.client.data.ClientSentPacket;
import orpg.client.net.BaseClient;
import orpg.editor.net.EditorProcessThread;
import orpg.shared.data.Map;
import orpg.shared.net.ClientPacketType;
import orpg.shared.net.OutputByteBuffer;

public class BaseEditor extends BaseClient {

	private MapSelectWindow mapSelectWindow;

	public BaseEditor(Socket socket) {
		super(socket, EditorProcessThread.class);
	}

	public void showMapSelectWindow(int totalMaps) {
		if (this.mapSelectWindow == null) {
			this.mapSelectWindow = new MapSelectWindow(this, totalMaps);
		}
		this.mapSelectWindow.setVisible(true);
	}

	public void closeMapSelectWindow() {
		this.mapSelectWindow.setVisible(false);
	}

	public void requestEditMap(int selectedMap) {
		OutputByteBuffer out = new OutputByteBuffer();
		out.putInt(selectedMap);
		getOutputQueue().add(
				new ClientSentPacket(ClientPacketType.EDITOR_EDIT_MAP, out
						.getBytes()));
	}

	public void editMap(Map map) {
		MapEditorWindow window = new MapEditorWindow(this, map);
	}

	public void saveMap(Map map) {
		OutputByteBuffer out = new OutputByteBuffer();
		out.putMap(map);
		getOutputQueue().add(
				new ClientSentPacket(ClientPacketType.EDITOR_MAP_SAVE, out
						.getBytes()));
	}
}
