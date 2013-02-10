package orpg.editor;

import java.net.Socket;

import orpg.client.net.BaseClient;
import orpg.editor.net.EditorController;
import orpg.editor.net.EditorProcessThread;

public class BaseEditor extends BaseClient {

	private EditorController editorController;

	public BaseEditor(Socket socket) {
		super(socket, EditorProcessThread.class);
		this.editorController = new EditorController(this);
	}

	public EditorController getEditorController() {
		return editorController;
	}
}
