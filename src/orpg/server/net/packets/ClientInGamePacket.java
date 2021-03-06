package orpg.server.net.packets;

import orpg.server.ServerSession;
import orpg.server.data.controllers.AutoTileController;
import orpg.shared.data.AccountCharacter;
import orpg.shared.net.ServerPacketType;
import orpg.shared.net.serialize.OutputByteBuffer;

public class ClientInGamePacket extends SessionPacket {

	private byte[] bytes;

	public ClientInGamePacket(ServerSession session,
			AccountCharacter character, AutoTileController autoTileController) {
		super(session);
		OutputByteBuffer out = new OutputByteBuffer();
		out.putAccountCharacter(character);
		out.putAutoTiles(autoTileController.getAutoTiles());
		this.bytes = out.getBytes();
	}

	@Override
	public ServerPacketType getPacketType() {
		return ServerPacketType.CLIENT_IN_GAME;
	}

	@Override
	public byte[] getBytes() {
		return bytes;
	}

}
