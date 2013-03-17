package orpg.server.net.packets;

import java.beans.DesignMode;

import com.artemis.ComponentType;
import com.artemis.Entity;

import orpg.server.ServerSession;
import orpg.server.net.DestinationType;
import orpg.shared.data.component.Position;
import orpg.shared.data.component.SynchronizebleComponent;
import orpg.shared.net.ServerPacketType;
import orpg.shared.net.serialize.OutputByteBuffer;
import sun.security.krb5.internal.crypto.Des;

public class ClientSyncEntityPropertiesPacket extends MapExceptForPacket {

	private byte[] bytes;

	public ClientSyncEntityPropertiesPacket(ServerSession session, Entity entity,
			boolean ignoreSession,
			Class<? extends SynchronizebleComponent>... components) {
		super(ignoreSession ? session : null, entity.getComponent(
				Position.class).getMap());

		OutputByteBuffer out = new OutputByteBuffer();
		out.putInt(entity.getId());
		out.putByte((byte) components.length);

		for (Class<? extends SynchronizebleComponent> component : components) {
			out.putValue((SynchronizebleComponent) entity
					.getComponent(ComponentType.getTypeFor(component)));
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
