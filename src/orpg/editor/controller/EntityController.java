package orpg.editor.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;

import org.lwjgl.opencl.CL;

import orpg.editor.BaseEditor;
import orpg.editor.EditorWindow;
import orpg.editor.data.EditorUpdateMessage;
import orpg.editor.util.PropertyDescriptorAdapter;
import orpg.editor.util.Reflection;
import orpg.shared.Callback;
import orpg.shared.data.ComponentList;
import orpg.shared.data.annotations.Attachable;
import orpg.shared.data.annotations.Editable;
import orpg.shared.data.annotations.Requires;
import orpg.shared.data.component.AttachableComponentDescriptor;

import com.artemis.Component;
import com.artemis.Entity;
import com.l2fprod.common.beans.ExtendedPropertyDescriptor;
import com.l2fprod.common.propertysheet.Property;

public class EntityController extends EditorController<ComponentList> implements
		PropertyChangeListener {

	/*
	 * Load all the attachable components and cache them.
	 */
	private static final Set<AttachableComponentDescriptor> ATTACHABLE_COMPONENTS = Reflection
			.getAttachableComponentDescriptors();
	private static final Map<Class<? extends Component>, AttachableComponentDescriptor> ATTACHABLE_DESCRIPTORS_BY_CLASS = new HashMap<Class<? extends Component>, AttachableComponentDescriptor>(
			ATTACHABLE_COMPONENTS.size());

	static {
		for (AttachableComponentDescriptor descriptor : ATTACHABLE_COMPONENTS) {
			ATTACHABLE_DESCRIPTORS_BY_CLASS.put(descriptor.getComponentClass(),
					descriptor);
		}
	}

	private Set<AttachableComponentDescriptor> availableComponents;

	private HashMap<String, Class<? extends Component>> classesByPropertyTag;
	private HashMap<Class<? extends Component>, List<Property>> classProperties;
	private HashMap<Class<? extends Component>, Component> componentInstances;

	private Callback<ComponentList> saveCallback;

	public EntityController(BaseEditor baseEditor, ComponentList componentList,
			Callback<ComponentList> saveCallback) {
		super(baseEditor);

		this.availableComponents = new TreeSet<AttachableComponentDescriptor>(
				ATTACHABLE_COMPONENTS);
		this.classProperties = new HashMap<Class<? extends Component>, List<Property>>();
		this.classesByPropertyTag = new HashMap<String, Class<? extends Component>>();
		this.componentInstances = new HashMap<Class<? extends Component>, Component>();
		this.saveCallback = saveCallback;

		if (componentList != null) {
			loadComponentList(componentList);
		}
	}

	private String getPropertyTag(Property property) {
		return property.getCategory() + "_" + property.getName();
	}

	private void loadComponentList(ComponentList componentList) {
		for (Component component : componentList.getComponents()) {
			// If the component is attachable, attach it, else just
			// add it to the instances
			if (ATTACHABLE_DESCRIPTORS_BY_CLASS.containsKey(component
					.getClass())) {
				attachComponent(component);
			} else {
				componentInstances.put(component.getClass(), component);
			}
		}
	}

	private void attachComponent(
			AttachableComponentDescriptor componentDescriptor,
			Component component) {
		if (availableComponents.contains(componentDescriptor)) {
			availableComponents.remove(componentDescriptor);

			// Add the descriptors
			Class clazz = componentDescriptor.getComponentClass();
			List<Property> properties = new ArrayList<Property>();

			// Iterate through all fields on the class, collecting the ones
			// annotated with Editable
			ExtendedPropertyDescriptor descriptor;
			Property property;
			Editable annotation;

			for (Field field : clazz.getFields()) {
				if ((annotation = field.getAnnotation(Editable.class)) != null) {
					try {
						// Create the descriptor based on the field and
						// annotation
						descriptor = new ExtendedPropertyDescriptor(
								field.getName(), clazz);
						descriptor.setCategory(((Attachable) (clazz
								.getAnnotation(Attachable.class))).name());
						descriptor.setDisplayName(annotation.name());
						descriptor
								.setShortDescription(annotation.description());

						// Convert to a property, loading value if component
						// instance is present
						property = new PropertyDescriptorAdapter(descriptor);
						if (component != null) {
							property.readFromObject(component);
						}

						properties.add(property);

						// Add the tag
						this.classesByPropertyTag.put(getPropertyTag(property),
								clazz);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			// Set the class properties
			this.classProperties.put(clazz, properties);

			// Create a new instance
			if (component != null) {
				this.componentInstances.put(clazz, component);
			} else {
				try {
					this.componentInstances.put(clazz,
							(Component) clazz.newInstance());
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(
							"Could not attach component of class "
									+ clazz.getName()
									+ ". No empty constructor.");
				}
			}

			// Pass the descriptors to the observers
			setChanged();
			notifyObservers(properties);
		}
	}

	public List<Property> getCurrentProperties() {
		List<Property> properties = new ArrayList<Property>(
				classProperties.size() * 2);
		for (List<Property> classPropertiesList : classProperties.values()) {
			properties.addAll(classPropertiesList);
		}
		return properties;
	}

	public void attachComponent(Component component) {
		Class<? extends Component> clazz = component.getClass();
		for (AttachableComponentDescriptor descriptor : ATTACHABLE_COMPONENTS) {
			if (descriptor.getComponentClass() == clazz) {
				attachComponent(descriptor, component);
				return;
			}
		}

	}

	public void attachComponent(AttachableComponentDescriptor descriptor) {
		attachComponent(descriptor, null);
	}

	public Set<AttachableComponentDescriptor> getAvailableComponents() {
		return availableComponents;
	}

	@Override
	public List<String> validate() {
		// Iterate through all our current component instances, making sure we
		// have prereqs.
		List<String> errors = new ArrayList<String>();
		Requires requires;
		AttachableComponentDescriptor descriptor;

		for (Class<? extends Component> clazz : componentInstances.keySet()) {
			descriptor = ATTACHABLE_DESCRIPTORS_BY_CLASS.get(clazz);
			// Only process if attachable
			if (descriptor != null) {
				if ((requires = clazz.getAnnotation(Requires.class)) != null) {
					for (Class<? extends Component> dependency : requires
							.dependencies()) {
						if (!componentInstances.containsKey(dependency)) {
							errors.add("The "
									+ descriptor.getName()
									+ " component requires a "
									+ ATTACHABLE_DESCRIPTORS_BY_CLASS.get(
											dependency).getName()
									+ " component.");
						}
					}
				}
			}
		}

		return errors;
	}

	@Override
	public void beforeSave() {
	}

	@Override
	public void save() {
		// Create a component list using the components
		List<Component> components = new ArrayList<Component>(
				componentInstances.values().size());
		for (Component component : componentInstances.values()) {
			components.add(component);
		}

		ComponentList entity = new ComponentList(components);

		if (saveCallback != null) {
			saveCallback.invoke(entity);
		}

		// Notify observers we've saved
		setChanged();
		notifyObservers(EditorUpdateMessage.SAVE);

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() instanceof Property) {
			// Attempt to find the instance by the tag, and update it
			Property property = (Property) evt.getSource();
			Class<? extends Component> clazz = classesByPropertyTag
					.get(getPropertyTag(property));
			if (clazz != null) {
				property.writeToObject(componentInstances.get(clazz));
			}
		}
	}

}
