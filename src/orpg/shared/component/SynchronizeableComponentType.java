package orpg.shared.component;

import orpg.shared.net.serialize.ValueSerializer;

public enum SynchronizeableComponentType {

	POSITION(Position.Serializer.getInstance()), RENDERABLE(
			Renderable.Serializer.getInstance()), NAMED(Named.Serializer
			.getInstance()), IS_PLAYER(IsPlayer.Serializer.getInstance()), DIRECTIONED(Directioned.Serializer.getInstance());

	private ValueSerializer<SynchronizeableComponent> serializer;

	private SynchronizeableComponentType(
			ValueSerializer<SynchronizeableComponent> serializer) {
		this.serializer = serializer;
	}

	public ValueSerializer<SynchronizeableComponent> getSerializer() {
		return serializer;
	}

}
