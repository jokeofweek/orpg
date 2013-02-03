package orpg.editor.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class EditorChangeManager extends Observable{

	private List<EditorChange> editorChanges;
	
	// this will always point at the index after the last change applied
	private int changePosition;

	public EditorChangeManager() {
		this.editorChanges = new ArrayList<EditorChange>();
		this.changePosition = 0;
	}
	
	public void addChange(EditorChange change) {
		// We must remove everything after current change position
		int i = editorChanges.size() - 1;
		while (editorChanges.size() > changePosition) {
			editorChanges.remove(i);
			i--;
		}
		
		// Add the change
		change.apply();
		editorChanges.add(change);
		changePosition++;
		
		// Notify
		this.setChanged();
		this.notifyObservers();
	}
	
	public void redo() {
		if (!canRedo()) {
			throw new IllegalStateException("Cannot redo.");
		}
	}
	
	public boolean canRedo() {
		return this.changePosition != this.editorChanges.size();
	}
	
	public void undo() {
		// Undo the hnage
	}
	
	public boolean canUndo() {
		return this.changePosition != 0;
	}

}
