package orpg.editor.net.packets;

import java.util.LinkedList;

import orpg.client.net.packets.ClientPacket;
import orpg.shared.data.Map;
import orpg.shared.data.Segment;
import orpg.shared.net.ClientPacketType;
import orpg.shared.net.OutputByteBuffer;

public class EditorSaveMapPacket extends ClientPacket {

	private byte[] bytes;

	public EditorSaveMapPacket(Map map, boolean[][] segmentsChanged) {
		OutputByteBuffer out = new OutputByteBuffer();
		out.putMapDescriptor(map);

		// Fetch the segments that've changed
		LinkedList<Segment> segments = new LinkedList<Segment>();
		for (int x = 0; x < segmentsChanged.length; x++) {
			for (int y = 0; y < segmentsChanged[0].length; y++) {
				if (segmentsChanged[x][y]) {
					segments.add(map.getSegment(x, y));
				}
			}
		}

		// Put number of segments and then the individual segments.
		System.out.println("Saving " + segments.size());
		out.putShort((short) segments.size());
		for (Segment segment : segments) {
			out.putSegment(segment);
		}

		out.compress();
		
		this.bytes = out.getBytes();
	}

	@Override
	public ClientPacketType getPacketType() {
		return ClientPacketType.EDITOR_SAVE_MAP;
	}

	@Override
	public byte[] getBytes() {
		return this.bytes;
	}

}
