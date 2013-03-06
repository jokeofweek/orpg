package orpg.client.state;

import orpg.client.BaseClient;
import orpg.client.Paths;
import orpg.client.data.ClientPlayerData;
import orpg.client.net.packets.MoveRequestPacket;
import orpg.client.ui.BackgroundTextureActor;
import orpg.client.ui.MapEntitiesActor;
import orpg.client.ui.MapLayerActor;
import orpg.client.ui.ViewBox;
import orpg.shared.Constants;
import orpg.shared.data.AccountCharacter;
import orpg.shared.data.Direction;
import orpg.shared.data.MapLayer;
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
	private Texture[] spritesets;

	public GameState(BaseClient baseClient, Texture[] tilesets,
			Texture loadingTileTexture, Texture[] spritesets) {
		this.baseClient = baseClient;

		// Setup the stage
		this.stage = new Stage();

		this.tilesets = tilesets;
		this.loadingTileTexture = loadingTileTexture;
		this.spritesets = spritesets;

		this.viewbox = new ViewBox(800, 446, baseClient.getMap().getWidth()
				* Constants.TILE_WIDTH, baseClient.getMap().getHeight()
				* Constants.TILE_HEIGHT);

		Actor bottomLayersActor = new MapLayerActor(baseClient, viewbox,
				tilesets, loadingTileTexture, new int[] {
						MapLayer.GROUND.ordinal(), MapLayer.MASK.ordinal(),
						MapLayer.MASK_2.ordinal() }, 0, 800, 0, 446);
		Actor mapEntitiesActor = new MapEntitiesActor(baseClient, viewbox,
				spritesets);
		Actor topLayersActor = new MapLayerActor(baseClient, viewbox, tilesets,
				loadingTileTexture, new int[] { MapLayer.FRINGE.ordinal() }, 0,
				800, 0, 446);

		this.stage.addActor(bottomLayersActor);
		this.stage.addActor(mapEntitiesActor);
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
		//handleInput();
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

	private void handleInput() {
		AccountCharacter accountCharacter = baseClient.getAccountCharacter();
		ClientPlayerData playerData = null;// baseClient
				//.getClientPlayerData(accountCharacter.getName());

		// If player data isn't loaded, quit out
		if (playerData == null) {
			return;
		}

		// Handle movement input
		if (!playerData.isMoving()) {
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT)
					&& accountCharacter.canMove(Direction.LEFT)) {
				playerData.move(Direction.LEFT);
				baseClient.sendPacket(MoveRequestPacket.LEFT);
				baseClient.getSegmentRequestManager()
						.requestSurroundingSegments(
								baseClient.getAccountCharacter());
			} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)
					&& accountCharacter.canMove(Direction.RIGHT)) {
				playerData.move(Direction.RIGHT);
				baseClient.sendPacket(MoveRequestPacket.RIGHT);
				baseClient.getSegmentRequestManager()
						.requestSurroundingSegments(
								baseClient.getAccountCharacter());
			} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)
					&& accountCharacter.canMove(Direction.DOWN)) {
				playerData.move(Direction.DOWN);
				baseClient.sendPacket(MoveRequestPacket.DOWN);
				baseClient.getSegmentRequestManager()
						.requestSurroundingSegments(
								baseClient.getAccountCharacter());
			} else if (Gdx.input.isKeyPressed(Input.Keys.UP)
					&& accountCharacter.canMove(Direction.UP)) {
				playerData.move(Direction.UP);
				baseClient.sendPacket(MoveRequestPacket.UP);
				baseClient.getSegmentRequestManager()
						.requestSurroundingSegments(
								baseClient.getAccountCharacter());
			}
		}
	}

	public void centerOnPlayer() {
		viewbox.centerAtTile(baseClient.getAccountCharacter().getX(),
				baseClient.getAccountCharacter().getY());
	}

}
