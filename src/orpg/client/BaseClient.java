package orpg.client;

import java.net.Socket;
import java.util.HashMap;

import com.badlogic.gdx.Game;

import orpg.client.config.ClientConfigurationManager;
import orpg.client.data.ClientPlayerData;
import orpg.client.data.store.DataStore;
import orpg.client.data.store.FileDataStore;
import orpg.client.net.SegmentRequestManager;
import orpg.editor.config.EditorConfigurationManager;
import orpg.shared.config.ConfigurationManager;
import orpg.shared.data.AccountCharacter;
import orpg.shared.data.Map;
import orpg.shared.net.AbstractClient;
import orpg.shared.net.PacketProcessThread;
import orpg.shared.state.StateManager;

public class BaseClient extends AbstractClient {

	private Game game;
	private Map map;
	private Map localMap;
	private AccountCharacter accountCharacter;
	private HashMap<String, ClientPlayerData> playersData;
	private ClientConfigurationManager config;
	private DataStore dataStore;
	private SegmentRequestManager segmentRequestManager;

	public BaseClient(Game game, Socket socket, PacketProcessThread gameThread,
			StateManager stateManager, ClientConfigurationManager config) {
		super(socket, gameThread, stateManager);
		this.game = game;
		this.playersData = new HashMap<String, ClientPlayerData>();
		this.config = config;
		this.dataStore = new FileDataStore(this);
		this.segmentRequestManager = new SegmentRequestManager(this);
	}

	public ClientConfigurationManager getConfigManager() {
		return this.config;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		if (this.map != map) {
			segmentRequestManager.reset();
		}
		this.map = map;
	}

	public Map getLocalMap() {
		return localMap;
	}

	public void setLocalMap(Map localMap) {
		this.localMap = localMap;
	}

	public DataStore getDataStore() {
		return dataStore;
	}

	public SegmentRequestManager getSegmentRequestManager() {
		return segmentRequestManager;
	}

	public AccountCharacter getAccountCharacter() {
		return accountCharacter;
	}

	public void setAccountCharacter(AccountCharacter accountCharacter) {
		this.accountCharacter = accountCharacter;
	}

	public ClientPlayerData getClientPlayerData(String name) {
		return playersData.get(name);
	}

	public void clearClientPlayerData() {
		this.playersData.clear();
	}

	public void addClientPlayerData(String name, ClientPlayerData playerData) {
		this.playersData.put(name, playerData);
	}

	public void removeClientPlayerData(String name) {
		this.playersData.remove(name);
	}

}
