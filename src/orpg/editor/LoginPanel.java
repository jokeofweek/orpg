package orpg.editor;

import java.io.IOException;

import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.FillPane;
import org.apache.pivot.wtk.Frame;
import org.apache.pivot.wtk.PushButton;

import org.apache.pivot.wtk.Window;

import orpg.editor.controller.MapController;
import orpg.editor.controller.MapEditorController;
import orpg.editor.ui.MapEditorMenuBar;
import orpg.shared.data.Map;

public class LoginPanel extends BasicWindow {

	public LoginPanel(WindowManager windowManager) {
		super(windowManager);
	}

	@Override
	public void enter(Frame applicationFrame) {
	}

	@Override
	public Component getContent() {
		FillPane content = new FillPane();

		PushButton button = new PushButton("Login");
		button.getButtonPressListeners().add(new ButtonPressListener() {

			@Override
			public void buttonPressed(Button button) {
				getWindowManager().switchWindow(
						new MapEditorPanel(getWindowManager()));
			}
		});
		content.add(button);

		return content;
	}

	@Override
	public void exit(Frame applicationFrame) {
	}

}
