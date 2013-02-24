package orpg.client.net.handlers;

import com.badlogic.gdx.Gdx;

import orpg.client.BaseClient;
import orpg.client.data.ClientReceivedPacket;
import orpg.client.state.GameState;
import orpg.shared.data.Map;
import orpg.shared.data.Segment;
import orpg.shared.net.AbstractClient;

public class MapDataHandler implements ClientPacketHandler {

	@Override
	public void handle(ClientReceivedPacket packet, AbstractClient client) {
		final BaseClient baseClient = ((BaseClient) client);

		packet.getByteBuffer().decompress();

		// Load the map
		Map map = packet.getByteBuffer().getMapDescriptor();
		map.updateSegment(packet.getByteBuffer().getSegment());

		baseClient.setMap(map);

		// Switch to game state if we aren't already in game state
		if (!(baseClient.getStateManager().getCurrentState() instanceof GameState)) {
			Gdx.app.postRunnable(new Runnable() {
				
				@Override
				public void run() {
					baseClient.getStateManager().switchState(new GameState(baseClient));
				}
			});
		}
	}

}
