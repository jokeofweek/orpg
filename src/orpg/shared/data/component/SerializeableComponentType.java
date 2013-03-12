package orpg.shared.data.component;

import com.sun.corba.se.spi.ior.Writeable;

import orpg.shared.net.serialize.ValueSerializer;

public enum SerializeableComponentType {

	POSITION(Position.Serializer.getInstance()), RENDERABLE(
			Renderable.Serializer.getInstance()), NAMED(Named.Serializer
			.getInstance()), IS_PLAYER(IsPlayer.Serializer.getInstance()), MOVEABLE(
			Moveable.Serializer.getInstance()), BASIC_COLLIDABLE(
			BasicCollidable.Serializer.getInstance()), MESSAGE_COLLIDABLE(
			MessageCollidable.Serializer.getInstance());

	private ValueSerializer<SerializableComponent> serializer;

	private SerializeableComponentType(
			ValueSerializer<SerializableComponent> serializer) {
		this.serializer = serializer;
	}

	public ValueSerializer<SerializableComponent> getSerializer() {
		return serializer;
	}

}
