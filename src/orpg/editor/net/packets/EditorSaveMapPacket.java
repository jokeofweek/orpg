package orpg.editor.net.packets;

import orpg.client.net.packets.ClientPacket;
import orpg.shared.data.Map;
import orpg.shared.net.ClientPacketType;
import orpg.shared.net.OutputByteBuffer;

public class EditorSaveMapPacket extends ClientPacket {

	private byte[] bytes;

	public EditorSaveMapPacket(Map map, boolean[][] segmentsChanged) {
		OutputByteBuffer out = new OutputByteBuffer();
		out.putMap(map);
		this.bytes = out.getBytes();

		for (int y = 0; y < segmentsChanged[0].length; y++) {
			for (int x = 0; x < segmentsChanged.length; x++) {
				System.out.print(segmentsChanged[x][y] ? "1" : "0");
			}
			System.out.println();
		}
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
