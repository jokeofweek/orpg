package orpg.server;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.PriorityQueue;
import java.util.Queue;

import orpg.server.data.ServerReceivedPacket;
import orpg.server.data.ServerSentPacket;

public class ServerSession {

	private BaseServer baseServer;
	private PriorityQueue<ServerSentPacket> outputQueue;
	private SessionType sessionType;
	private volatile boolean connected;
	private String disconnectReason;
	private ServerSessionThread serverSessionThread;

	public ServerSession(BaseServer baseServer, Socket socket)
			throws SocketException {
		this.baseServer = baseServer;
		this.outputQueue = new PriorityQueue<ServerSentPacket>();
		this.setSessionType(SessionType.GAME);

		// Start the session thread
		this.serverSessionThread = new ServerSessionThread(baseServer, socket,
				this);
		this.serverSessionThread.start();

		this.connected = true;
	}

	public void setSessionType(SessionType sessionType) {
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
		baseServer.getServerSessionManager().removeSession(this);
		baseServer.getConsole().out()
				.println("Socket disconnected. Reason: " + reason);
		this.connected = false;
		this.disconnectReason = reason;
	}

	public String getDisconnectReason() {
		return disconnectReason;
	}

}
