package orpg.shared.data.component;

import orpg.server.BaseServer;
import orpg.server.event.EntityWarpToPositionMovementEvent;
import orpg.server.systems.MovementSystem;
import orpg.shared.data.annotations.Attachable;
import orpg.shared.data.annotations.Editable;
import orpg.shared.data.component.BasicCollidable.Serializer;
import orpg.shared.net.serialize.InputByteBuffer;
import orpg.shared.net.serialize.OutputByteBuffer;
import orpg.shared.net.serialize.ValueSerializer;

import com.artemis.Entity;

@Attachable(name = "Warp", description = "Warps an entity to another location.")
public class WarpCollidable extends Collidable {

	@Editable(name = "Map ID", description = "The map to warp to.")
	public int mapId;

	@Editable(name = "X", description = "The x position to warp to.")
	public int x;

	@Editable(name = "Y", description = "The y position to warp to.")
	public int y;

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public int getMapId() {
		return mapId;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getX() {
		return x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getY() {
		return y;
	}

	@Override
	public void onCollision(BaseServer baseServer, Entity componentOwner,
			Entity collidingEntity) {
		// Only warp if it is a player
		if (baseServer.getWorld().getMapper(IsPlayer.class)
				.getSafe(collidingEntity) != null) {
			baseServer
					.getWorld()
					.getSystem(MovementSystem.class)
					.addEvent(
							new EntityWarpToPositionMovementEvent(
									collidingEntity, mapId, x, y));
		}
	}

	@Override
	public boolean isPassable() {
		return true;
	}

	@Override
	public SerializeableComponentType getType() {
		return SerializeableComponentType.WARP_COLLIDABLE;
	}

	public static class Serializer implements
			ValueSerializer<SerializableComponent> {

		private static Serializer instance = new Serializer();

		private Serializer() {
		};

		public static Serializer getInstance() {
			return instance;
		}

		@Override
		public void put(OutputByteBuffer out, SerializableComponent obj) {
			WarpCollidable collidable = (WarpCollidable) obj;
			out.putInt(collidable.getMapId());
			out.putInt(collidable.getX());
			out.putInt(collidable.getY());
		}

		@Override
		public WarpCollidable get(InputByteBuffer in) {
			WarpCollidable collidable = new WarpCollidable();
			collidable.setMapId(in.getInt());
			collidable.setX(in.getInt());
			collidable.setY(in.getInt());
			return collidable;
		}

	}
}
