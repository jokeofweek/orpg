package orpg.editor;

import org.apache.pivot.collections.Map;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.DesktopApplicationContext;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.Frame;
import org.apache.pivot.wtk.Theme;
import org.apache.pivot.wtk.Window;
import org.apache.pivot.wtk.WindowActionMappingListener;
import org.apache.pivot.wtk.Keyboard.KeyStroke;
import org.apache.pivot.wtk.Window.ActionMapping;

import orpg.editor.ui.MapEditorMenuBar;
import orpg.editor.ui.MapView;
import orpg.editor.ui.MapViewSkin;
import orpg.shared.Strings;

public class EditorApplication implements Application {

	private Frame applicationFrame = null;

	@Override
	public void resume() throws Exception {
	}

	@Override
	public boolean shutdown(boolean arg0) throws Exception {
		return false;
	}

	@Override
	public void startup(Display display, Map<String, String> properties)
			throws Exception {

		// Setup custom controls
		Theme.getTheme().set(MapView.class, MapViewSkin.class);

		// Setup the application frame
		applicationFrame = new Frame();
		applicationFrame.setMaximized(true);
		applicationFrame.getStyles().put("showWindowControls", false);
		applicationFrame.open(display);

		// Setup the window manager
		WindowManager windowManager = new WindowManager(applicationFrame);
		windowManager.switchWindow(new LoginWindow(windowManager));
	}

	@Override
	public void suspend() throws Exception {
	}

	public static void main(String[] args) {
		DesktopApplicationContext.main(EditorApplication.class, args);
	}

}
