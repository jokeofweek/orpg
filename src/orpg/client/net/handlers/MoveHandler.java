package orpg.client.net.handlers;

import com.badlogic.gdx.Gdx;

import orpg.client.BaseClient;
import orpg.client.data.ClientPlayerData;
import orpg.client.data.ClientReceivedPacket;
import orpg.shared.data.Direction;

public class MoveHandler implements ClientPacketHandler {

	@Override
	public void handle(ClientReceivedPacket packet, BaseClient client) {
		String name = packet.getByteBuffer().getString();
		final Direction direction = Direction.values()[packet.getByteBuffer()
				.getByte()];

		final ClientPlayerData playerData = client.getClientPlayerData(name);

		// Check if we have the current player loaded, and if so update them
		if (playerData != null) {
			Gdx.app.postRunnable(new Runnable() {

				@Override
				public void run() {
					playerData.move(direction);
				}
			});
		}

	}

}
