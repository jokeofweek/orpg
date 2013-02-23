package orpg.editor;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import orpg.shared.Constants;
import orpg.shared.Strings;

public class EditorApplication {

	public static void main(String[] args) {
		try {
			// Attempt to load the configuration
			final EditorConfigurationManager config = new EditorConfigurationManager(
					new String[] { Constants.EDITOR_DATA_PATH
							+ "editor.properties" });

			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					// Setup look and feel
					try {
						// UIManager.setLookAndFeel(UIManager
						// .getSystemLookAndFeelClassName());
					} catch (Exception e) {
					}

					new LoginWindow(config);
				}
			});
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null,
					Strings.EDITOR_ERROR_LOADING_PROPERTIES);
			System.exit(1);
		}
	}

}
