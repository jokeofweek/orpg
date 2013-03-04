package orpg.server.net.packets;

import orpg.server.ServerSession;
import orpg.shared.data.AccountCharacter;
import orpg.shared.net.ServerPacketType;
import orpg.shared.net.serialize.OutputByteBuffer;

public class ClientJoinMapPacket extends MapExceptForPacket {

	private byte[] bytes;

	/**
	 * This sends a packet to the character's map saying that the character has
	 * joined the map. Note that it uses the character's
	 * {@link AccountCharacter#getMap()} method to determine the map.
	 * 
	 * @param serverSession
	 *            the session of the character
	 * @param character
	 *            the character that has joined.
	 */
	public ClientJoinMapPacket(ServerSession serverSession,
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
		return ServerPacketType.CLIENT_JOIN_MAP;
	}

	@Override
	public byte[] getBytes() {
		return this.bytes;
	}

}
