package orpg.editor;

import java.util.List;

import org.apache.pivot.wtk.Frame;
import org.apache.pivot.wtk.Window.ActionMapping;

public class WindowManager {

	private Frame applicationFrame;
	private BasicWindow currentWindow;

	public WindowManager(Frame applicationFrame) {
		this.applicationFrame = applicationFrame;
	}

	public void switchWindow(BasicWindow newWindow) {
		List<ActionMapping> actionMappings;
		
		if (currentWindow != null) {
			// Remove the action mappings added in from the window
			actionMappings = currentWindow.getActionMappings();
			if (actionMappings != null) {
				for (ActionMapping actionMapping : actionMappings) {
					applicationFrame.getWindow().getActionMappings().remove(actionMapping);
				}
			}
			this.currentWindow.exit(this.applicationFrame);
			applicationFrame.setMenuBar(null);
		}

		this.currentWindow = newWindow;
		
		newWindow.enter(applicationFrame);
		applicationFrame.getWindow().setTitle(newWindow.getTitle());
		applicationFrame.setMenuBar(newWindow.getMenuBar());
		
		// Add action mappings
		actionMappings = newWindow.getActionMappings();
		if (actionMappings != null) {
			for (ActionMapping actionMapping : actionMappings) {
				applicationFrame.getWindow().getActionMappings().add(actionMapping);
			}
		}
		
		applicationFrame.setContent(newWindow.getContent());
		
	}

}
