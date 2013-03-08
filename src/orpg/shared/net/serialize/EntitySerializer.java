package orpg.shared.net.serialize;

import java.util.HashMap;

import orpg.shared.data.component.SynchronizeableComponent;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.TagManager;
import com.artemis.utils.Bag;

public class EntitySerializer extends Object implements
		ValueSerializer<Entity> {

	private static HashMap<World, EntitySerializer> instances = new HashMap<World, EntitySerializer>(
			1);

	public static EntitySerializer getInstance(World world) {
		EntitySerializer serializer = instances.get(world);
		if (serializer == null) {
			serializer = new EntitySerializer(world);
			instances.put(world, serializer);
		}
		return serializer;
	}

	private World world;

	private EntitySerializer(World world) {
		this.world = world;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * orpg.shared.net.serialize.ValueSerializer#put(orpg.shared.net.serialize
	 * .OutputByteBuffer, java.lang.Object)
	 */
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

		SynchronizeableComponent component;
		for (int i = 0; i < bag.size(); i++) {
			if (bag.get(i) instanceof SynchronizeableComponent) {
				component = (SynchronizeableComponent) bag.get(i);
				out.putValue(component);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * orpg.shared.net.serialize.ValueSerializer#get(orpg.shared.net.serialize
	 * .InputByteBuffer)
	 */
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
