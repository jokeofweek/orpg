package orpg.client.state;

import orpg.client.BaseClient;
import orpg.client.Paths;
import orpg.client.ui.BackgroundTextureActor;
import orpg.client.ui.MapEntitiesActor;
import orpg.client.ui.MapLayerActor;
import orpg.client.ui.ViewBox;
import orpg.shared.Constants;
import orpg.shared.data.MapLayer;

import com.badlogic.gdx.Gdx;
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
	private Texture[] spritesets;

	public GameState(BaseClient baseClient, Texture[] tilesets,
			Texture loadingTileTexture, Texture[] spritesets) {
		this.baseClient = baseClient;

		// Setup the stage
		this.stage = new Stage();

		this.tilesets = tilesets;
		this.loadingTileTexture = loadingTileTexture;
		this.spritesets = spritesets;

		Actor bottomLayersActor = new MapLayerActor(
				baseClient,
				tilesets,
				loadingTileTexture,
				new int[] { MapLayer.GROUND.ordinal(),
						MapLayer.MASK.ordinal(), MapLayer.MASK_2.ordinal() },
				0, 800, 0, 446);
		Actor mapEntitiesActor = new MapEntitiesActor(baseClient,
				spritesets);
		Actor topLayersActor = new MapLayerActor(baseClient, tilesets,
				loadingTileTexture,
				new int[] { MapLayer.FRINGE.ordinal() }, 0, 800, 0, 446);

		this.stage.addActor(bottomLayersActor);
		this.stage.addActor(mapEntitiesActor);
		this.stage.addActor(topLayersActor);

		this.backgroundTexture = new Texture(
				Paths.asset("game_background.png"));
		this.stage.addActor(new BackgroundTextureActor(backgroundTexture,
				800, 600, true));

		// Setup the camera
		this.camera = new OrthographicCamera(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		camera.setToOrtho(true, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		this.stage.setCamera(camera);
	}

	@Override
	public void enter() {
	}

	@Override
	public void exit() {
	}

	@Override
	public void displayError(String errorMessage) {
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		baseClient.getWorld().setDelta(delta);
		baseClient.getWorld().process();

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		// Set up the input handler
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		stage.dispose();
		backgroundTexture.dispose();
		for (int i = 0; i < tilesets.length; i++) {
			tilesets[i].dispose();
		}
		for (int i = 0; i < spritesets.length; i++) {
			spritesets[i].dispose();
		}
		loadingTileTexture.dispose();
	}

	public void centerOnPlayer() {
		viewbox.centerAtTile(baseClient.getAccountCharacter().getX(),
				baseClient.getAccountCharacter().getY());
	}

}
