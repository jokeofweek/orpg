package orpg.client.state;

import orpg.client.BaseClient;
import orpg.client.Paths;
import orpg.client.ui.BackgroundTextureActor;
import orpg.client.ui.MapLayerActor;
import orpg.client.ui.ViewBox;
import orpg.shared.Constants;
import orpg.shared.data.MapLayer;
import orpg.shared.net.AbstractClient;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class GameState extends ClientState {

	private BaseClient baseClient;
	private Stage stage;

	private OrthographicCamera camera;
	private ViewBox viewbox;
	private Texture backgroundTexture;
	private Texture[] tilesets;
	private Texture loadingTileTexture;

	public GameState(BaseClient baseClient, Texture[] tilesets,
			Texture loadingTileTexture) {
		this.baseClient = baseClient;

		// Setup the stage
		this.stage = new Stage();

		this.tilesets = tilesets;
		this.loadingTileTexture = loadingTileTexture;

		this.viewbox = new ViewBox(800, 478, baseClient.getMap().getWidth()
				* Constants.TILE_WIDTH, baseClient.getMap().getHeight()
				* Constants.TILE_HEIGHT);

		Actor bottomLayersActor = new MapLayerActor(baseClient, viewbox,
				tilesets, loadingTileTexture, new int[] {
						MapLayer.GROUND.ordinal(), MapLayer.MASK.ordinal(),
						MapLayer.MASK_2.ordinal() }, 0, 800, 0, 478);
		Actor topLayersActor = new MapLayerActor(baseClient, viewbox, tilesets,
				loadingTileTexture, new int[] { MapLayer.FRINGE.ordinal() }, 0,
				800, 0, 478);
		
		this.stage.addActor(bottomLayersActor);
		this.stage.addActor(topLayersActor);

		this.backgroundTexture = new Texture(Paths.asset("game_background.png"));
		this.stage.addActor(new BackgroundTextureActor(backgroundTexture, 800,
				600, true));

		// Setup the camera
		this.camera = new OrthographicCamera(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		camera.setToOrtho(true, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		this.stage.setCamera(camera);
	}

	@Override
	public void enter() {
		// TODO Auto-generated method stub

	}

	@Override
	public void exit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void displayError(String errorMessage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		handleInput();
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// Set up the input handler
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		stage.dispose();
		backgroundTexture.dispose();
		for (int i = 0; i < Constants.TILESETS; i++) {
			tilesets[i].dispose();
		}
		loadingTileTexture.dispose();
	}

	private void handleInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			viewbox.scroll(-4, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			viewbox.scroll(4, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			viewbox.scroll(0, 4);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			viewbox.scroll(0, -4);
		}

	}

}
