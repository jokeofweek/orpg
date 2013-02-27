package orpg.client.net.handlers;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;

import orpg.client.BaseClient;
import orpg.client.data.ClientReceivedPacket;
import orpg.client.net.packets.LoadMapPacket;
import orpg.client.state.CharacterSelectState;
import orpg.client.state.LoadingState;
import orpg.shared.net.AbstractClient;
import orpg.shared.net.InputByteBuffer;

public class LoginOkHandler implements ClientPacketHandler {

	@Override
	public void handle(ClientReceivedPacket packet, final BaseClient client) {

		// Get list of characters
		InputByteBuffer in = packet.getByteBuffer();
		byte totalCharacters = in.getByte();
		final List<String> characters = new ArrayList<String>(totalCharacters);

		for (int i = 0; i < totalCharacters; i++) {
			characters.add(in.getString());
		}

		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				client.getStateManager().switchState(
						new CharacterSelectState(client, characters));
			}
		});
	}
}
