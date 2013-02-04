package orpg.editor;

import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Frame;
import org.apache.pivot.wtk.MenuBar;

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

	public MenuBar getMenuBar() {
		// By default return null
		return null;
	}

	public WindowManager getWindowManager() {
		return windowManager;
	}
}
