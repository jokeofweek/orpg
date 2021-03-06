package orpg.client.net.handlers;

import java.util.List;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.utils.Bag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import orpg.client.BaseClient;
import orpg.client.ClientConstants;
import orpg.client.Paths;
import orpg.client.data.ClientReceivedPacket;
import orpg.client.data.component.Camera;
import orpg.client.state.GameState;
import orpg.shared.Constants;
import orpg.shared.data.AccountCharacter;
import orpg.shared.data.Map;
import orpg.shared.data.Segment;
import orpg.shared.data.component.IsPlayer;
import orpg.shared.data.component.Named;
import orpg.shared.data.component.Position;
import orpg.shared.data.store.DataStoreException;

public class SegmentDataHandler implements ClientPacketHandler {

	@Override
	public void handle(final ClientReceivedPacket packet,
			final BaseClient baseClient) {
		packet.getByteBuffer().decompress();

		int mapId = packet.getByteBuffer().getInt();

		// If not the same map, drop the packet
		if (mapId != baseClient.getMap().getId()) {
			return;
		}

		// Load the map
		boolean usingLocal = packet.getByteBuffer().getBoolean();
		short tmpSegmentX = -1;
		short tmpSegmentY = -1;
		if (usingLocal) {
			tmpSegmentX = packet.getByteBuffer().getShort();
			tmpSegmentY = packet.getByteBuffer().getShort();
		}

		System.out.println("Using local? " + usingLocal);

		final Segment segment = (usingLocal ? baseClient.getLocalMap()
				.getSegment(tmpSegmentX, tmpSegmentY) : packet.getByteBuffer()
				.getSegment());

		final short segmentX = usingLocal ? tmpSegmentX : segment.getX();
		final short segmentY = usingLocal ? tmpSegmentY : segment.getY();

		// No more data should be withdrawn from the byte buffer at this time!!
		baseClient.getMap().updateSegment(segment, false);
		baseClient.getAutoTileController().updateAutoTileCache(
				baseClient.getMap(), segment, true);
		baseClient.getSegmentRequestManager().receivedResponse(mapId, segment);

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

					// Load sprite textures
					Texture[] spritesets = new Texture[Constants.SPRITESETS];
					for (int i = 0; i < Constants.SPRITESETS; i++) {
						spritesets[i] = new Texture(Paths
								.asset("sprites/sprite_" + i + ".png"));
					}

					baseClient.getStateManager().switchState(
							new GameState(baseClient, tilesets,
									loadingTileTexture, spritesets));
				}
			});
		}

		// If we were changing maps, then synchronize the instances and request
		// segments
		final boolean wasChangingMaps = baseClient.isChangingMap();

		if (wasChangingMaps) {
			// baseClient.getMap().syncPlayer(
			// baseClient.getAccountCharacter());
		}

		final Map map = baseClient.getMap();

		// Add all players to client player data cache
		Gdx.app.postRunnable(new Runnable() {

			@Override
			public void run() {
				Bag<Entity> entities = packet.getByteBuffer().getEntities(
						baseClient.getWorld());

				// See if the entity is the current player's
				ComponentMapper<IsPlayer> isPlayerMapper = baseClient
						.getWorld().getMapper(IsPlayer.class);
				ComponentMapper<Named> namedMapper = baseClient.getWorld()
						.getMapper(Named.class);
				ComponentMapper<Position> positionMapper = baseClient
						.getWorld().getMapper(Position.class);
				String name = baseClient.getAccountCharacter().getName();
				Entity entity;

				for (int i = 0; i < entities.size(); i++) {
					entity = entities.get(i);

					// Can assume named if is player
					if (baseClient.getEntity() == null
							&& isPlayerMapper.getSafe(entity) != null
							&& namedMapper.get(entity).getName().equals(name)) {
						baseClient.setEntity(entity);

						// If we were presently changing maps, we are done now
						baseClient.setChangingMap(false);

						// If it is the player, add the camera if it is not
						// already present
						Position position = positionMapper.get(entity);
						entity.addComponent(new Camera(
								ClientConstants.GAME_WIDTH,
								ClientConstants.GAME_HEIGHT, baseClient
										.getMap().getWidth()
										* Constants.TILE_WIDTH, baseClient
										.getMap().getHeight()
										* Constants.TILE_HEIGHT, position
										.getX() * Constants.TILE_WIDTH,
								position.getY() * Constants.TILE_HEIGHT));

					}

					baseClient.getWorld().addEntity(entity);
				}

				// If we are joining a map, request surrounding segments
				if (wasChangingMaps) {
					baseClient.getSegmentRequestManager()
							.requestSurroundingSegmentsOfSegment(segmentX,
									segmentY);
				}

			}
		});

		// Save the segment
		try {
			baseClient.getDataStore().saveSegment(map.getId(), segment);
		} catch (DataStoreException e) {
		}
	}
}
