package orpg.client.screen;

import orpg.client.Paths;
import orpg.client.net.BaseClient;
import orpg.client.net.packets.LoginPacket;
import orpg.client.ui.BackgroundTextureActor;
import orpg.shared.Constants;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenuScreen implements Screen {

	private Stage stage;
	private Game game;
	private Texture backgroundTexture;
	private BaseClient baseClient;

	public MainMenuScreen(Game game, final BaseClient baseClient) {
		this.stage = new Stage();
		this.game = game;
		this.backgroundTexture = new Texture(
				Paths.asset("menu_background.png"));
		this.baseClient = baseClient;

		// Set the background
		this.stage.addActor(new BackgroundTextureActor(backgroundTexture,
				800, 600));

		// Build the root table
		Skin skin = new Skin(Paths.asset("uiskin.json"));
		Table root = new Table(skin);
		root.setFillParent(true);
		this.stage.addActor(root);

		// Add components to root.
		final TextField nameText = new TextField("", skin);
		TextField addressText = new TextField("", skin);
		final TextField passwordText = new TextField("", skin);
		passwordText.setPasswordCharacter('*');
		passwordText.setPasswordMode(true);

		root.add(new Label("Name:", skin)).left().pad(10);
		root.add(nameText).width(150);
		root.row();
		root.add(new Label("Email:", skin)).left().pad(10);
		root.add(addressText).width(150);
		root.row();
		root.add(new Label("Password:", skin)).left().pad(10);
		root.add(passwordText).width(150);
		root.row();

		Button loginButton = new TextButton("Login", skin);
		root.add(loginButton).colspan(2).width(100);
		loginButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				baseClient.sendPacket(new LoginPacket(nameText.getText(),
						passwordText.getText().toCharArray(), false));
			}
		});

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
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
	}

}
