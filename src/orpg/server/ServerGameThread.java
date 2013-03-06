package orpg.server;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

import orpg.server.data.ServerReceivedPacket;
import orpg.server.net.handlers.ClientMoveRequestHandler;
import orpg.server.net.handlers.ClientNeedSegmentHandler;
import orpg.server.net.handlers.CreateAccountHandler;
import orpg.server.net.handlers.EditorEditMapHandler;
import orpg.server.net.handlers.EditorReadyHandler;
import orpg.server.net.handlers.EditorRequestSegmentHandler;
import orpg.server.net.handlers.EditorSaveMapHandler;
import orpg.server.net.handlers.LoginHandler;
import orpg.server.net.handlers.ServerPacketHandler;
import orpg.server.net.handlers.UseCharacterHandler;
import orpg.server.net.packets.ServerPacket;
import orpg.shared.net.ClientPacketType;

/**
 * This thread is responsible for handling all packets in the input queue as
 * well as potentially adding packets to the output queue.
 * 
 * @author Dominic Charley-Roy
 * 
 */
public class ServerGameThread implements Runnable {

	private BlockingQueue<ServerReceivedPacket> inputQueue;
	private BlockingQueue<ServerPacket> outputQueue;
	private BaseServer baseServer;
	private HashMap<ClientPacketType, ServerPacketHandler> anonymousHandlers;
	private HashMap<ClientPacketType, ServerPacketHandler> gameHandlers;
	private HashMap<ClientPacketType, ServerPacketHandler> loggedInHandlers;
	private HashMap<ClientPacketType, ServerPacketHandler> editorHandlers;

	public ServerGameThread(BaseServer baseServer,
			BlockingQueue<ServerReceivedPacket> inputQueue,
			BlockingQueue<ServerPacket> outputQueue) {
		this.baseServer = baseServer;
		this.inputQueue = inputQueue;
		this.outputQueue = outputQueue;

		setupAnonymousHandlers();
		setupLoggedInHandlers();
		setupGameHandlers();
		setupEditorHandlers();
	}

	private void setupAnonymousHandlers() {
		this.anonymousHandlers = new HashMap<ClientPacketType, ServerPacketHandler>();
		anonymousHandlers.put(ClientPacketType.LOGIN, new LoginHandler());
		anonymousHandlers.put(ClientPacketType.CREATE_ACCOUNT, new CreateAccountHandler());
	}

	private void setupLoggedInHandlers() {
		this.loggedInHandlers = new HashMap<ClientPacketType, ServerPacketHandler>();
		loggedInHandlers.put(ClientPacketType.USE_CHARACTER, new UseCharacterHandler());
	}

	private void setupGameHandlers() {
		this.gameHandlers = new HashMap<ClientPacketType, ServerPacketHandler>();
		gameHandlers.put(ClientPacketType.CLIENT_NEED_SEGMENT, new ClientNeedSegmentHandler());
		gameHandlers.put(ClientPacketType.CLIENT_MOVE_REQUEST, new ClientMoveRequestHandler());
	}

	private void setupEditorHandlers() {
		this.editorHandlers = new HashMap<ClientPacketType, ServerPacketHandler>();
		editorHandlers.put(ClientPacketType.EDITOR_READY, new EditorReadyHandler());
		editorHandlers.put(ClientPacketType.EDITOR_EDIT_MAP, new EditorEditMapHandler());
		editorHandlers.put(ClientPacketType.EDITOR_SAVE_MAP, new EditorSaveMapHandler());
		editorHandlers.put(ClientPacketType.EDITOR_REQUEST_SEGMENT, new EditorRequestSegmentHandler());
	}

	@Override
	public void run() {
		ServerReceivedPacket packet = null;
		ServerPacketHandler handler = null;
		try {
			while (true) {
				packet = inputQueue.take();
				// Determine the right handler for the packet based on
				// session type.
				switch (packet.getSession().getSessionType()) {
				case ANONYMOUS:
					handler = anonymousHandlers.get(packet.getType());
					break;
				case LOGGED_IN:
					handler = loggedInHandlers.get(packet.getType());
					break;
				case GAME:
					handler = gameHandlers.get(packet.getType());
					break;
				case EDITOR:
					handler = editorHandlers.get(packet.getType());
					break;
				}

				// Execute handler or disconnect.
				if (handler == null) {
					packet.getSession().disconnect("Invalid packet.");
				} else {
					handler.handle(packet, baseServer);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
