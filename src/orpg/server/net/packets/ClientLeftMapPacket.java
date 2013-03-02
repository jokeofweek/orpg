package orpg.server.net.packets;

import orpg.server.ServerSession;
import orpg.shared.data.AccountCharacter;
import orpg.shared.net.OutputByteBuffer;
import orpg.shared.net.ServerPacketType;

public class ClientLeftMapPacket extends MapExceptForPacket {

	private byte[] bytes;

	/**
	 * This sends a packet to the character's map saying that the character has
	 * left the map. Note that it uses the character's
	 * {@link AccountCharacter#getMap()} method to determine the map.
	 * 
	 * @param serverSession
	 *            the session of the character
	 * @param character
	 *            the character that has left.
	 */
	public ClientLeftMapPacket(ServerSession serverSession,
			AccountCharacter character) {
		super(serverSession, character.getMap());

		OutputByteBuffer out = new OutputByteBuffer();
		out.putInt(character.getMap().getId()); // put this for sanity check on
												// client side
		out.putMapCharacter(character);
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
