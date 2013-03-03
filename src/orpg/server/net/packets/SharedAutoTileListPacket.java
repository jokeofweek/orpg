package orpg.server.net.packets;

import orpg.server.ServerSession;
import orpg.server.data.controllers.AutoTileController;
import orpg.shared.net.OutputByteBuffer;
import orpg.shared.net.ServerPacketType;

public class SharedAutoTileListPacket extends SessionPacket {

	private byte[] bytes;

	public SharedAutoTileListPacket(ServerSession session,
			AutoTileController controller) {
		super(session);
		OutputByteBuffer out = new OutputByteBuffer();
		out.putAutoTiles(controller.getAutoTiles());
		this.bytes = out.getBytes();
	}

	@Override
	public ServerPacketType getPacketType() {
		return ServerPacketType.SHARED_AUTO_TILE_LIST;
	}

	@Override
	public byte[] getBytes() {
		return this.bytes;
	}

}
