package orpg.client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import orpg.client.data.ClientSentPacket;
import orpg.shared.ClientPacketType;

public class ClientWindow extends JFrame {

	private BaseClient baseClient;
	public static JTextArea textArea;
	private JTextField enterTextField;

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

		textArea = new JTextArea();
		basePanel.add(textArea);

		JPanel entryPanel = new JPanel(new BorderLayout());
		this.enterTextField = new JTextField();
		entryPanel.add(enterTextField);

		JButton sendButton = new JButton("Send");
		entryPanel.add(sendButton, BorderLayout.EAST);
		sendButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (enterTextField.getText().length() == 0) {
					return;
				}
				
				baseClient.getOutputQueue().add(
						new ClientSentPacket(ClientPacketType.PING,
								enterTextField.getText()));
				enterTextField.setText("");

			}
		});

		basePanel.add(entryPanel, BorderLayout.SOUTH);
		
		this.add(basePanel);

	}

}
