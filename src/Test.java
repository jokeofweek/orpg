import orpg.shared.data.component.Position;
import orpg.shared.data.component.Renderable;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.PlayerManager;
import com.artemis.utils.Bag;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		World world = new World();
		world.initialize();

		Entity e = world.createEntity();
		e.addComponent(new Position(5, 5, 5));
		world.addEntity(e);
		world.process();
		e.addComponent(new Position(10, 10, 1000));
		world.changedEntity(e);
		world.process();

		System.out.println(e.getComponent(Position.class).getX());

	}

}
