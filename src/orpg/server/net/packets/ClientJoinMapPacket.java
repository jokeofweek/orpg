package orpg.server.net.packets;

import com.artemis.Entity;

import orpg.server.ServerSession;
import orpg.shared.data.AccountCharacter;
import orpg.shared.data.Map;
import orpg.shared.net.ServerPacketType;
import orpg.shared.net.serialize.EntitySerializer;
import orpg.shared.net.serialize.OutputByteBuffer;

public class ClientJoinMapPacket extends MapExceptForPacket {

	private byte[] bytes;

	/**
	 * This sends a packet to the a map saying that an entity has joined the
	 * map.
	 * 
	 * @param serverSession
	 *            the session of the entity
	 * @param map
	 *            the map the entity is joining
	 * @param entity
	 *            the entity that has joined.
	 */
	public ClientJoinMapPacket(ServerSession serverSession, Map map,
			Entity entity) {
		super(serverSession, map);

		OutputByteBuffer out = new OutputByteBuffer(10);
		out.putInt(map.getId()); // put this for sanity check on client side
		out.putValue(entity, EntitySerializer.getInstance(entity.getWorld()));
		this.bytes = out.getBytes();

	}

	@Override
	public ServerPacketType getPacketType() {
		return ServerPacketType.CLIENT_JOIN_MAP;
	}

	@Override
	public byte[] getBytes() {
		return this.bytes;
	}

}
