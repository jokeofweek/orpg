package orpg.editor;

import javax.swing.SwingUtilities;

public class EditorApplication {

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// Setup look and feel
				try {
				//	UIManager.setLookAndFeel(UIManager
				//			.getSystemLookAndFeelClassName());
				} catch (Exception e) {
				}

				new LoginWindow();
			}
		});
	}

}
