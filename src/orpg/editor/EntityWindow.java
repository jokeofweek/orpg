package orpg.editor;

import javax.swing.JFrame;
import javax.swing.JList;

import orpg.editor.controller.EntityController;
import orpg.shared.Strings;
import orpg.shared.data.ComponentList;
import orpg.shared.data.component.EditableComponentManager;

import com.artemis.Entity;

public class EntityWindow extends JFrame implements
		EditorWindow<ComponentList> {

	private EntityController controller;

	private JList availableComponents;

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
		JList availableComponents = new JList(EditableComponentManager
				.getInstance().getDescriptors().toArray());
		this.add(availableComponents);
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
