package orpg.client.net.handlers;

import orpg.client.BaseClient;
import orpg.client.data.ClientReceivedPacket;
import orpg.shared.data.Message;
import orpg.shared.net.serialize.ValueSerializer;

public class MessageHandler implements ClientPacketHandler {

	private static ValueSerializer<Message> deserializer = Message.Serializer
			.getInstance();

	@Override
	public void handle(ClientReceivedPacket packet, BaseClient client) {
		client.getChatController().addMessage(
				packet.getByteBuffer().getValue(deserializer));
	}

}
