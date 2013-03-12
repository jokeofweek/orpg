package orpg.shared.data.component;

import orpg.shared.net.serialize.InputByteBuffer;
import orpg.shared.net.serialize.OutputByteBuffer;
import orpg.shared.net.serialize.ValueSerializer;

/**
 * This component acts as a flag denoting the entity as a player.
 * 
 * @author Dominic Charley-Roy
 */
public class IsPlayer extends SynchronizebleComponent {

	private static IsPlayer instance = new IsPlayer();

	public static IsPlayer getInstance() {
		return instance;
	}

	private IsPlayer() {
	}

	@Override
	public SerializeableComponentType getType() {
		return SerializeableComponentType.IS_PLAYER;
	}

	public static class Serializer implements
			ValueSerializer<SerializableComponent> {

		private static Serializer instance = new Serializer();

		public static Serializer getInstance() {
			return instance;
		}

		private Serializer() {
		}

		@Override
		public void put(OutputByteBuffer out, SerializableComponent obj) {
		}

		@Override
		public IsPlayer get(InputByteBuffer in) {
			return IsPlayer.getInstance();
		}
	}
}
