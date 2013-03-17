package orpg.client.data.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import orpg.client.BaseClient;
import orpg.client.ClientConstants;
import orpg.server.BaseServer;
import orpg.shared.data.Message;

public class ChatController extends Observable {

	private BaseClient baseClient;
	private List<Message> messages;

	public ChatController(BaseClient baseClient) {
		this.baseClient = baseClient;
		this.messages = new ArrayList<Message>(ClientConstants.CHAT_HISTORY);
	}

	public void addMessage(Message message) {
		// Remove message if at limit
		synchronized (messages) {
			if (this.messages.size() == ClientConstants.CHAT_HISTORY) {
				this.messages.remove(0);
			}

			this.messages.add(message);

			this.setChanged();
			this.notifyObservers(message);
		}
	}

	public List<Message> getMessages() {
		return messages;
	}
}
