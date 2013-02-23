package orpg.editor.controller;

import java.awt.event.ActionEvent;
import java.util.Observable;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import orpg.editor.BaseEditor;
import orpg.editor.EditorWindow;

public abstract class EditorController<K> extends Observable {

	private EditorWindow<K> editorWindow;
	private BaseEditor baseEditor;
	private K editingObject;

	private Action saveAction;

	public EditorController(final BaseEditor baseEditor,
			final EditorWindow<K> editorWindow) {
		this.baseEditor = baseEditor;
		this.editorWindow = editorWindow;

		this.saveAction = new AbstractAction("Save") {
			@Override
			public void actionPerformed(ActionEvent e) {
				// First we validate to check.
				String[] errors = editorWindow.validate(baseEditor);
				if (errors != null && errors.length > 0) {
					// Errors occurred!
					String errorMessages = "";
					for (String error : errors) {
						errorMessages += error + "\n";
					}
					JOptionPane.showMessageDialog(null, errorMessages);
				} else {
					// First trigger the window save
					editorWindow.save(baseEditor);
					// Then do the controller save
					save();
				}
			}
		};
	}
	
	public abstract void save();

	public Action getSaveAction() {
		return saveAction;
	}

	public BaseEditor getBaseEditor() {
		return baseEditor;
	}

}
