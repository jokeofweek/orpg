package orpg.shared.net.serialize;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.artemis.Entity;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;

import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;

import orpg.shared.Constants;
import orpg.shared.data.AccountCharacter;
import orpg.shared.data.AutoTileType;
import orpg.shared.data.ComponentList;
import orpg.shared.data.Map;
import orpg.shared.data.MapLayer;
import orpg.shared.data.Segment;

public class OutputByteBuffer {

	private byte[] bytes;
	private int pos;
	private int compressionMarker;

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
		this.reset();
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
	 * This clears the contents of the output byte buffer and sets the position
	 * back to 0. It also resets the compression marker.
	 */
	public void reset() {
		this.pos = 0;
		this.compressionMarker = 0;
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
			this.bytes = Arrays.copyOfRange(this.bytes, 0, this.bytes.length
					+ (extraCapacity << 1));
		}
	}

	public void reserveExtraSpace(int extraSpace) {
		this.testForExtraCapacity(extraSpace);
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

	public void putInt(int data) {
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
		if (data == null) {
			putShort((short) 0);
			return;
		}

		byte[] stringBytes = data.getBytes(Constants.CHARSET);
		putInt(stringBytes.length);
		testForExtraCapacity(stringBytes.length);
		for (int i = 0; i < stringBytes.length; i++) {
			bytes[pos + i] = stringBytes[i];
		}
		pos += stringBytes.length;
	}

	public void putCharArray(char[] data) {
		putInt(data.length);
		testForExtraCapacity(data.length * 2);
		for (int i = 0; i < data.length; i++) {
			bytes[pos] = (byte) ((data[i] >> 8) & 0xff);
			bytes[pos + 1] = (byte) (data[i] & 0xff);
			pos += 2;
		}
	}

	public void putSegment(Segment segment) {
		short height = segment.getHeight();
		short width = segment.getWidth();

		putShort(width);
		putShort(height);

		putShort(segment.getX());
		putShort(segment.getY());

		// Revision data
		putInt(segment.getRevision());
		putLong(segment.getRevisionTime());

		// test for extra capacity right away to pre-allocate
		short[][][] tiles = segment.getTiles();
		byte[][] flags = segment.getFlags();
		testForExtraCapacity(MapLayer.values().length * segment.getHeight()
				* segment.getWidth() * 3);

		int z, y, x;
		for (x = 0; x < width; x++) {
			for (y = 0; y < height; y++) {
				putByte(flags[x][y]);
				for (z = 0; z < MapLayer.values().length; z++) {
					putShort(tiles[z][x][y]);
				}
			}
		}

		// store entities
		List<ComponentList> entities = segment.getEntities();
		testForExtraCapacity(2 + entities.size());
		putShort((short) entities.size());
		for (ComponentList entity : entities) {
			putValue(entity);
		}
	}

	/**
	 * This puts a bag of entities into the byte buffer. <b>Note this assumes
	 * all entities are part of the same world.</b>
	 * 
	 * @param entities
	 */
	public void putEntities(ImmutableBag<Entity> entities) {
		putInt(entities.size());

		EntitySerializer serializer = null;
		if (entities.size() != 0) {
			serializer = EntitySerializer.getInstance(entities.get(0)
					.getWorld());
		}

		for (int i = 0; i < entities.size(); i++) {
			putValue(entities.get(i), serializer);
		}
	}

	public void putMapDescriptor(Map map) {
		putInt(map.getId());
		putShort(map.getSegmentWidth());
		putShort(map.getSegmentHeight());
		putShort(map.getSegmentsWide());
		putShort(map.getSegmentsHigh());
		putString(map.getName());
	}

	public void putMap(Map map, boolean storePlayers) {
		putMapDescriptor(map);
		reserveExtraSpace(map.getSegmentsWide() * map.getSegmentsHigh()
				* map.getSegmentHeight() * map.getSegmentWidth() * 2);

		// Count non-null segments
		int count = 0;
		for (short x = 0; x < map.getSegmentsWide(); x++) {
			for (short y = 0; y < map.getSegmentsHigh(); y++) {
				if (map.getSegment(x, y) != null) {
					count++;
				}
			}
		}

		putInt(count);

		for (short x = 0; x < map.getSegmentsWide(); x++) {
			for (short y = 0; y < map.getSegmentsHigh(); y++) {
				if (map.getSegment(x, y) != null) {
					putSegment(map.getSegment(x, y));
				}
			}
		}
	}

	public void putAccountCharacter(AccountCharacter character) {
		testForExtraCapacity(20);
		putInt(character.getId());
		putString(character.getName());
		putShort(character.getSprite());
		putInt(character.getMap().getId());
		putInt(character.getX());
		putInt(character.getY());
		putByte((byte) character.getDirection().ordinal());
	}

	public void putMapCharacter(AccountCharacter character) {
		testForExtraCapacity(20);
		putInt(character.getId());
		putString(character.getName());
		putShort(character.getSprite());
		putInt(character.getX());
		putInt(character.getY());
		putByte((byte) character.getDirection().ordinal());
	}

	public void putAutoTiles(java.util.Map<Short, AutoTileType> autoTiles) {
		testForExtraCapacity(2 + (3 * autoTiles.size()));
		putShort((short) autoTiles.size());
		for (java.util.Map.Entry<Short, AutoTileType> entry : autoTiles
				.entrySet()) {
			putShort(entry.getKey());
			putByte((byte) entry.getValue().ordinal());
		}
	}

	public <K extends SerializableValue> void putValue(K value) {
		value.getSerializer().put(this, value);
	}

	public <K> void putValue(K value, ValueSerializer<K> serializer) {
		serializer.put(this, value);
	}

	/**
	 * This notifies the output byte buffer to set the compression marker to the
	 * current state of the output byte buffer. A subsequent call to
	 * {@link #compress()} will compress all data added from this point on.
	 */
	public void setCompressionMarker() {
		this.compressionMarker = this.pos;
	}

	/**
	 * This compresses the data in the current stream, starting at the
	 * compression marker up to the last object inserted.
	 */
	public void compress() {
		// Always put the length of the decompressed input as an int
		// followed by the compressed bytes
		LZ4Factory factory = LZ4Factory.fastestInstance();
		LZ4Compressor compressor = factory.fastCompressor();

		int decompressedLength = this.pos - this.compressionMarker;
		int maxRequiredLength = compressor
				.maxCompressedLength(decompressedLength);
		byte[] originalBytes = this.bytes;

		// Copy all bytes over into our new bytes
		this.bytes = new byte[this.pos - decompressedLength + maxRequiredLength
				+ 8];
		System.arraycopy(originalBytes, 0, this.bytes, 0,
				this.compressionMarker);
		this.pos = this.compressionMarker;

		// Put the data.
		this.putInt(decompressedLength);
		int compressedLength = compressor.compress(originalBytes,
				this.compressionMarker, decompressedLength, this.bytes,
				this.pos + 4);
		this.putInt(compressedLength);
		this.pos += compressedLength;

		// Set the compression marker to after our current pos
		this.compressionMarker = this.pos;
	}

}
