package orpg.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.SwingUtilities;

import orpg.client.net.BaseClient;
import orpg.client.net.ClientGameThread;

public class ClientApplication {

	public static void main(String[] args) throws UnknownHostException,
			IOException, InterruptedException, InstantiationException,
			IllegalAccessException {
		Socket s = new Socket("localhost", 8000);
		
		final BaseClient baseClient = new BaseClient(s,
				ClientGameThread.class);
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new ClientWindow(baseClient);
			}
		});
	}

}
