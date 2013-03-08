package orpg.client.net.handlers;

import com.artemis.Entity;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;

import orpg.client.BaseClient;
import orpg.client.data.ClientReceivedPacket;
import orpg.shared.data.Direction;
import orpg.shared.data.component.Moveable;

public class MoveHandler implements ClientPacketHandler {

	@Override
	public void handle(ClientReceivedPacket packet, final BaseClient client) {
		final String entityId = "" + packet.getByteBuffer().getInt();
		final Direction direction = Direction.values()[packet.getByteBuffer()
				.getByte()];

		// Check if we have the current player loaded, and if so update them
		Gdx.app.postRunnable(new Runnable() {

			@Override
			public void run() {
				Entity entity = client.getWorld().getManager(TagManager.class)
						.getEntity(entityId);

				if (entity != null) {
					Moveable moveable = entity.getComponent(Moveable.class);

					if (moveable != null) {
						moveable.setDirection(direction);
						moveable.setMoveProcessed(false);
						moveable.setMoving(true);
					}
				}
			}
		});

	}
}
