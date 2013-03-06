package orpg.client.net.handlers;

import com.badlogic.gdx.Gdx;

import orpg.client.BaseClient;
import orpg.client.data.ClientReceivedPacket;
import orpg.shared.data.Map;
import orpg.shared.net.serialize.InputByteBuffer;

public class NewMapHandler implements ClientPacketHandler {

	@Override
	public void handle(ClientReceivedPacket packet, final BaseClient client) {
		client.setChangingMap(true);

		InputByteBuffer in = packet.getByteBuffer();

		Map map = in.getMapDescriptor();
		client.setMap(map);
		client.getAccountCharacter().setMap(map);

		in.reset();
		client.setLocalMap(in.getMapDescriptor());

		// Determine the segment
		client.getAccountCharacter().setX(in.getInt());
		short segmentX = map.getSegmentX(client.getAccountCharacter()
				.getX());

		client.getAccountCharacter().setY(in.getInt());
		short segmentY = map.getSegmentY(client.getAccountCharacter()
				.getY());

		// Clear the client data cache
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				client.setChangingMap(true);
				client.resetWorld();
			}
		});

		// Request the segment
		client.getSegmentRequestManager().requestSegment(segmentX,
				segmentY);

	}

}
