package orpg.client.net.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import orpg.client.BaseClient;
import orpg.client.Paths;
import orpg.client.data.ClientReceivedPacket;
import orpg.client.state.GameState;
import orpg.shared.Constants;
import orpg.shared.data.Map;
import orpg.shared.data.Segment;
import orpg.shared.net.AbstractClient;

public class MapDataHandler implements ClientPacketHandler {

	@Override
	public void handle(ClientReceivedPacket packet, BaseClient client) {
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
					// Load the textures, and then pass them
					Texture[] tilesets = new Texture[Constants.TILESETS];
					for (int i = 0; i < Constants.TILESETS; i++) {
						tilesets[i] = new Texture(Paths.asset("tiles_" + i
								+ ".png"));
					}
					Texture loadingTileTexture = new Texture(Paths
							.asset("loading_tile.png"));

					baseClient.getStateManager().switchState(
							new GameState(baseClient, tilesets,
									loadingTileTexture));
				}
			});
		}
	}

}
