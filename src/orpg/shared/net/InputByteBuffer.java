package orpg.shared.net;

import java.util.Arrays;

import orpg.shared.Constants;

public class InputByteBuffer {

	private byte[] bytes;
	private int pos;

	public InputByteBuffer(byte[] bytes) {
		this.bytes = bytes;
		this.reset();
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

	public int getInteger() {
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

	public String getString() {
		short length = getShort();
		hasEnoughCapacity(length);
		String v = new String(Arrays.copyOfRange(bytes, this.pos,
				this.pos + length), Constants.CHARSET);
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
