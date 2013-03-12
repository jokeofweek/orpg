package orpg.editor;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;

import orpg.editor.controller.EntityController;
import orpg.shared.Strings;
import orpg.shared.data.ComponentList;
import orpg.shared.data.annotations.Editable;
import orpg.shared.data.component.EditableComponentManager;
import orpg.shared.data.component.Named;
import orpg.shared.data.component.Position;
import orpg.shared.data.component.Renderable;
import orpg.shared.data.component.TestComponent;

import com.artemis.Entity;
import com.l2fprod.common.beans.ExtendedPropertyDescriptor;
import com.l2fprod.common.propertysheet.DefaultProperty;
import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertySheetPanel;

public class EntityWindow extends JFrame implements EditorWindow<ComponentList> {

	private EntityController controller;

	public EntityWindow() {
		this.controller = new EntityController(null, this);

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle(Strings.ENGINE_NAME);
		this.setupContent();
		this.pack();
		this.setVisible(true);
		this.setSize(800, 600);
		this.setLocationRelativeTo(null);
		this.requestFocusInWindow();

		this.load(null);
	}

	public void setupContent() {
		// JList availableComponents = new JList(EditableComponentManager
		// .getInstance().getDescriptors().toArray());
		// this.add(availableComponents);

		List<DefaultProperty> properties = new ArrayList<DefaultProperty>();

		final PropertySheetPanel panel = new PropertySheetPanel();
		List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();

		Class[] classes = new Class[] { TestComponent.class, Named.class,
				Renderable.class };
		final HashMap<String, Class> fieldBeanClasses = new HashMap<String, Class>();

		ExtendedPropertyDescriptor descriptor;
		Editable annotation;

		for (Class clazz : classes) {
			for (Field field : clazz.getFields()) {
				if ((annotation = field.getAnnotation(Editable.class)) != null) {
					try {
						descriptor = new ExtendedPropertyDescriptor(
								field.getName(), clazz);
						descriptor.setCategory(clazz.getSimpleName());
						fieldBeanClasses.put(descriptor.getCategory() + "_"
								+ descriptor.getName(), clazz);
						descriptor.setDisplayName(annotation.name());
						descriptor
								.setShortDescription(annotation.description());
						descriptors.add(descriptor);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		PropertyDescriptor[] convertedDescriptors = new PropertyDescriptor[descriptors
				.size()];
		panel.setProperties(descriptors.toArray(convertedDescriptors));
		panel.setMode(PropertySheetPanel.VIEW_AS_CATEGORIES);
		
		JPanel content = new JPanel(new BorderLayout());
		content.add(panel);

		JButton saveButton = new JButton("Save");
		content.add(saveButton, BorderLayout.SOUTH);

		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				HashMap<Class, Object> instances = new HashMap<Class, Object>();
				Class componentClass;
				Object component;

				for (Property property : panel.getProperties()) {
					componentClass = fieldBeanClasses.get(property
							.getCategory() + "_" + property.getName());
					if ((component = instances.get(componentClass)) == null) {
						try {
							component = componentClass.newInstance();
							instances.put(componentClass, component);
						} catch (InstantiationException e1) {
						} catch (IllegalAccessException e1) {
						}
					}

					property.writeToObject(component);

				}
				
				System.out.println(":D");
			}
		});

		add(content);
	}

	@Override
	public void load(BaseEditor baseEditor) {
		// TODO Auto-generated method stub

	}

	@Override
	public String[] validate(BaseEditor baseEditor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(BaseEditor baseEditor) {
		// TODO Auto-generated method stub

	}

}
