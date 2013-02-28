package orpg.server.net.handlers;

import java.util.logging.Level;

import orpg.server.BaseServer;
import orpg.server.data.ServerReceivedPacket;
import orpg.shared.data.Direction;

public class ClientMoveRequestHandler implements ServerPacketHandler {

	@Override
	public void handle(ServerReceivedPacket packet, BaseServer baseServer) {
		byte direction = packet.getByteBuffer().getByte();
		if (direction < 0 || direction >= Direction.values().length) {
			packet.getSession().preventativeDisconnect(
					"Invalid move direction.");
		}

		// Make sure we can move
		if (packet.getSession().getCharacter()
				.canMove(Direction.values()[direction])) {
			baseServer.getAccountController().move(
					packet.getSession().getCharacter(),
					Direction.values()[direction]);
		} else {
			packet.getSession()
					.preventativeDisconnect("Position modification.");
		}
	}

}
