package orpg.editor;

import java.util.List;

import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Frame;
import org.apache.pivot.wtk.MenuBar;
import org.apache.pivot.wtk.Window.ActionMapping;

import orpg.shared.Strings;

public abstract class BasicWindow {

	private WindowManager windowManager;

	public BasicWindow(WindowManager windowManager) {
		this.windowManager = windowManager;
	}

	public abstract void enter(Frame applicationFrame);

	public abstract Component getContent();

	public abstract void exit(Frame applicationFrame);

	public String getTitle() {
		// By default return name of the application
		return Strings.ENGINE_NAME;
	}

	/**
	 * <b>Condition:</b> The same instances of action mappings should be
	 * returned upon every call.
	 * 
	 * @return a list of all action mappings to be added (and removed) when
	 *         entering and exiting this window, or null if none.
	 */
	public List<ActionMapping> getActionMappings() {
		// By default return null
		return null;
	}

	public MenuBar getMenuBar() {
		// By default return null
		return null;
	}

	public WindowManager getWindowManager() {
		return windowManager;
	}
}
