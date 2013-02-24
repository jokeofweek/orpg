package orpg.client.net.packets;

import orpg.shared.net.ClientPacketType;

public class LoadMapPacket extends ClientPacket {

	@Override
	public ClientPacketType getPacketType() {
		return ClientPacketType.CLIENT_LOAD_MAP;
	}

	@Override
	public byte[] getBytes() {
		return new byte[0];
	}

}
