package orpg.client.net.handlers;

import javax.swing.JOptionPane;

import orpg.client.data.ClientReceivedPacket;
import orpg.client.net.BaseClient;
import orpg.shared.ErrorMessage;

public class ErrorPacketHandler implements ClientPacketHandler {

	@Override
	public void handle(ClientReceivedPacket packet, BaseClient baseClient) {
		// Determine type of the error
		boolean isEnum = packet.getByteBuffer().getBoolean();

		if (isEnum) {
			int val = packet.getByteBuffer().getInt();
			JOptionPane.showMessageDialog(null, ErrorMessage.values()[val]);
		} else {
			String error = packet.getByteBuffer().getString();
			JOptionPane.showMessageDialog(null, error);
		}
	}

}
