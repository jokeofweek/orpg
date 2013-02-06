package orpg.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class LoginWindow extends JFrame {

	private static final long serialVersionUID = 8720835145332237954L;

	public LoginWindow() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Login");
		setupComponents();
		pack();
		setLocationRelativeTo(null);
		setVisible(true);

	}

	private void setupComponents() {
		JButton button = new JButton("Login");

		final JFrame window = this;

		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new MapEditorWindow();
				window.setVisible(false);
			}
		});

		add(button);
	}

}
