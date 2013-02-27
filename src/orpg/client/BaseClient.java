package orpg.client;

import java.net.Socket;

import com.badlogic.gdx.Game;

import orpg.shared.data.AccountCharacter;
import orpg.shared.data.Map;
import orpg.shared.net.AbstractClient;
import orpg.shared.net.PacketProcessThread;
import orpg.shared.state.StateManager;

public class BaseClient extends AbstractClient {

	private Game game;
	private Map map;
	private AccountCharacter accountCharacter;

	public BaseClient(Game game, Socket socket, PacketProcessThread gameThread,
			StateManager stateManager) {
		super(socket, gameThread, stateManager);
		this.game = game;
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
		this.map = map;
	}

	public AccountCharacter getAccountCharacter() {
		return accountCharacter;
	}

	public void setAccountCharacter(AccountCharacter accountCharacter) {
		this.accountCharacter = accountCharacter;
	}

}
