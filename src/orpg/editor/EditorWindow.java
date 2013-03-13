package orpg.editor;

import java.util.List;

import orpg.editor.controller.EditorController;

public interface EditorWindow<K> {

	public void load(BaseEditor baseEditor);

	public List<String> validate(BaseEditor baseEditor);
		
	public void beforeSave(BaseEditor baseEditor);	
	
}
