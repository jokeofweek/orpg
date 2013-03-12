package orpg.shared.data.component;

import java.lang.reflect.Constructor;

import com.artemis.Component;

public class EditableComponentDescriptor implements
		Comparable<EditableComponentDescriptor> {

	private String name;
	private String description;
	private Class<? extends Component> componentClass;

	public EditableComponentDescriptor(String name, String description,
			Class<? extends Component> componentClass) {
		// Ensure class has an empty constructor.
		boolean found = false;
		for (Constructor constructor : componentClass.getConstructors()) {
			if (constructor.getParameterTypes().length == 0) {
				found = true;
				break;
			}
		}

		if (!found) {
			throw new IllegalArgumentException(
					"The class "
							+ componentClass.getName()
							+ " cannot be editable as it does not have an empty constructor.");
		}

		this.name = name;
		this.description = description;
		this.componentClass = componentClass;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Class<? extends Component> getComponentClass() {
		return componentClass;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof EditableComponentDescriptor)) {
			return false;
		}
		if (obj == this) {
			return true;
		}

		// Two descriptors are equal if they point to the same class
		return this.componentClass
				.equals(((EditableComponentDescriptor) obj)
						.getComponentClass());
	}

	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public int compareTo(EditableComponentDescriptor o) {
		return this.name.compareTo(o.getName());
	}
}
