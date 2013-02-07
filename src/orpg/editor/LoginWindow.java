package orpg.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;

import orpg.client.net.BaseClient;
import orpg.editor.net.EditorProcessThread;

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
				// Set up the client
				try {
					Socket s = new Socket("localhost", 8000);
					BaseClient baseClient = new BaseClient(s, EditorProcessThread.class);
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
