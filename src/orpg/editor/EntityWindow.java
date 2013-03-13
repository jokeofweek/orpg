package orpg.editor;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import orpg.editor.controller.EditorController;
import orpg.editor.controller.EntityController;
import orpg.editor.data.EditorUpdateMessage;
import orpg.editor.util.EnumPropertyEditor;
import orpg.editor.util.PropertyDescriptorAdapter;
import orpg.shared.Strings;
import orpg.shared.data.ComponentList;
import orpg.shared.data.Direction;
import orpg.shared.data.annotations.Editable;
import orpg.shared.data.component.AttachableComponentDescriptor;
import orpg.shared.data.component.Named;
import orpg.shared.data.component.Position;
import orpg.shared.data.component.Renderable;
import orpg.shared.data.component.TestComponent;

import com.artemis.Entity;
import com.l2fprod.common.beans.ExtendedPropertyDescriptor;
import com.l2fprod.common.propertysheet.DefaultProperty;
import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertyEditorRegistry;
import com.l2fprod.common.propertysheet.PropertySheetPanel;
import com.sun.jmx.interceptor.DefaultMBeanServerInterceptor;

public class EntityWindow extends JFrame implements
		EditorWindow<ComponentList>, Observer {

	private EntityController controller;

	private final PropertySheetPanel propertySheet;
	private JList availableComponentList;

	public EntityWindow(EntityController controller) {
		this.controller = controller;
		this.controller.addObserver(this);

		// Set up the property sheet
		propertySheet = new PropertySheetPanel();
		((PropertyEditorRegistry) (propertySheet.getEditorFactory()))
				.registerEditor(Direction.class, new EnumPropertyEditor(
						Direction.class));

		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setTitle(Strings.ENGINE_NAME);
		this.setupContent();
		this.pack();
		this.setVisible(true);
		this.setSize(300, 200);
		this.setLocationRelativeTo(null);
		this.requestFocusInWindow();

		this.load(null);
	}

	public void setupContent() {

		propertySheet.setMode(PropertySheetPanel.VIEW_AS_CATEGORIES);

		JPanel content = new JPanel(new BorderLayout());
		content.add(propertySheet);
		propertySheet.addPropertySheetChangeListener(controller);

		// Add any properties already loaded in the controller
		List<Property> properties = controller.getCurrentProperties();
		for (Property property : properties) {
			propertySheet.addProperty(property);
		}

		JButton saveButton = new JButton("Save");
		saveButton.setAction(controller.getSaveAction());
		content.add(saveButton, BorderLayout.SOUTH);

		availableComponentList = new JList(new DefaultListModel());
		populateAvailableComponentList();
		content.add(availableComponentList, BorderLayout.WEST);
		availableComponentList.addMouseListener(new ComponentListMouseListener(
				controller));

		add(content);
	}

	private void populateAvailableComponentList() {
		// Remove all list items
		DefaultListModel model = (DefaultListModel) (availableComponentList
				.getModel());
		model.removeAllElements();

		// Iterate through the descriptors, adding them to the list
		Set<AttachableComponentDescriptor> availableDescriptors = controller
				.getAvailableComponents();
		for (AttachableComponentDescriptor descriptor : availableDescriptors) {
			model.addElement(descriptor);
		}
	}

	@Override
	public void load(BaseEditor baseEditor) {

	}

	@Override
	public List<String> validate(BaseEditor baseEditor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void beforeSave(BaseEditor baseEditor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Observable o, Object arg) {
		populateAvailableComponentList();

		// If it is a list of property descriptors, add them
		if (arg instanceof List<?>) {
			if (((List<?>) arg).size() != 0) {
				// Make sure it's a property list, and if so add them all
				if (((List<?>) arg).get(0) instanceof Property) {
					for (Property property : (List<Property>) arg) {
						propertySheet.addProperty(property);
					}
				}
			}
		} else if (arg instanceof EditorUpdateMessage) {
			if (((EditorUpdateMessage) arg) == EditorUpdateMessage.SAVE) {
				// If it is a save, close the window!
				setVisible(false);
				dispose();
			}
		}
	}

	private static class ComponentListMouseListener implements MouseListener {

		private EntityController controller;

		public ComponentListMouseListener(EntityController controller) {
			this.controller = controller;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				Object selection = ((JList) (e.getSource())).getSelectedValue();
				if (selection != null) {
					controller
							.attachComponent((AttachableComponentDescriptor) selection);
				}
				e.consume();
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

	}
}
