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
		world.setManager(new GroupManager());
		world.setManager(new PlayerManager());
		world.initialize();
		
		Entity e = world.createEntity();
		world.getManager(PlayerManager.class).setPlayer(e, "blab");
		e.deleteFromWorld();
		System.out.println(world.getManager(PlayerManager.class).getEntitiesOfPlayer("blah").size());
		
	}

}
