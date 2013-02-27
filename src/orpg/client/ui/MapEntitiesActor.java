package orpg.client.ui;

import orpg.client.BaseClient;
import sun.util.calendar.BaseCalendar;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MapEntitiesActor extends Actor {

	private BaseClient baseClient;
	private ViewBox viewBox;

	public MapEntitiesActor(BaseClient baseClient, ViewBox viewBox) {
		this.baseClient = baseClient;
		this.viewBox = viewBox;
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, 0);
		System.out
				.println(viewBox.getStartX() + "," + viewBox.getStartY());
	}
}
