package orpg.client.ui;

import orpg.client.BaseClient;
import orpg.client.data.ClientPlayerData;
import orpg.shared.ClientConstants;
import orpg.shared.Constants;
import orpg.shared.data.AccountCharacter;
import orpg.shared.data.Map;
import orpg.shared.data.Segment;
import sun.util.calendar.BaseCalendar;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MapEntitiesActor extends Actor {

	private BaseClient baseClient;
	private ViewBox viewBox;
	private Texture texture;

	public MapEntitiesActor(BaseClient baseClient, ViewBox viewBox,
			Texture texture) {
		this.baseClient = baseClient;
		this.viewBox = viewBox;
		this.texture = texture;
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, 0);

		// Iterate through all possible segments, rendering all possible
		// entities
		Map map = baseClient.getMap();
		int startX = viewBox.getStartX();
		int startY = viewBox.getStartY();
		int startSegmentX = map.getSegmentX(startX);
		int endSegmentX = map.getSegmentX(viewBox.getEndX());
		int startSegmentY = map.getSegmentY(startY);
		int endSegmentY = map.getSegmentY(viewBox.getEndY());
		int dX = viewBox.getOffsetX() % Constants.TILE_WIDTH;
		int dY = viewBox.getOffsetY() % Constants.TILE_HEIGHT;

		Segment segment;
		ClientPlayerData playerData;

		for (int x = startSegmentX; x <= endSegmentX; x++) {
			for (int y = startSegmentY; y <= endSegmentY; y++) {
				segment = map.getSegment(x, y);
				if (segment != null) {
					for (AccountCharacter character : segment.getPlayers()
							.values()) {
						playerData = baseClient.getClientPlayerData(character
								.getName());
						batch.draw(
								texture,
								((character.getX() - startX) * Constants.TILE_WIDTH)
										- dX + playerData.getXOffset(),
								((character.getY() - startY) * Constants.TILE_WIDTH)
										- dY + playerData.getYOffset(), 32, 32,
								96, 96, 32, 32, true, false);
					}
				}
			}
		}

	}

	@Override
	public void act(float delta) {
		super.act(delta);

		Map map = baseClient.getMap();
		int startX = viewBox.getStartX();
		int startY = viewBox.getStartY();
		int startSegmentX = map.getSegmentX(startX);
		int endSegmentX = map.getSegmentX(viewBox.getEndX());
		int startSegmentY = map.getSegmentY(startY);
		int endSegmentY = map.getSegmentY(viewBox.getEndY());

		Segment segment;
		ClientPlayerData playerData;

		if (baseClient.getAccountCharacter().isChangingMap()) {
			return;
		}
		
		// Move each entity that is moving
		for (int x = startSegmentX; x <= endSegmentX; x++) {
			for (int y = startSegmentY; y <= endSegmentY; y++) {
				segment = map.getSegment(x, y);
				if (segment != null) {
					for (AccountCharacter character : segment.getPlayers()
							.values()) {
						playerData = baseClient.getClientPlayerData(character
								.getName());

						if (playerData.isMoving()) {
							// Update offset based on walk speed and direction
							switch (playerData.getMoveDirection()) {
							case UP:
								playerData.setYOffset((int) (playerData.getYOffset() - (delta * ClientConstants.WALK_SPEED)));
								break;
							case DOWN:
								playerData.setYOffset((int) (playerData.getYOffset() + (delta * ClientConstants.WALK_SPEED)));
								break;
							case LEFT:
								playerData.setXOffset((int) (playerData.getXOffset() - (delta * ClientConstants.WALK_SPEED)));
								break;
							case RIGHT:
								playerData.setXOffset((int) (playerData.getXOffset() + (delta * ClientConstants.WALK_SPEED)));
								break;
							}
							if (playerData.getXOffset() == 0
									&& playerData.getYOffset() == 0) {
								playerData.setMoving(false);
							}
						}
					}

				}
			}
		}

	}
}
