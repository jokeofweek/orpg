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

	private EditableComponentManager() {
		this.componentDescriptors = new TreeSet<EditableComponentDescriptor>();

		register(new EditableComponentDescriptor(
				"Collision Handler",
				"This component allows you to execute certain actions upon collision.",
				Collidable.class));

		register(new EditableComponentDescriptor(
				"Named",
				"This component allows you to assign a name to an entity.",
				Named.class));
	}

	public Set<EditableComponentDescriptor> getDescriptors() {
		return componentDescriptors;
	}

	public void register(EditableComponentDescriptor descriptor) {
		this.componentDescriptors.add(descriptor);
	}
}
