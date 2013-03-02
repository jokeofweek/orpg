package orpg.client.state;

import javax.swing.JOptionPane;

import orpg.client.BaseClient;
import orpg.client.Paths;
import orpg.client.net.packets.CreateAccountPacket;
import orpg.client.net.packets.LoginPacket;
import orpg.client.ui.BackgroundTextureActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenuState extends ClientState {

	private Stage stage;
	private Texture backgroundTexture;
	private BaseClient baseClient;

	public MainMenuState(final BaseClient baseClient) {
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

		// Add components to root.
		final TextField nameText = new TextField("", skin);
		final TextField addressText = new TextField("", skin);
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
		root.row();

		Button registerButton = new TextButton("Register", skin);
		root.add(registerButton).colspan(2).width(100);
		registerButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				baseClient.sendPacket(new CreateAccountPacket(nameText
						.getText(), addressText.getText(), passwordText
						.getText().toCharArray()));
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

	@Override
	public void enter() {

	}

	@Override
	public void exit() {

	}

	@Override
	public void displayError(String errorMessage) {
		JOptionPane.showMessageDialog(null, errorMessage);
	}

}
