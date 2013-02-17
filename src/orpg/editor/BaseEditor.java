package orpg.editor;

import java.net.Socket;

import orpg.client.data.ClientSentPacket;
import orpg.client.net.BaseClient;
import orpg.editor.net.EditorProcessThread;
import orpg.editor.net.packets.EditorEditMapPacket;
import orpg.editor.net.packets.EditorSaveMapPacket;
import orpg.shared.data.Map;
import orpg.shared.data.Pair;
import orpg.shared.net.ClientPacketType;
import orpg.shared.net.OutputByteBuffer;

public class BaseEditor extends BaseClient {

	private MapSelectWindow mapSelectWindow;

	public BaseEditor(Socket socket) {
		super(socket, new EditorProcessThread());
	}

	public void showMapSelectWindow(Pair<Integer, String>[] mapNames) {
		if (this.mapSelectWindow == null) {
			this.mapSelectWindow = new MapSelectWindow(this, mapNames);
		}
		this.mapSelectWindow.setVisible(true);
	}

	public void closeMapSelectWindow() {
		this.mapSelectWindow.setVisible(false);
	}

	public void requestEditMap(int selectedMap) {
		getOutputQueue().add(new EditorEditMapPacket(selectedMap));
	}

	public void editMap(Map map) {
		MapEditorWindow window = new MapEditorWindow(this, map);
	}

	public void saveMap(Map map) {
		getOutputQueue().add(new EditorSaveMapPacket(map));
	}
}
