package orpg.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This thread is responsible for accepting new connections and establishing
 * sessions.
 * 
 * @author Dominic Charley-Roy
 * 
 */
public class ServerSocketThread implements Runnable {

	private ServerSocket socket;
	private BaseServer server;

	public ServerSocketThread(int port, BaseServer server)
			throws IOException {
		this.socket = new ServerSocket(port);
		this.server = server;
	}

	@Override
	public void run() {
		Socket s = null;
		ServerSession session = null;
		while (true) {
			try {
				s = socket.accept();
				server.getConsole()
						.out()
						.println(
								"Connection accepted from "
										+ s.getInetAddress());

				// Establish the session, add it to the session manager and then
				// run it.
				session = new ServerSession(s, server);
				server.getServerSessionManager().addSession(session);
				new Thread(session).start();
			} catch (IOException e) {
				server.getConsole()
						.out()
						.println(
								"Error accepting socket: "
										+ e.getMessage());
			}

		}
	}

}
