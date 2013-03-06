package orpg.shared.net.serialize;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;

import net.jpountz.lz4.LZ4Decompressor;
import net.jpountz.lz4.LZ4Factory;

import orpg.shared.Constants;
import orpg.shared.data.AccountCharacter;
import orpg.shared.data.AutoTileType;
import orpg.shared.data.Direction;
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
				read = in.read(this.bytes, offset, this.bytes.length
						- offset);
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
		short segmentX = getShort();
		short segmentY = getShort();

		// Revision
		int revision = getInt();
		long revisionTime = getLong();

		// Ensure enough capacity for tiles
		hasEnoughCapacity(width * height * MapLayer.values().length * 2);
		short[][][] tiles = new short[MapLayer.values().length][width][height];
		byte[][] flags = new byte[width][height];
		int x, y, z;
		for (x = 0; x < width; x++) {
			for (y = 0; y < height; y++) {
				flags[x][y] = getByte();
				for (z = 0; z < MapLayer.values().length; z++) {
					tiles[z][x][y] = getShort();
				}
			}
		}

		Segment segment = new Segment(segmentX, segmentY, width, height,
				tiles, flags, revision, revisionTime);

		return segment;
	}

	public List<AccountCharacter> getSegmentPlayers() {
		int count = getInt();
		List<AccountCharacter> characters = new ArrayList<AccountCharacter>(
				count);
		for (int i = 0; i < count; i++) {
			characters.add(getMapCharacter());
		}
		return characters;
	}

	public Bag<Entity> getEntities(World world) {
		int count = getInt();
		Bag<Entity> entities = new Bag<Entity>(count);

		for (int i = 0; i < count; i++) {
			entities.add(getValue(EntitySerializer.getInstance(world)));
		}

		return entities;
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
		int count = getInt();

		for (int i = 0; i < count; i++) {
			map.updateSegment(getSegment(), false);
		}
		return map;
	}

	public String getString() {
		int length = getInt();
		hasEnoughCapacity(length);
		String v = new String(Arrays.copyOfRange(bytes, this.pos, this.pos
				+ length), Constants.CHARSET);
		pos += length;
		return v;
	}

	public char[] getCharArray() {
		int length = getInt();
		hasEnoughCapacity(length);
		char[] chars = new char[length];
		for (int i = 0; i < length; i++) {
			chars[i] = (char) (((bytes[pos] & 0xff) << 8) | (bytes[pos + 1] & 0xff));
			pos += 2;
		}

		return chars;
	}

	public AccountCharacter getAccountCharacter() {
		AccountCharacter character = new AccountCharacter();
		character.setId(getInt());
		character.setName(getString());
		character.setSprite(getShort());
		getInt(); // Must drop an int for the map ID
		character.setX(getInt());
		character.setY(getInt());
		character.setDirection(Direction.values()[getByte()]);

		return character;
	}

	public AccountCharacter getMapCharacter() {
		AccountCharacter character = new AccountCharacter();
		character.setId(getInt());
		character.setName(getString());
		character.setSprite(getShort());
		character.setX(getInt());
		character.setY(getInt());
		character.setDirection(Direction.values()[getByte()]);

		return character;
	}

	public java.util.Map<Short, AutoTileType> getAutoTiles() {
		short count = getShort();
		HashMap<Short, AutoTileType> autoTiles = new HashMap<Short, AutoTileType>(
				count);

		for (short i = 0; i < count; i++) {
			autoTiles.put(getShort(), AutoTileType.values()[getByte()]);
		}

		return autoTiles;
	}

	public <K> K getValue(ValueSerializer<K> valueReader) {
		return valueReader.get(this);
	}

	/**
	 * This decompresses a data block in the current stream.
	 */
	public void decompress() {
		// Get uncompressed and compressed length
		int decompressedLength = getInt();
		int compressedLength = getInt();

		// Decompress the data
		LZ4Factory factory = LZ4Factory.fastestInstance();
		LZ4Decompressor decompressor = factory.decompressor();

		// Create the new byte array
		byte[] newBytes = new byte[this.bytes.length - compressedLength
				+ decompressedLength];

		// Copy all bytes before and after the compressed data to this new
		// array. This permits us to have only partially compressed data in a
		// buffer.
		System.arraycopy(this.bytes, 0, newBytes, 0, this.pos);
		System.arraycopy(this.bytes, this.pos + compressedLength,
				newBytes, this.pos + decompressedLength, this.bytes.length
						- (this.pos + compressedLength));

		// Now inject the decompressed bytes
		decompressor.decompress(this.bytes, this.pos, newBytes, this.pos,
				decompressedLength);
		this.bytes = newBytes;

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
