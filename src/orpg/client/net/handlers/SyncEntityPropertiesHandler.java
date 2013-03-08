package orpg.client.net.handlers;

import com.artemis.Entity;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;

import orpg.client.BaseClient;
import orpg.client.data.ClientReceivedPacket;
import orpg.shared.data.component.SynchronizeableComponent;
import orpg.shared.net.serialize.EntitySerializer;
import orpg.shared.net.serialize.SynchronizeableComponentSerializer;

public class SyncEntityPropertiesHandler implements ClientPacketHandler {

	@Override
	public void handle(ClientReceivedPacket packet, final BaseClient client) {
		final String entityId = "" + packet.getByteBuffer().getInt();
		byte count = packet.getByteBuffer().getByte();
		
		final SynchronizeableComponent[] components = new SynchronizeableComponent[count];
		for (int i = 0; i < count; i++) {
			components[i] = packet.getByteBuffer().getValue(
					SynchronizeableComponentSerializer.getInstance());
		}

		Gdx.app.postRunnable(new Runnable() {

			@Override
			public void run() {
				Entity entity = client.getWorld().getManager(TagManager.class)
						.getEntity(entityId);

				if (entity != null) {
					for (SynchronizeableComponent component : components) {
						entity.addComponent(component);
					}
					entity.changedInWorld();
				}

			}
		});

	}

}
