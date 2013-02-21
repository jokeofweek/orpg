package orpg.client.net.handlers;

import com.badlogic.gdx.Gdx;

import orpg.client.data.ClientReceivedPacket;
import orpg.client.state.LoadingState;
import orpg.shared.net.AbstractClient;

public class LoginOkHandler implements ClientPacketHandler {

	@Override
	public void handle(ClientReceivedPacket packet, final AbstractClient client) {

		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				client.getStateManager().switchState(new LoadingState());
			}
		});
	}

}
