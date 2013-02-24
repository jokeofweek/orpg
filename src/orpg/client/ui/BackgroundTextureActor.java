package orpg.client.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BackgroundTextureActor extends Actor {

	private TextureRegion region;
	
	public BackgroundTextureActor(Texture texture, int width, int height) {
		this.region = new TextureRegion(texture, width, height);
		this.region.flip(false, true);
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.draw(region, 0, 0);
	}

}
