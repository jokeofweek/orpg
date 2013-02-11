package orpg.editor;

public interface EditorWindow<K> {

	public void load(BaseEditor baseEditor);

	public String[] validate(BaseEditor baseEditor);
	
	public void save(BaseEditor baseEditor);	
	
}
