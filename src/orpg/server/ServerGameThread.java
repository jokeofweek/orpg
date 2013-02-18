package orpg.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.logging.Level;

import orpg.server.data.ServerReceivedPacket;
import orpg.server.data.SessionType;
import orpg.server.net.handlers.CreateAccountHandler;
import orpg.server.net.handlers.EditorEditMapHandler;
import orpg.server.net.handlers.LoginHandler;
import orpg.server.net.handlers.EditorReadyHandler;
import orpg.server.net.handlers.EditorSaveMapHandler;
import orpg.server.net.handlers.ServerPacketHandler;
import orpg.server.net.packets.ServerPacket;
import orpg.shared.Priority;
import orpg.shared.data.Map;
import orpg.shared.net.ClientPacketType;
import orpg.shared.net.InputByteBuffer;
import orpg.shared.net.OutputByteBuffer;
import orpg.shared.net.ServerPacketType;

/**
 * This thread is responsible for handling all packets in the input queue as
 * well as potentially adding packets to the output queue.
 * 
 * @author Dominic Charley-Roy
 * 
 */
public class ServerGameThread implements Runnable {

	private Queue<ServerReceivedPacket> inputQueue;
	private Queue<ServerPacket> outputQueue;
	private BaseServer baseServer;
	private HashMap<ClientPacketType, ServerPacketHandler> anonymousHandlers;
	private HashMap<ClientPacketType, ServerPacketHandler> clientHandlers;
	private HashMap<ClientPacketType, ServerPacketHandler> editorHandlers;

	public ServerGameThread(BaseServer baseServer, Queue<ServerReceivedPacket> inputQueue, Queue<ServerPacket> outputQueue) {
		this.baseServer = baseServer;
		this.inputQueue = inputQueue;
		this.outputQueue = outputQueue;

		setupAnonymousHandlers();
		setupClientHandlers();
		setupEditorHandlers();
	}

	private void setupAnonymousHandlers() {
		this.anonymousHandlers = new HashMap<ClientPacketType, ServerPacketHandler>();

		anonymousHandlers.put(ClientPacketType.LOGIN, new LoginHandler());
		anonymousHandlers.put(ClientPacketType.CREATE_ACCOUNT,
				new CreateAccountHandler());
	}

	private void setupClientHandlers() {
		this.clientHandlers = new HashMap<ClientPacketType, ServerPacketHandler>();
	}

	private void setupEditorHandlers() {
		this.editorHandlers = new HashMap<ClientPacketType, ServerPacketHandler>();
		editorHandlers.put(ClientPacketType.EDITOR_READY,
				new EditorReadyHandler());
		editorHandlers.put(ClientPacketType.EDITOR_EDIT_MAP,
				new EditorEditMapHandler());
		editorHandlers.put(ClientPacketType.EDITOR_SAVE_MAP,
				new EditorSaveMapHandler());
	}

	@Override
	public void run() {
		ServerReceivedPacket packet = null;
		ServerPacketHandler handler = null;
		while (true) {
			if (!inputQueue.isEmpty()) {
				packet = inputQueue.remove();

				// Determine the right handler for the packet based on session
				// type.
				switch (packet.getSession().getSessionType()) {
				case GAME:
					handler = clientHandlers.get(packet.getType());
					break;
				case ANONYMOUS:
					handler = anonymousHandlers.get(packet.getType());
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
		}
	}

}
