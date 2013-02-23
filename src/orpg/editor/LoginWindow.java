package orpg.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;

public class LoginWindow extends JFrame {

	private static final long serialVersionUID = 8720835145332237954L;
	private EditorConfigurationManager config;

	public LoginWindow(EditorConfigurationManager config) {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Login");
		setupComponents();
		pack();
		setLocationRelativeTo(null);
		setVisible(true);

		this.config = config;

	}

	private void setupComponents() {
		JButton button = new JButton("Login");

		final JFrame window = this;

		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Set up the client
				try {
					Socket s = new Socket(config.getServerAddress(), config
							.getServerPort());
					BaseEditor baseEditor = new BaseEditor(s, config);
					window.setVisible(false);
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		add(button);
	}

}
