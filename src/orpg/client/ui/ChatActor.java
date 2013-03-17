package orpg.client.ui;

import orpg.client.BaseClient;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ChatActor extends Actor {

	private Skin skin;
	private BaseClient baseClient;
	private BitmapFont bitmapFont;
	private Matrix4 matrix;
	private int x;
	private int y;

	public ChatActor(BaseClient baseClient, Skin skin, int x, int y) {
		this.baseClient = baseClient;
		this.skin = skin;
		this.x = x;
		this.y = y;
		this.matrix = new Matrix4();

		bitmapFont = skin.getFont("default-font");
		bitmapFont.setScale(1, -1);
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		//super.draw(batch, parentAlpha);
		//float xOffset = x;
		//bitmapFont.setColor(Color.YELLOW);
		//xOffset += bitmapFont.draw(batch, "Dominic: ", xOffset, y).width;
		//bitmapFont.setColor(Color.WHITE);
		//xOffset += bitmapFont.draw(batch, " Hello, world! ", xOffset, y).width;

	}

}
