package orpg.editor;

import org.apache.pivot.wtk.Frame;

public class WindowManager {

	private Frame applicationFrame;
	private BasicWindow currentWindow;

	public WindowManager(Frame applicationFrame) {
		this.applicationFrame = applicationFrame;
	}

	public void switchWindow(BasicWindow newWindow) {
		if (currentWindow != null) {
			this.currentWindow.exit(this.applicationFrame);
			applicationFrame.setMenuBar(null);
		}

		this.currentWindow = newWindow;
		
		newWindow.enter(applicationFrame);
		applicationFrame.getWindow().setTitle(newWindow.getTitle());
		applicationFrame.setMenuBar(newWindow.getMenuBar());
		applicationFrame.setContent(newWindow.getContent());
	}

}
