package orpg.shared.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.IOException;
import java.util.Arrays;

import orpg.shared.Constants;
import orpg.shared.data.Map;
import orpg.shared.data.MapLayer;
import orpg.shared.data.Segment;

public class InputByteBuffer {

	private byte[] bytes;
	private int pos;

	public InputByteBuffer(byte[] bytes) {
		this.bytes = bytes;
		this.reset();
	}

	/**
	 * This reads a file to completion and then wraps it in an input byte
	 * buffer.
	 * 
	 * @throws IOException
	 * 
	 * @pram file the file to read.
	 */
	public InputByteBuffer(File file) throws IOException {
		this.bytes = new byte[(int) file.length()];
		int offset = 0;
		int read = 0;

		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			while (offset < this.bytes.length) {
				read = in.read(this.bytes, offset, this.bytes.length - offset);
				if (read == -1) {
					throw new IOException(
							"Could could not read any bytes. End of stream.");
				}
				offset += read;
			}
		} finally {
			if (in != null) {
				in.close();
			}
		}

		this.pos = 0;

	}

	/**
	 * This resets the internal pointer, allowing you to go through the byte
	 * buffer again.
	 */
	public void reset() {
		this.pos = 0;
	}

	public boolean atEnd() {
		return this.pos == this.bytes.length;
	}

	private void hasEnoughCapacity(int capacity) {
		if (this.bytes.length < pos + capacity) {
			throw new ByteBufferOutOfBoundsException(this.bytes.length,
					this.pos + capacity);
		}
	}

	public byte getByte() {
		hasEnoughCapacity(1);
		pos++;
		return bytes[this.pos - 1];
	}

	public short getShort() {
		hasEnoughCapacity(2);
		short v = bytes[this.pos];
		v <<= 8;
		v |= (bytes[this.pos + 1] & 0xff);
		pos += 2;
		return v;
	}

	public int getUnsignedShort() {
		hasEnoughCapacity(2);
		int v = bytes[this.pos];
		v <<= 8;
		v |= (bytes[this.pos + 1] & 0xff);
		v &= 0x0ffff;
		pos += 2;
		return v;
	}

	public int getInt() {
		hasEnoughCapacity(4);
		int v = bytes[this.pos];
		v <<= 8;
		v |= (bytes[this.pos + 1] & 0xff);
		v <<= 8;
		v |= (bytes[this.pos + 2] & 0xff);
		v <<= 8;
		v |= (bytes[this.pos + 3] & 0xff);
		pos += 4;
		return v;
	}

	public long getLong() {
		hasEnoughCapacity(8);
		long v = bytes[this.pos];
		v <<= 8;
		v |= (bytes[this.pos + 1] & 0xff);
		v <<= 8;
		v |= (bytes[this.pos + 2] & 0xff);
		v <<= 8;
		v |= (bytes[this.pos + 3] & 0xff);
		v <<= 8;
		v |= (bytes[this.pos + 4] & 0xff);
		v <<= 8;
		v |= (bytes[this.pos + 5] & 0xff);
		v <<= 8;
		v |= (bytes[this.pos + 6] & 0xff);
		v <<= 8;
		v |= (bytes[this.pos + 7] & 0xff);
		pos += 8;
		return v;
	}

	public boolean getBoolean() {
		hasEnoughCapacity(1);
		pos++;
		return bytes[pos - 1] == 1;
	}

	public Segment getSegment() {
		// Test for dimensions first
		short width = getShort();
		short height = getShort();

		// Get positions
		int segmentX = getInt();
		int segmentY = getInt();

		// Ensure enough capacity for tiles
		hasEnoughCapacity(width * height * MapLayer.values().length * 2);
		short[][][] tiles = new short[MapLayer.values().length][height][width];
		int x, y, z;
		for (z = 0; z < MapLayer.values().length; z++) {
			for (x = 0; x < width; x++) {
				for (y = 0; y < height; y++) {
					tiles[z][x][y] = getShort();
				}
			}
		}

		return new Segment(segmentX, segmentY, width, height, tiles);
	}

	public Map getMapDescriptor() {
		int id = getInt();
		short segmentWidth = getShort();
		short segmentHeight = getShort();
		short segmentsWide = getShort();
		short segmentsHigh = getShort();
		Map map = new Map(id, segmentWidth, segmentHeight, segmentsWide,
				segmentsHigh, false);
		
		map.setName(getString());
		return map;
	}

	public Map getMap() {
		Map map = getMapDescriptor();
		for (int x = 0; x < map.getSegmentsWide(); x++) {
			for (int y = 0; y < map.getSegmentsHigh(); y++) {
				map.updateSegment(getSegment());
			}
		}
		return map;
	}

	public String getString() {
		short length = getShort();
		hasEnoughCapacity(length);
		String v = new String(Arrays.copyOfRange(bytes, this.pos, this.pos
				+ length), Constants.CHARSET);
		pos += length;
		return v;
	}

	public class ByteBufferOutOfBoundsException extends RuntimeException {

		private final long serialVersionUID = 4114630363307944017L;

		public ByteBufferOutOfBoundsException(int length, int index) {
			super(
					String.format(
							"Attempted to read out of byte buffer bounds.\nCurrent length: %d\nAttempted read at: %d.",
							length, index));
		}
	}
}
