import orpg.client.data.component.Animated;
import orpg.client.data.component.AnimatedPlayer;
import orpg.shared.data.component.Position;

import com.artemis.ComponentType;
import com.artemis.Entity;
import com.artemis.World;

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
		e.addComponent((Animated)new AnimatedPlayer(), ComponentType.getTypeFor(Animated.class));
		world.changedEntity(e);
		world.process();

	}

}
