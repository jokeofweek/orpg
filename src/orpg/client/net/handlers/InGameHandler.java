package orpg.client.net.handlers;

import com.badlogic.gdx.Gdx;

import orpg.client.BaseClient;
import orpg.client.data.ClientReceivedPacket;
import orpg.client.state.LoadingState;

public class InGameHandler implements ClientPacketHandler {

	@Override
	public void handle(ClientReceivedPacket packet, final BaseClient client) {
		// Update the client
		client.setAccountCharacter(packet.getByteBuffer().getAccountCharacter());
		client.setAutoTiles(packet.getByteBuffer().getAutoTiles());

		// Switch to loading
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				client.getStateManager().switchState(new LoadingState());
			}
		});

	}

}
