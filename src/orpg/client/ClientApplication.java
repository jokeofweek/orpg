package orpg.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.SwingUtilities;

import orpg.client.net.ClientProcessThread;
import orpg.shared.net.AbstractClient;

public class ClientApplication {

	public static void main(String[] args) throws UnknownHostException,
			IOException, InterruptedException, InstantiationException,
			IllegalAccessException {
		Socket s = new Socket("localhost", 8000);
		
		final AbstractClient baseClient = new AbstractClient(s, new ClientProcessThread(), null);
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new ClientWindow(baseClient);
			}
		});
	}

}
