import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class SwingTest extends JFrame {

	public SwingTest() {
		try {
			UIManager.setLookAndFeel(UIManager
					.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.setTitle("Test");
		this.add(getContent());
		this.setVisible(true);
		this.pack();
	}

	private Component getContent() {
		JTabbedPane tabbedPane = new JTabbedPane();

		tabbedPane.addTab("Test", new JLabel("Test"));

		return tabbedPane;
	}

	public static void main(String... args) {
		new SwingTest();
	}

}
