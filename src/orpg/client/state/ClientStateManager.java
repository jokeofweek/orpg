package orpg.client.state;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import orpg.shared.state.State;
import orpg.shared.state.StateManager;

public class ClientStateManager extends StateManager  {

	private Game game;

	public ClientStateManager(Game game) {
		this.game = game;
	}

	public Game getGame() {
		return game;
	}
	
	@Override
	protected void onChangeState() {
		try {
			// If we switched state and it is a screen, update the game screen
			State state = getCurrentState();

			if (state instanceof Screen) {
				game.setScreen((Screen) state);
				setChanged();
				notifyObservers();
			}
		} catch (IllegalStateException e) {
			// No state
		}
	}

}
