package orpg.server;

import java.net.Socket;
import java.net.SocketException;
import java.util.PriorityQueue;
import java.util.logging.Level;

import orpg.server.data.ServerSentPacket;

public class ServerSession {

	private BaseServer baseServer;
	private PriorityQueue<ServerSentPacket> outputQueue;
	private SessionType sessionType;
	private volatile boolean connected;
	private String disconnectReason;
	private ServerSessionThread thread;
	private String id;

	public ServerSession(BaseServer baseServer, Socket socket)
			throws SocketException {
		this.baseServer = baseServer;
		this.outputQueue = new PriorityQueue<ServerSentPacket>();
		this.setSessionType(SessionType.GAME);

		this.id = socket.getInetAddress().toString();

		// Start the session thread
		this.thread = new ServerSessionThread(baseServer, socket,
				this);
		this.thread.start();

		this.connected = true;
	}

	public String getId() {
		return id;
	}

	public void setSessionType(SessionType sessionType) {
		baseServer
				.getConfigManager()
				.getSessionLogger()
				.log(Level.FINE,
						String.format(
								"Session %s changed session type from %s to %s",
								id, this.sessionType, sessionType));
		this.sessionType = sessionType;
	}

	public SessionType getSessionType() {
		return sessionType;
	}

	public PriorityQueue<ServerSentPacket> getOutputQueue() {
		return outputQueue;
	}

	public boolean isConnected() {
		return connected;
	}

	public void disconnect() {
		disconnect("Client disconnect.");
	}

	public void disconnect(String reason) {
		baseServer
				.getConfigManager()
				.getSessionLogger()
				.log(Level.INFO,
						String.format("Session %s disconnected for reason %s.",
								getId(), reason));
		baseServer.getServerSessionManager().removeSession(this);

		this.connected = false;
		this.disconnectReason = reason;

	}

	public String getDisconnectReason() {
		return disconnectReason;
	}

}
