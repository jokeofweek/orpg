package orpg.editor;

import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.FillPane;
import org.apache.pivot.wtk.Frame;
import org.apache.pivot.wtk.PushButton;

public class LoginWindow extends BasicWindow {

	public LoginWindow(WindowManager windowManager) {
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
						new MapEditorWindow(getWindowManager()));
			}
		});
		content.add(button);

		return content;
	}

	@Override
	public void exit(Frame applicationFrame) {
	}

}
