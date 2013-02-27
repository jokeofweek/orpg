package orpg.server.net.packets;

import orpg.server.ServerSession;
import orpg.server.data.Account;
import orpg.shared.data.AccountCharacter;
import orpg.shared.net.OutputByteBuffer;
import orpg.shared.net.ServerPacketType;

public class LoginOkPacket extends SessionPacket {

	private byte[] bytes;

	public LoginOkPacket(ServerSession session, Account account) {
		super(session);
		// Add all character names to the packet
		OutputByteBuffer out = new OutputByteBuffer();
		out.putByte((byte) account.getCharacters().size());
		for (AccountCharacter character : account.getCharacters()) {
			out.putString(character.getName());
		}

		this.bytes = out.getBytes();

	}

	@Override
	public ServerPacketType getPacketType() {
		return ServerPacketType.LOGIN_OK;
	}

	@Override
	public byte[] getBytes() {
		return this.bytes;
	}

}
