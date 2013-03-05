import orpg.shared.component.Position;
import orpg.shared.component.Renderable;
import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		World world = new World();
		Entity e = world.createEntity();
		e.addComponent(new Position());
		e.addComponent(new Renderable());
		Bag<Component> bag = new Bag<Component>();
		e.getComponents(bag);
		
	}

}
