package orpg.client;

import java.net.Socket;
import java.util.HashMap;

import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.PlayerManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Game;

import orpg.client.config.ClientConfigurationManager;
import orpg.client.data.controllers.AutoTileController;
import orpg.client.data.entity.EntityPreprocessor;
import orpg.client.data.store.DataStore;
import orpg.client.data.store.FileDataStore;
import orpg.client.net.SegmentRequestManager;
import orpg.client.systems.AnimationSystem;
import orpg.client.systems.InputSystem;
import orpg.client.systems.MovementSystem;
import orpg.client.systems.RenderSystem;
import orpg.shared.data.AccountCharacter;
import orpg.shared.data.AutoTileType;
import orpg.shared.data.Map;
import orpg.shared.net.AbstractClient;
import orpg.shared.net.PacketProcessThread;
import orpg.shared.state.StateManager;

public class BaseClient extends AbstractClient {

	private Game game;
	private Map map;
	private Map localMap;
	private AccountCharacter accountCharacter;
	private ClientConfigurationManager config;
	private DataStore dataStore;
	private SegmentRequestManager segmentRequestManager;
	private AutoTileController autoTileController;
	private World world;
	private boolean isChangingMap = true;
	
	public BaseClient(Game game, Socket socket,
			PacketProcessThread gameThread, StateManager stateManager,
			ClientConfigurationManager config) {
		super(socket, gameThread, stateManager);
		this.game = game;
		this.config = config;
		this.dataStore = new FileDataStore(this);
		this.segmentRequestManager = new SegmentRequestManager(this);
		this.autoTileController = new AutoTileController(this);
		this.resetWorld();

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

	public AutoTileController getAutoTileController() {
		return autoTileController;
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

	public World getWorld() {
		return world;
	}

	public void resetWorld() {
		this.world = new World();
		this.world.setManager(new GroupManager());
		this.world.setManager(new PlayerManager());
		this.world.setManager(new TagManager());
		this.world.setManager(new EntityPreprocessor(this));
		this.world.setSystem(new InputSystem(this));
		this.world.setSystem(new MovementSystem(this));
		this.world.setSystem(new AnimationSystem(this));
		this.world.setSystem(new RenderSystem(this), true);
		this.world.initialize();
	}
	
	public boolean isChangingMap() {
		return isChangingMap;
	}
	
	public void setChangingMap(boolean isChangingMap) {
		this.isChangingMap = isChangingMap;
	}

}
