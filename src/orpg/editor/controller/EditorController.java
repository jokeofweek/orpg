package orpg.editor.controller;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import orpg.editor.BaseEditor;
import orpg.editor.EditorWindow;

public abstract class EditorController<K> extends Observable {

	protected BaseEditor baseEditor;

	private List<EditorWindow<K>> editorWindows;

	private Action saveAction;

	public EditorController(final BaseEditor baseEditor) {
		this.baseEditor = baseEditor;
		this.editorWindows = new ArrayList<EditorWindow<K>>();
		this.saveAction = new AbstractAction("Save") {
			@Override
			public void actionPerformed(ActionEvent e) {
				// First we validate to check.
				List<String> errors = validate();
				if (errors != null && errors.size() > 0) {
					// Errors occurred!
					String errorMessages = "";
					for (String error : errors) {
						errorMessages += error + "\n";
					}
					JOptionPane.showMessageDialog(null, errorMessages);
				} else {
					// First trigger the window save
					beforeSave();
					// Then do the controller save
					save();
				}
			}
		};
	}

	public abstract List<String> validate();

	public abstract void beforeSave();

	public abstract void save();

	public Action getSaveAction() {
		return saveAction;
	}

	public BaseEditor getBaseEditor() {
		return baseEditor;
	}

}
