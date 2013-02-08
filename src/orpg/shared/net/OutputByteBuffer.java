package orpg.shared.net;

import java.util.Arrays;

import orpg.shared.Constants;
import orpg.shared.data.MapLayer;
import orpg.shared.data.MapSaveData;
import orpg.shared.data.Segment;

public class OutputByteBuffer {

	private byte[] bytes;
	private int pos;

	public OutputByteBuffer() {
		this(24);
	}

	/**
	 * This creates an output byte buffer with a number of bytes pre-allocated.
	 * 
	 * @param capacity
	 *            the number of bytes to pre-allocate
	 */
	public OutputByteBuffer(int capacity) {
		this.bytes = new byte[capacity];
	}

	/**
	 * This creates an output byte buffer with an estimate number of bytes based
	 * on the objects passed. It pre-loads it with the objects passed.
	 * 
	 * @param objects
	 *            the objects to store in the buffer.
	 */
	public OutputByteBuffer(Object... objects) {
		this(objects.length * 4); // rough guess
		// TODO: Estimate the size of the array more thoroughly.
		for (Object o : objects) {
			if (o instanceof Number) {
				if (o instanceof Byte) {
					putByte((Byte) o);
				} else if (o instanceof Short) {
					putShort((Short) o);
				} else if (o instanceof Integer) {
					putInteger((Integer) o);
				} else if (o instanceof Long) {
					putLong((Long) o);
				} else {
					throw new IllegalArgumentException(
							"Cannot serialize object of type '"
									+ o.getClass() + "'.");
				}
			} else if (o instanceof Boolean) {
				putBoolean((Boolean) o);
			} else if (o instanceof String) {
				putString((String) o);
			} else if (o instanceof MapSaveData) {
				putMapSaveData((MapSaveData)o);
			} else if (o instanceof Segment) {
				putSegment((Segment)o);
			} else {
				throw new IllegalArgumentException(
						"Cannot serialize object of type '" + o.getClass()
								+ "'.");
			}
		}

	}

	public byte[] getBytes() {
		// If our byte buffer is perfectly full, just return that, else
		// trim it.
		if (bytes.length != pos) {
			this.bytes = Arrays.copyOfRange(bytes, 0, pos);
		}
		return bytes;
	}

	/**
	 * This tests whether we have room for a given number of bytes, and if not
	 * grows the internal byte buffer.
	 * 
	 * @param extraCapacity
	 *            the number of bytes we want to add.
	 */
	private void testForExtraCapacity(int extraCapacity) {
		if (this.bytes.length < pos + extraCapacity) {
			// If necessary, expand the byte array. Note we also shift new
			// capacity by 1 to have extra space for later.
			this.bytes = Arrays.copyOfRange(this.bytes, 0,
					this.bytes.length + (extraCapacity << 1));
		}
	}

	public void putByte(byte data) {
		testForExtraCapacity(1);
		this.bytes[pos] = data;
		pos++;
	}

	public void putShort(short data) {
		testForExtraCapacity(2);
		bytes[pos] = (byte) ((data >> 8) & 0xff);
		bytes[pos + 1] = (byte) (data & 0xff);
		pos += 2;
	}

	public void putUnsignedShort(int data) {
		testForExtraCapacity(2);
		putShort((short) data);
	}

	public void putInteger(int data) {
		testForExtraCapacity(4);
		bytes[pos] = (byte) ((data >> 24) & 0xff);
		bytes[pos + 1] = (byte) ((data >> 16) & 0xff);
		bytes[pos + 2] = (byte) ((data >> 8) & 0xff);
		bytes[pos + 3] = (byte) (data & 0xff);
		pos += 4;
	}

	public void putLong(long data) {
		testForExtraCapacity(8);
		bytes[pos] = (byte) ((data >> 56) & 0xff);
		bytes[pos + 1] = (byte) ((data >> 48) & 0xff);
		bytes[pos + 2] = (byte) ((data >> 40) & 0xff);
		bytes[pos + 3] = (byte) ((data >> 32) & 0xff);
		bytes[pos + 4] = (byte) ((data >> 24) & 0xff);
		bytes[pos + 5] = (byte) ((data >> 16) & 0xff);
		bytes[pos + 6] = (byte) ((data >> 8) & 0xff);
		bytes[pos + 7] = (byte) (data & 0xff);
		pos += 8;
	}

	public void putBoolean(boolean data) {
		testForExtraCapacity(1);
		bytes[pos] = (byte) (data ? 1 : 0);
		pos++;
	}

	public void putString(String data) {
		byte[] stringBytes = data.getBytes(Constants.CHARSET);
		putShort((short) stringBytes.length);
		testForExtraCapacity(stringBytes.length);
		for (int i = 0; i < stringBytes.length; i++) {
			bytes[pos + i] = stringBytes[i];
		}
		pos += stringBytes.length;
	}

	public void putSegment(Segment segment) {
		short height = segment.getHeight();
		short width = segment.getWidth();

		putShort(width);
		putShort(height);

		putInteger(segment.getX());
		putInteger(segment.getY());

		// test for extra capacity right away to pre-allocate
		short[][][] tiles = segment.getTiles();
		testForExtraCapacity(MapLayer.values().length
				* segment.getHeight() * segment.getWidth() * 2);

		int z, y, x;
		for (z = 0; z < MapLayer.values().length; z++) {
			for (y = 0; y < height; y++) {
				for (x = 0; x < width; x++) {
					putShort(tiles[z][y][x]);
				}
			}
		}
	}
	
	public void putMapSaveData(MapSaveData mapSaveData) {
		putInteger(mapSaveData.getSegments().length);
		
		for (Segment segment : mapSaveData.getSegments()) {
			putSegment(segment);
		}
	}

}
