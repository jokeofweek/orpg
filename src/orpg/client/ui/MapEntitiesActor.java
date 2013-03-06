package orpg.client.ui;

import orpg.client.BaseClient;
import orpg.client.ClientConstants;
import orpg.client.data.ClientPlayerData;
import orpg.client.systems.RenderSystem;
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
	private Texture[] spriteSets;
	
	public MapEntitiesActor(BaseClient baseClient, ViewBox viewBox,
			Texture[] spriteSets) {
		this.baseClient = baseClient;
		this.viewBox = viewBox;
		this.spriteSets = spriteSets;
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, 0);

		if (baseClient.isChangingMap()) {
			return;
		}

		RenderSystem system = baseClient.getWorld().getSystem(RenderSystem.class);
		system.prepare(viewBox, spriteSets, batch);
		system.process();
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		if (baseClient.isChangingMap()) {
			return;
		}
		/*
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
		for (short x = startSegmentX; x <= endSegmentX; x++) {
			for (short y = startSegmentY; y <= endSegmentY; y++) {
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
		}*/

	}
}
