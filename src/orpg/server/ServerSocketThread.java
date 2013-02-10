package orpg.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;

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

	public ServerSocketThread(BaseServer server, int port) throws IOException {
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
				// Establish the session, add it to the session manager and then
				// run it.
				session = new ServerSession(server, socket);
				server.getServerSessionManager().addSession(session);
			} catch (IOException e) {
				server.getConfigManager()
						.getSessionLogger()
						.log(Level.SEVERE,
								String.format(
										"Could not accept connection from socket %s. Reason: %s",
										socket.getInetAddress().toString(),
										e.getMessage()));
			}

		}
	}

}
