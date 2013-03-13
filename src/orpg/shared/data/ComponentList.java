package orpg.shared.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import orpg.shared.data.component.Position;
import orpg.shared.data.component.SerializableComponent;
import orpg.shared.net.serialize.InputByteBuffer;
import orpg.shared.net.serialize.OutputByteBuffer;
import orpg.shared.net.serialize.SerializableValue;
import orpg.shared.net.serialize.ValueSerializer;

import com.artemis.Component;

public class ComponentList implements SerializableValue<ComponentList> {

	private final List<Component> components;
	private Position position;

	public ComponentList(List<Component> components) {
		this.components = components;

		// Find the position
		for (Component component : components) {
			if (component instanceof Position) {
				this.position = (Position) component;
			}
		}
	}

	public List<Component> getComponents() {
		return this.components;
	}

	public Position getPosition() {
		return position;
	}

	@Override
	public ValueSerializer<ComponentList> getSerializer() {
		return Serializer.getInstance();
	}

	public static class Serializer implements ValueSerializer<ComponentList> {

		private static Serializer instance = new Serializer();

		public static Serializer getInstance() {
			return instance;
		}

		private Serializer() {
		}

		@Override
		public void put(OutputByteBuffer out, ComponentList obj) {
			short count = 0;
			List<Component> components = obj.components;

			for (Component component : components) {
				if (component instanceof SerializableValue) {
					count++;
				}
			}

			out.putShort(count);

			for (Component component : components) {
				if (component instanceof SerializableValue) {
					out.putValue((SerializableValue) component);
				}
			}
		}

		@Override
		public ComponentList get(InputByteBuffer in) {
			short count = in.getShort();
			List<Component> components = new ArrayList<Component>(count);

			for (int i = 0; i < count; i++) {
				components.add(in.getValue(SerializableComponent.Serializer
						.getInstance()));
			}

			ComponentList list = new ComponentList(components);
			return list;
		}

	}

}
