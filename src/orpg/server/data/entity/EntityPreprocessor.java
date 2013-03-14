package orpg.server.data.entity;

import orpg.shared.data.component.Collidable;

import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.ComponentType;
import com.artemis.Entity;
import com.artemis.EntityManager;
import com.artemis.utils.Bag;

public class EntityPreprocessor extends EntityManager {

	private ComponentType collidableType;

	public EntityPreprocessor() {
		this.collidableType = ComponentType.getTypeFor(Collidable.class);
	}

	@Override
	public void added(Entity e) {
		super.added(e);

		ComponentMapper<Collidable> collidableMapper = world
				.getMapper(Collidable.class);
		boolean hasCollidable = collidableMapper.getSafe(e) != null;

		Bag<Component> components = new Bag<Component>();
		e.getComponents(components);
		// Register collidable component as a generic collidable component
		for (int i = 0; i < components.size(); i++) {
			if (!hasCollidable && components.get(i) instanceof Collidable) {
				e.addComponent(components.get(i), collidableType);
				e.changedInWorld();
				hasCollidable = true;
			}
		}
	}

}
