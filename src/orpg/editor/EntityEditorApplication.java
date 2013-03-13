package orpg.editor;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import com.artemis.Component;

import orpg.editor.controller.EntityController;
import orpg.shared.data.ComponentList;
import orpg.shared.data.component.Named;
import orpg.shared.data.component.Position;
import orpg.shared.data.component.Renderable;

public class EntityEditorApplication {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				List<Component> components = new ArrayList<Component>();
				components.add(new Renderable((short) 5));
				components.add(new Position(12, 4, 12));
				components.add(new Named("Hello!"));
				ComponentList component = new ComponentList();
				component.setComponents(components);
				EntityController controller = new EntityController(null,
						component);
				new EntityWindow(controller);
			}
		});
	}

}
