package orpg.client.net.handlers;

import com.badlogic.gdx.Gdx;

import orpg.client.BaseClient;
import orpg.client.data.ClientPlayerData;
import orpg.client.data.ClientReceivedPacket;
import orpg.shared.data.AccountCharacter;
import orpg.shared.data.Direction;

public class MoveHandler implements ClientPacketHandler {

	@Override
	public void handle(ClientReceivedPacket packet, BaseClient client) {
		String name = packet.getByteBuffer().getString();
		final Direction direction = Direction.values()[packet.getByteBuffer()
				.getByte()];

		final ClientPlayerData playerData = client.getClientPlayerData(name);

		Gdx.app.postRunnable(new Runnable() {

			@Override
			public void run() {
				playerData.move(direction);
			}
		});

	}

}
