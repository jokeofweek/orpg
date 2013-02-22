package orpg.editor;

import java.net.Socket;

import orpg.editor.net.EditorProcessThread;
import orpg.editor.net.packets.EditorEditMapPacket;
import orpg.editor.net.packets.EditorSaveMapPacket;
import orpg.shared.data.Map;
import orpg.shared.data.Pair;
import orpg.shared.net.AbstractClient;

public class BaseEditor extends AbstractClient {

	private MapSelectWindow mapSelectWindow;

	public BaseEditor(Socket socket) {
		super(socket, new EditorProcessThread(), null);
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
		sendPacket(new EditorEditMapPacket(selectedMap));
	}

	public void editMap(Map map) {
		MapEditorWindow window = new MapEditorWindow(this, map);
	}

	public void saveMap(Map map, boolean[][] segmentsChanged) {
		sendPacket(new EditorSaveMapPacket(map, segmentsChanged));
	}
}
