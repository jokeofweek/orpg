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

	public ChatController(BaseClient baseClient) {
		this.baseClient = baseClient;
	}

	public void addMessage(Message message) {
		this.setChanged();
		this.notifyObservers(message);
	}
}
