package orpg.server.net.handlers;

import orpg.server.BaseServer;
import orpg.server.data.ServerReceivedPacket;
import orpg.server.net.packets.ErrorPacket;
import orpg.shared.ErrorMessage;
import orpg.shared.data.AccountCharacter;

public class UseCharacterHandler implements ServerPacketHandler {

	@Override
	public void handle(ServerReceivedPacket packet, BaseServer baseServer) {
		String characterName = packet.getByteBuffer().getString();

		// Make sure this character is associated with this account
		for (AccountCharacter character : packet.getSession().getAccount()
				.getCharacters()) {
			if (character.getName().equals(characterName)) {
				packet.getSession().useCharacter(characterName);
				return;
			}
		}

		baseServer.sendPacket(new ErrorPacket(packet.getSession(),
				ErrorMessage.GENERIC_USE_CHARACTER_ERROR));
	}

}
