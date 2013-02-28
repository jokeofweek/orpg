package orpg.client.net.handlers;

import com.badlogic.gdx.Gdx;

import orpg.client.BaseClient;
import orpg.client.data.ClientReceivedPacket;
import orpg.shared.data.AccountCharacter;

public class LeftMapHandler implements ClientPacketHandler {

	@Override
	public void handle(ClientReceivedPacket packet, final BaseClient client) {
		int mapId = packet.getByteBuffer().getInt();
		final AccountCharacter character = packet.getByteBuffer()
				.getMapCharacter();

		// Make sure map is the same
		if (mapId == client.getMap().getId()) {
			character.setMap(client.getMap());
			// Add the entity in the render thread.
			Gdx.app.postRunnable(new Runnable() {
				@Override
				public void run() {
					client.getMap().removePlayer(character);
				}
			});
		}

	}
}
