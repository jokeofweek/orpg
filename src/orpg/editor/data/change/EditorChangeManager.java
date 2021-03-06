package orpg.editor.data.change;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class EditorChangeManager extends Observable {

	private List<EditorChange> editorChanges;

	// this will always point at the index after the last change applied
	private int nextChange;

	public EditorChangeManager() {
		this.editorChanges = new ArrayList<EditorChange>();
		this.nextChange = 0;
	}

	public void addChange(EditorChange change) {
		// If this change cannot be undone, then just remove the list of editor
		// changes and add this one
		if (!change.canUndo()) {
			this.editorChanges = new ArrayList<EditorChange>();
			this.nextChange = 0;
		} else {
			// We must remove everything after current change position
			while (editorChanges.size() > nextChange) {
				editorChanges.remove(editorChanges.size() - 1);
			}
		}

		// Add the change
		change.apply();
		editorChanges.add(change);
		nextChange++;

		// Notify
		this.setChanged();
		this.notifyObservers();
	}

	/**
	 * This redoes the next change in the list of changes.
	 * 
	 * @throws IllegalStateException
	 *             if there are no changes to redo. This can be checked with
	 *             {@link #canRedo()}.
	 */
	public void redo() throws IllegalStateException {
		if (!canRedo()) {
			throw new IllegalStateException("No changes to redo.");
		}

		// Apply the next change
		editorChanges.get(nextChange).apply();
		nextChange++;

		// Notify
		this.setChanged();
		this.notifyObservers();
	}

	/**
	 * @return whether there is currently a change which can be redone.
	 */
	public boolean canRedo() {
		return this.nextChange != this.editorChanges.size();
	}

	/**
	 * This undoes the current last change in the list of changes.
	 * 
	 * @throws IllegalStateException
	 *             if there are no changes to undo. This can be checked with
	 *             {@link #canUndo()}.
	 */
	public void undo() {
		if (!canUndo()) {
			throw new IllegalStateException("No changes to undo.");
		}

		// Undo the previous change
		nextChange--;
		editorChanges.get(nextChange).undo();

		// Notify
		this.setChanged();
		this.notifyObservers();
	}

	/**
	 * @return whether there is currently a change which can be undone.
	 */
	public boolean canUndo() {
		return this.nextChange != 0
				&& this.editorChanges.get(nextChange - 1).canUndo();
	}

	/**
	 * This resets the change manager, erasing all changes currently in the
	 * system.
	 */
	public void reset() {
		this.editorChanges = new ArrayList<EditorChange>();
		this.nextChange = 0;
		this.setChanged();
		this.notifyObservers();
	}

}
