package orpg.server.net.packets;

import com.artemis.ComponentType;
import com.artemis.Entity;

import orpg.server.ServerSession;
import orpg.shared.data.component.Position;
import orpg.shared.data.component.SynchronizeableComponent;
import orpg.shared.net.ServerPacketType;
import orpg.shared.net.serialize.OutputByteBuffer;

public class ClientSyncEntity extends SessionPacket {

	private byte[] bytes;

	public ClientSyncEntity(ServerSession session, Entity entity,
			Class<SynchronizeableComponent>... components) {
		super(session);
		OutputByteBuffer out = new OutputByteBuffer();
		out.putInt(entity.getId());
		out.putByte((byte) components.length);

		for (Class<SynchronizeableComponent> type : components) {
			out.putValue((SynchronizeableComponent) entity
					.getComponent(ComponentType.getTypeFor(type)));
		}
		
		this.bytes = out.getBytes();
	}

	@Override
	public ServerPacketType getPacketType() {
		return ServerPacketType.CLIENT_SYNC_ENTITY_PROPERTIES;
	}

	@Override
	public byte[] getBytes() {
		return bytes;
	}
}
