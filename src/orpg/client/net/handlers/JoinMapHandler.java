package orpg.client.net.handlers;

import com.badlogic.gdx.Gdx;

import orpg.client.BaseClient;
import orpg.client.data.ClientReceivedPacket;
import orpg.shared.net.serialize.EntitySerializer;

public class JoinMapHandler implements ClientPacketHandler {

	@Override
	public void handle(final ClientReceivedPacket packet,
			final BaseClient client) {
		int mapId = packet.getByteBuffer().getInt();

		System.out.println("Join map!");

		// Make sure map is the same
		if (mapId == client.getMap().getId()) {
			// Add the entity in the render thread.
			Gdx.app.postRunnable(new Runnable() {
				@Override
				public void run() {
					client.getWorld().addEntity(
							packet.getByteBuffer().getValue(
									EntitySerializer.getInstance(client
											.getWorld())));
				}
			});
		}

	}

}
