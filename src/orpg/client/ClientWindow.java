package orpg.client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import orpg.client.data.ClientSentPacket;
import orpg.client.net.BaseClient;
import orpg.client.net.packets.CreateAccountPacket;
import orpg.shared.net.ClientPacketType;
import orpg.shared.net.OutputByteBuffer;

public class ClientWindow extends JFrame {

	private BaseClient baseClient;

	public ClientWindow(BaseClient baseClient) {
		super("Chat Client");

		this.baseClient = baseClient;

		setupUI();
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	public void setupUI() {
		JPanel basePanel = new JPanel(new BorderLayout());
		JPanel registrationForm = new JPanel(new GridLayout(3, 2));

		registrationForm.add(new JLabel("Account Name:"));
		final JTextField accountNameField = new JTextField();
		registrationForm.add(accountNameField);

		registrationForm.add(new JLabel("Email:"));
		final JTextField emailField = new JTextField();
		registrationForm.add(emailField);

		registrationForm.add(new JLabel("Password:"));
		final JPasswordField passwordField = new JPasswordField();
		registrationForm.add(passwordField);

		basePanel.add(registrationForm);

		JButton createButton = new JButton("Create Account");
		createButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				baseClient.getOutputQueue().add(
						new CreateAccountPacket(accountNameField.getText(),
								emailField.getText(), passwordField
										.getPassword()));
			}
		});

		basePanel.add(createButton, BorderLayout.SOUTH);
		
		this.add(basePanel);

	}

}
