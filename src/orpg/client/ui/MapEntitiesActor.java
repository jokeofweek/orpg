package orpg.client.ui;

import orpg.client.BaseClient;
import orpg.client.data.ClientPlayerData;
import orpg.shared.ClientConstants;
import orpg.shared.Constants;
import orpg.shared.data.AccountCharacter;
import orpg.shared.data.Map;
import orpg.shared.data.Segment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MapEntitiesActor extends Actor {

	private BaseClient baseClient;
	private ViewBox viewBox;
	private Texture[] spritesets;

	public MapEntitiesActor(BaseClient baseClient, ViewBox viewBox,
			Texture[] spritesets) {
		this.baseClient = baseClient;
		this.viewBox = viewBox;
		this.spritesets = spritesets;
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

		if (baseClient.getAccountCharacter().isChangingMap()) {
			return;
		}

		int sprite;
		int spriteSet;
		int spriteY;
		int spriteX;
		int frame;

		for (int x = startSegmentX; x <= endSegmentX; x++) {
			for (int y = startSegmentY; y <= endSegmentY; y++) {
				segment = map.getSegment(x, y);
				if (segment != null) {
					for (AccountCharacter character : segment.getPlayers()
							.values()) {
						playerData = baseClient.getClientPlayerData(character
								.getName());

						// Render the character's texture
						sprite = playerData.getCharacter().getSprite();
						spriteSet = sprite / Constants.SPRITES_PER_SPRITESET;
						spriteX = sprite % Constants.SPRITESET_WIDTH;
						spriteY = (sprite % Constants.SPRITES_PER_SPRITESET)
								/ Constants.SPRITESET_WIDTH;

						// Calculate the frame offset
						frame = 0;
						if (playerData.isMoving()) {
							switch (playerData.getMoveDirection()) {
							case UP:
								frame = (playerData.getYOffset() > 16) ? 1 : 3;
								break;
							case DOWN:
								frame = (playerData.getYOffset() > -16) ? 3 : 1;
								break;
							case LEFT:
								frame = (playerData.getXOffset() > 16) ? 1 : 3;
								break;
							case RIGHT:
								frame = (playerData.getXOffset() > -16) ? 3 : 1;
								break;
							}
						}

						batch.draw(
								spritesets[spriteSet],
								((character.getX() - startX) * Constants.TILE_WIDTH)
										- dX + playerData.getXOffset(),
								((character.getY() - startY) * Constants.TILE_WIDTH)
										- dY + playerData.getYOffset(),
								Constants.SPRITE_FRAME_WIDTH,
								Constants.SPRITE_FRAME_HEIGHT,
								(spriteX * Constants.SPRITE_WIDTH)
										+ (frame * Constants.SPRITE_FRAME_WIDTH),
								(spriteY * Constants.SPRITE_HEIGHT)
										+ (playerData.getCharacter()
												.getDirection().ordinal() * Constants.SPRITE_FRAME_HEIGHT),
								Constants.SPRITE_FRAME_WIDTH,
								Constants.SPRITE_FRAME_HEIGHT, false, true);
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

		// If the player is moving, scroll the viewbox as well
		playerData = baseClient.getClientPlayerData(baseClient
				.getAccountCharacter().getName());
		if (playerData.isMoving()) {
			switch (playerData.getMoveDirection()) {
			case UP:
				viewBox.scroll(0,
						(int) -(delta * 2 * ClientConstants.WALK_SPEED));
				break;
			case DOWN:
				viewBox.scroll(0,
						(int) (delta * 2 * ClientConstants.WALK_SPEED));
				break;
			case LEFT:
				viewBox.scroll((int) -(delta * 2 * ClientConstants.WALK_SPEED),
						0);
				break;
			case RIGHT:
				viewBox.scroll((int) (delta * 2 * ClientConstants.WALK_SPEED),
						0);
				break;
			}
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
								playerData
										.setYOffset((int) (playerData
												.getYOffset() - (delta * ClientConstants.WALK_SPEED)));
								if (playerData.getYOffset() < 0) {
									playerData.setYOffset(0);
								}
								break;
							case DOWN:
								playerData
										.setYOffset((int) (playerData
												.getYOffset() + (delta * ClientConstants.WALK_SPEED)));
								if (playerData.getYOffset() > 0) {
									playerData.setYOffset(0);
								}
								break;
							case LEFT:
								playerData
										.setXOffset((int) (playerData
												.getXOffset() - (delta * ClientConstants.WALK_SPEED)));
								if (playerData.getXOffset() < 0) {
									playerData.setXOffset(0);
								}
								break;
							case RIGHT:
								playerData
										.setXOffset((int) (playerData
												.getXOffset() + (delta * ClientConstants.WALK_SPEED)));
								if (playerData.getXOffset() > 0) {
									playerData.setXOffset(0);
								}
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
