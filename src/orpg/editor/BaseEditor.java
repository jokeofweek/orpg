package orpg.editor;

import java.net.Socket;
import java.util.HashMap;

import orpg.editor.controller.MapController;
import orpg.editor.net.EditorProcessThread;
import orpg.editor.net.packets.EditorEditMapPacket;
import orpg.editor.net.packets.EditorSaveMapPacket;
import orpg.server.config.ServerConfigurationManager;
import orpg.shared.config.ConfigurationManager;
import orpg.shared.data.Map;
import orpg.shared.data.Pair;
import orpg.shared.net.AbstractClient;

public class BaseEditor extends AbstractClient {

	private MapSelectWindow mapSelectWindow;
	private HashMap<Integer, MapController> mapControllers;
	private EditorConfigurationManager config;

	public BaseEditor(Socket socket, EditorConfigurationManager config) {
		super(socket, new EditorProcessThread(), null);
		this.mapControllers = new HashMap<Integer, MapController>();
		this.config = config;
	}

	public EditorConfigurationManager getConfigManager() {
		return this.config;
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
		if (getMapController(map.getId()) == null) {
			MapController controller = new MapController(this, map);
			mapControllers.put(map.getId(), controller);

		}
		MapEditorWindow window = new MapEditorWindow(this,
				getMapController(map.getId()));
	}

	public MapController getMapController(int id) {
		return mapControllers.get(id);
	}

	public void saveMap(Map map, boolean[][] segmentsChanged) {
		sendPacket(new EditorSaveMapPacket(map, segmentsChanged));
	}
}
