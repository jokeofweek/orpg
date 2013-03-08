package orpg.server.net.handlers;

import orpg.server.BaseServer;
import orpg.server.data.ServerReceivedPacket;
import orpg.server.event.PlayerRequestMovementEvent;
import orpg.server.systems.MovementSystem;
import orpg.shared.data.Direction;

public class ClientMoveRequestHandler implements ServerPacketHandler {

	@Override
	public void handle(ServerReceivedPacket packet, BaseServer baseServer) {
		byte direction = packet.getByteBuffer().getByte();
		if (direction < 0 || direction >= Direction.values().length) {
			packet.getSession().preventativeDisconnect(
					"Invalid move direction.");
		}

		baseServer
				.getWorld()
				.getSystem(MovementSystem.class)
				.addEvent(
						new PlayerRequestMovementEvent(packet.getSession(), Direction
								.values()[direction]));

	}

}
