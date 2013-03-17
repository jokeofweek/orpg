package orpg.shared.data;

import com.badlogic.gdx.utils.Json.Serializer;

import orpg.shared.net.serialize.InputByteBuffer;
import orpg.shared.net.serialize.OutputByteBuffer;
import orpg.shared.net.serialize.SerializableValue;
import orpg.shared.net.serialize.ValueSerializer;

public class Message implements SerializableValue<Message> {

	private String emitter;
	private String contents;
	private ChatChannel channel;

	public Message(String emitter, String contents, ChatChannel channel) {
		super();
		this.emitter = emitter;
		this.contents = contents;
		this.channel = channel;
	}

	public String getEmitter() {
		return emitter;
	}

	public String getContents() {
		return contents;
	}

	public ChatChannel getChannel() {
		return channel;
	}

	@Override
	public ValueSerializer<Message> getSerializer() {
		return Serializer.getInstance();
	}

	public static class Serializer implements ValueSerializer<Message> {

		private static Serializer instance = new Serializer();

		public static Serializer getInstance() {
			return instance;
		}

		private Serializer() {
		}

		@Override
		public void put(OutputByteBuffer out, Message obj) {
			out.putString(obj.getEmitter());
			out.putString(obj.getContents());
			out.putByte((byte) obj.getChannel().ordinal());
		}

		@Override
		public Message get(InputByteBuffer in) {
			return new Message(in.getString(), in.getString(),
					ChatChannel.values()[in.getByte()]);
		}

	}

}
