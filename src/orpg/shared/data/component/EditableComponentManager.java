package orpg.shared.data.component;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

public class EditableComponentManager {

	private static EditableComponentManager instance = new EditableComponentManager();
	
	public static EditableComponentManager getInstance() {
		return instance;
	}

	private Set<EditableComponentDescriptor> componentDescriptors;
	
	private EditableComponentManager(){
		this.componentDescriptors = new TreeSet<EditableComponentDescriptor>();
	}	
	
	public void register(EditableComponentDescriptor descriptor) {
		this.componentDescriptors.add(descriptor);
	}
}
