package orpg.mapeditor;

import org.apache.pivot.collections.Map;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.DesktopApplicationContext;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.Window;
import orpg.shared.Strings;

public class EditorApplication implements Application {

	private Window window = null;

	@Override
	public void resume() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean shutdown(boolean arg0) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void startup(Display display, Map<String, String> properties)
			throws Exception {
		window = new Window();
		window.setContent(new LoginPanel(window));
		window.setTitle(String.format("%s - %s", Strings.ENGINE_NAME,
				Strings.MAP_EDITOR_NAME));
		window.setMaximized(true);

		window.open(display);
		

	}

	@Override
	public void suspend() throws Exception {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		DesktopApplicationContext.main(EditorApplication.class, args);
	}

}
