package orpg.client.net;

import orpg.client.ClientWindow;
import orpg.client.data.ClientReceivedPacket;

public class ClientGameThread extends ClientProcessThread {

	@Override
	public void handlePacket(ClientReceivedPacket p) {
		// Just print out the message if client window is setup
		if (ClientWindow.textArea != null) {
			ClientWindow.textArea.setText(ClientWindow.textArea.getText()
					+ p.getByteBuffer().getString() + "\r\n");
		}
	}

}
