package orpg.shared.data.component;

import com.sun.corba.se.spi.ior.Writeable;

import orpg.shared.net.serialize.ValueSerializer;

public enum SerializeableComponentType {

	POSITION(Position.Serializer.getInstance()), RENDERABLE(
			Renderable.Serializer.getInstance()), NAMED(Named.Serializer
			.getInstance()), IS_PLAYER(IsPlayer.Serializer.getInstance()), MOVEABLE(
			Moveable.Serializer.getInstance()), COLLIDEABLE(
			Collideable.Serializer.getInstance());

	private ValueSerializer<SerializeableComponent> serializer;

	private SerializeableComponentType(
			ValueSerializer<SerializeableComponent> serializer) {
		this.serializer = serializer;
	}

	public ValueSerializer<SerializeableComponent> getSerializer() {
		return serializer;
	}

}
