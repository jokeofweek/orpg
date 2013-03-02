package orpg.client.net.handlers;

import com.badlogic.gdx.Gdx;

import orpg.client.BaseClient;
import orpg.client.data.ClientReceivedPacket;
import orpg.client.net.packets.NeedSegmentPacket;
import orpg.client.state.GameState;
import orpg.shared.data.Map;
import orpg.shared.net.InputByteBuffer;

public class NewMapHandler implements ClientPacketHandler {

	@Override
	public void handle(ClientReceivedPacket packet, final BaseClient client) {
		client.getAccountCharacter().setChangingMap(true);

		InputByteBuffer in = packet.getByteBuffer();

		Map map = in.getMapDescriptor();
		client.setMap(map);
		client.getAccountCharacter().setMap(map);

		// Determine the segment
		client.getAccountCharacter().setX(in.getInt());
		int segmentX = map.getSegmentX(client.getAccountCharacter().getX());

		client.getAccountCharacter().setY(in.getInt());
		int segmentY = map.getSegmentY(client.getAccountCharacter().getY());

		// Clear the client data cache
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				client.clearClientPlayerData();
			}
		});

		// Send the need segment packet
		client.sendPacket(new NeedSegmentPacket(segmentX, segmentY));

	}

}
