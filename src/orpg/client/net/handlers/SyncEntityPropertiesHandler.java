package orpg.client.net.handlers;

import com.artemis.Entity;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;

import orpg.client.BaseClient;
import orpg.client.data.ClientReceivedPacket;
import orpg.client.data.component.AnimatedPlayer;
import orpg.client.systems.MovementSystem;
import orpg.shared.data.component.Moveable;
import orpg.shared.data.component.Position;
import orpg.shared.data.component.SerializableComponent;
import orpg.shared.data.component.SynchronizebleComponent;
import orpg.shared.net.serialize.EntitySerializer;

public class SyncEntityPropertiesHandler implements ClientPacketHandler {

	@Override
	public void handle(ClientReceivedPacket packet, final BaseClient client) {
		final String entityId = "" + packet.getByteBuffer().getInt();
		byte count = packet.getByteBuffer().getByte();

		final SerializableComponent[] components = new SerializableComponent[count];
		for (int i = 0; i < count; i++) {
			components[i] = packet.getByteBuffer().getValue(
					SerializableComponent.Serializer.getInstance());
		}

		Gdx.app.postRunnable(new Runnable() {

			@Override
			public void run() {
				Entity entity = client.getWorld().getManager(TagManager.class)
						.getEntity(entityId);

				boolean stopMovement = false;

				if (entity != null) {
					for (SerializableComponent component : components) {
						// special case for position
						if (component instanceof Position) {
							Position old = entity.getComponent(Position.class);
							Position newer = (Position) component;
							entity.getWorld()
									.getSystem(MovementSystem.class)
									.updateEntitySegment(entity, old.getX(),
											old.getY(), newer.getX(),
											newer.getY());

							stopMovement = true;
						}

						entity.addComponent(component);

					}

					// Stop movement, once everything is done.
					if (stopMovement) {
						AnimatedPlayer animatedPlayer = entity.getWorld()
								.getMapper(AnimatedPlayer.class)
								.getSafe(entity);
						if (animatedPlayer != null) {
							animatedPlayer.setAnimating(false);
						}
						Moveable moveable = entity.getWorld()
								.getMapper(Moveable.class).getSafe(entity);
						if (moveable != null) {
							moveable.setMoveProcessed(true);
							moveable.setMoving(false);
						}
					}

					entity.changedInWorld();
				}

			}
		});

	}

}
