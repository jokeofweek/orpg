package orpg.server.net.packets;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;

import orpg.server.ServerSession;
import orpg.server.data.DestinationType;
import orpg.shared.component.Position;
import orpg.shared.data.AccountCharacter;
import orpg.shared.data.Map;
import orpg.shared.net.ServerPacketType;
import orpg.shared.net.serialize.OutputByteBuffer;

public class ClientLeftMapPacket extends MapExceptForPacket {

	private byte[] bytes;

	/**
	 * This sends a packet to an entity's map saying that the entity has left
	 * the map.
	 * 
	 * @param serverSession
	 *            the session of the entity
	 * @param map
	 * 			  the map the entity is leaving
	 * @param entity
	 *            the entity that has left.
	 */
	public ClientLeftMapPacket(ServerSession serverSession, Map map,
			Entity entity) {
		super(serverSession, map);
		OutputByteBuffer out = new OutputByteBuffer(8);

		out.putInt(map.getId()); // put this for sanity check on client side
		out.putInt(entity.getId());
		this.bytes = out.getBytes();

	}

	@Override
	public ServerPacketType getPacketType() {
		return ServerPacketType.CLIENT_LEFT_MAP;
	}

	@Override
	public byte[] getBytes() {
		return this.bytes;
	}

}
