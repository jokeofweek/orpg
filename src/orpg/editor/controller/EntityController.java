package orpg.editor.controller;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import orpg.editor.BaseEditor;
import orpg.editor.EditorWindow;
import orpg.editor.util.Reflection;
import orpg.shared.data.ComponentList;
import orpg.shared.data.annotations.Editable;
import orpg.shared.data.component.AttachableComponentDescriptor;

import com.artemis.Component;
import com.artemis.Entity;
import com.l2fprod.common.beans.ExtendedPropertyDescriptor;

public class EntityController extends EditorController<ComponentList> {

	private static final Set<AttachableComponentDescriptor> ATTACHABLE_COMPONENTS = Reflection
			.getAttachableComponentDescriptors();

	private Set<AttachableComponentDescriptor> availableComponents;
	private HashMap<Class<? extends Component>, List<String>> classProperties;

	public EntityController(BaseEditor baseEditor,
			EditorWindow<ComponentList> editorWindow) {
		super(baseEditor, editorWindow);
		this.availableComponents = new TreeSet<AttachableComponentDescriptor>(
				ATTACHABLE_COMPONENTS);
		this.classProperties = new HashMap<Class<? extends Component>, List<String>>();
	}

	public void attachComponent(AttachableComponentDescriptor component) {
		if (availableComponents.contains(component)) {
			availableComponents.remove(component);

			// Add the descriptors
			Class clazz = component.getComponentClass();
			ArrayList<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
			List<String> properties = new ArrayList<String>(
					clazz.getFields().length);

			// Iterate through all fields on the class, collecting the ones
			// annotated with Editable
			ExtendedPropertyDescriptor descriptor;
			Editable annotation;

			for (Field field : clazz.getFields()) {
				if ((annotation = field.getAnnotation(Editable.class)) != null) {
					try {
						descriptor = new ExtendedPropertyDescriptor(
								field.getName(), clazz);
						descriptor.setCategory(clazz.getSimpleName());
						properties.add(descriptor.getCategory() + "_"
								+ descriptor.getName());
						// fieldBeanClasses.put(descriptor.getCategory()
						// + "_" + descriptor.getName(), clazz);
						descriptor.setDisplayName(annotation.name());
						descriptor.setShortDescription(annotation
								.description());
						descriptors.add(descriptor);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			// Set the class properties
			this.classProperties.put(clazz, properties);
			
			// Pass the descriptors to the observers
			ExtendedPropertyDescriptor[] arr = new ExtendedPropertyDescriptor[descriptors.size()];
			descriptors.toArray(arr);
			
			setChanged();
			notifyObservers(arr);
		}
	}

	public Set<AttachableComponentDescriptor> getAvailableComponents() {
		return availableComponents;
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

}
