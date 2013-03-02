package orpg.client.state;

import java.util.List;

import javax.swing.JOptionPane;

import orpg.client.BaseClient;
import orpg.client.Paths;
import orpg.client.net.packets.UseCharacterPacket;
import orpg.client.ui.BackgroundTextureActor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class CharacterSelectState extends ClientState {

	private List<String> characters;
	private Stage stage;
	private Texture backgroundTexture;
	private Object baseClient;
	private OrthographicCamera camera;

	public CharacterSelectState(final BaseClient baseClient,
			List<String> characters) {
		this.characters = characters;

		this.stage = new Stage();
		this.backgroundTexture = new Texture(Paths.asset("menu_background.png"));
		this.baseClient = baseClient;

		// Set the background
		this.stage.addActor(new BackgroundTextureActor(backgroundTexture, 800,
				600, false));

		// Build the root table
		Skin skin = new Skin(Paths.asset("uiskin.json"));
		Table root = new Table(skin);
		root.setFillParent(true);
		this.stage.addActor(root);

		Button characterButton;

		for (final String character : characters) {
			characterButton = new TextButton(character, skin);
			characterButton.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					baseClient.sendPacket(new UseCharacterPacket(character));
				}
			});
			root.add(characterButton).colspan(2).width(100);
			root.row();
		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
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
		JOptionPane.showMessageDialog(null, errorMessage);
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
	}

}
