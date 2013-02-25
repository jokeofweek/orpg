package orpg.editor.data.change;

public interface EditorChange {

	public void apply();

	public void undo();

	public boolean canUndo();

}
