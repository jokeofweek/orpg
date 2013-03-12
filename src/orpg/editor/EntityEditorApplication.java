package orpg.editor;

import javax.swing.SwingUtilities;

public class EntityEditorApplication {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new EntityWindow();
			}
		});
	}
	
}
