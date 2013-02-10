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

	private ServerSocket serverSocket;
	private BaseServer server;

	public ServerSocketThread(BaseServer server, int port)
			throws IOException {
		this.serverSocket = new ServerSocket(port);
		this.server = server;
	}

	@Override
	public void run() {
		Socket socket = null;
		ServerSession session = null;
		while (true) {
			try {
				socket = serverSocket.accept();
				server.getConsole()
						.out()
						.println(
								"Connection accepted from "
										+ socket.getInetAddress());

				// Establish the session, add it to the session manager and then
				// run it.
				session = new ServerSession(server, socket);
				server.getServerSessionManager().addSession(session);
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
