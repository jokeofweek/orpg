package orpg.client.net.handlers;

import com.artemis.Entity;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;

import orpg.client.BaseClient;
import orpg.client.data.ClientReceivedPacket;
import orpg.shared.data.AccountCharacter;

public class LeftMapHandler implements ClientPacketHandler {

	@Override
	public void handle(ClientReceivedPacket packet, final BaseClient client) {
		int mapId = packet.getByteBuffer().getInt();
		final int entityToRemove = packet.getByteBuffer().getInt();

		System.out.println("Leave map.");
		
		// Make sure map is the same
		if (mapId == client.getMap().getId()) {
			// Add the entity in the render thread.
			Gdx.app.postRunnable(new Runnable() {
				@Override
				public void run() {
					Entity entity = client.getWorld()
							.getManager(TagManager.class)
							.getEntity(entityToRemove + "");
					if (entity != null) {
						client.getWorld().getManager(TagManager.class).unregister(entityToRemove + "");
						client.getWorld().deleteEntity(entity);
					}
				}
			});
		}

	}
}
