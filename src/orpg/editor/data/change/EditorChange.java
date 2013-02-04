package orpg.editor.data.change;

public interface EditorChange {

	public void apply();
	
	public boolean canApply();
	
	public void undo();
	
	public boolean canUndo();
	
}
