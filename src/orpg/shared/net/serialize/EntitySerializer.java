package orpg.shared.net.serialize;

import orpg.shared.component.SynchronizeableComponent;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.TagManager;
import com.artemis.utils.Bag;

public class EntitySerializer extends Object implements
		ValueSerializer<Entity> {

	private World world;

	public EntitySerializer(World world) {
		this.world = world;
	}

	@Override
	public void put(OutputByteBuffer out, Entity obj) {
		out.putInt(obj.getId());
		Bag<Component> bag = new Bag<Component>(20);
		obj.getComponents(bag);

		// Get count of syncable components
		int count = 0;
		for (int i = 0; i < bag.size(); i++) {
			if (bag.get(i) instanceof SynchronizeableComponent) {
				count++;
			}
		}

		out.putInt(count);

		for (int i = 0; i < bag.size(); i++) {
			if (bag.get(i) instanceof SynchronizeableComponent) {
				out.putValue((SynchronizeableComponent) bag.get(i));
			}
		}
	}

	@Override
	public Entity get(InputByteBuffer in) {
		Entity entity = world.createEntity();
		world.getManager(TagManager.class).register(in.getInt() + "",
				entity);

		int count = in.getInt();
		for (int i = 0; i < count; i++) {
			entity.addComponent(in
					.getValue(SynchronizeableComponentSerializer
							.getInstance()));
		}

		return entity;
	}

}
