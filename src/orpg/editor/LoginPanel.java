package orpg.editor;

import java.io.IOException;

import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.FillPane;
import org.apache.pivot.wtk.PushButton;

import org.apache.pivot.wtk.Window;

public class LoginPanel extends FillPane {

	public LoginPanel(final Window window) {

		PushButton button = new PushButton("Login");
		button.getButtonPressListeners().add(new ButtonPressListener() {

			@Override
			public void buttonPressed(Button button) {
				window.setContent(new MapEditorPanel());
			}
		});
		add(button);

	}

}
