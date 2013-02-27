package orpg.client.net.handlers;

import javax.swing.JOptionPane;

import orpg.client.BaseClient;
import orpg.client.data.ClientReceivedPacket;
import orpg.shared.ErrorMessage;
import orpg.shared.net.AbstractClient;

public class ErrorPacketHandler implements ClientPacketHandler {

	@Override
	public void handle(ClientReceivedPacket packet, BaseClient baseClient) {
		// Determine type of the error
		boolean isEnum = packet.getByteBuffer().getBoolean();

		if (isEnum) {
			int val = packet.getByteBuffer().getInt();
			baseClient.getStateManager().getCurrentState()
					.displayError(ErrorMessage.values()[val].getMessage());
		} else {
			String error = packet.getByteBuffer().getString();
			baseClient.getStateManager().getCurrentState().displayError(error);
		}
	}

}
