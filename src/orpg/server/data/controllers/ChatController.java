package orpg.server.data.controllers;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import orpg.server.BaseServer;
import orpg.server.ServerSession;
import orpg.shared.ChatChannel;

public class ChatController {

	private BaseServer baseServer;
	private Map<ChatChannel, Set<ServerSession>> channelSessions;

	public ChatController(BaseServer baseServer) {
		this.baseServer = baseServer;
		this.channelSessions = new HashMap<ChatChannel, Set<ServerSession>>(
				ChatChannel.values().length);

		for (ChatChannel channel : ChatChannel.values()) {
			channelSessions.put(channel, new HashSet<ServerSession>());
		}
	}

	public void addSession(ServerSession session,
			Collection<ChatChannel> channels) {
		synchronized (channelSessions) {
			for (ChatChannel channel : channels) {
				channelSessions.get(channel).add(session);
			}
		}
	}

	public void removeSession(ServerSession serverSession) {
		synchronized (channelSessions) {
			for (Set<ServerSession> sessions : channelSessions.values()) {
				sessions.remove(serverSession);
			}
		}
	}

}
