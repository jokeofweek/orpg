package orpg.shared;

import java.nio.charset.Charset;
import java.util.Arrays;

public class ByteStream {

	public static final String CHARSET_NAME = "UTF-8";
	private static final Charset CHARSET = Charset.forName(CHARSET_NAME);

	/**
	 * This unserializes a byte array which has been serialized with
	 * {@link ByteStream#serialize(byte, Object...)}. Note that it does not
	 * return the type of the packet (stored in the header byte) and thus this
	 * should be extracted manually.
	 * 
	 * @param data
	 *            the serialized byte array.
	 * @param classes
	 *            the classes of the objects in the byte array, stored in the
	 *            same order as they were seralized in.
	 * @return an array of the objects extracted from the byte array. The
	 *         classes of these objects will match the <i>clases</i> parameter
	 *         if the unserialization was successful.
	 * 
	 * @throws ArrayIndexOutOfBoundsException
	 *             if the byte array is not formatted correctly. This usually
	 *             means there wa an error in formatting the packet, the classes
	 *             were sent wrong, or the byte array was tampered with.
	 * @throws IllegalArgumentException
	 *             if the number of classes passed mismatches the number of
	 *             objects in the byte array
	 * @throws SerializationException
	 *             if a class passed was not a serializable type.
	 * 
	 */
	public static Object[] unserialize(byte[] data, Class... classes)
			throws ArrayIndexOutOfBoundsException {
		int offset = 0;

		// Get first byte for number of objects
		Object[] objects = new Object[readByte(data, offset)];
		offset++;

		// If we have a mismatch between object count and class count.
		if (objects.length != classes.length) {
			throw new IllegalArgumentException(
					"Class count does not match object count.");
		}

		for (int i = 0; i < objects.length; i++) {
			// Deserialize based on type
			if (classes[i] == Byte.class) {
				objects[i] = readByte(data, offset);
			} else if (classes[i] == Short.class) {
				objects[i] = readShort(data, offset);
			} else if (classes[i] == Integer.class) {
				objects[i] = readInteger(data, offset);
			} else if (classes[i] == Long.class) {
				objects[i] = readLong(data, offset);
			} else if (classes[i] == Boolean.class) {
				objects[i] = readBoolean(data, offset);
			} else if (classes[i] == String.class) {
				objects[i] = readString(data, offset);
			} else {
				throw new SerializationException("Invalid object type '"
						+ classes[i] + "'. Cannot be deserialized.");
			}

			// Add to the offset
			offset += getByteLength(objects[i]);
		}

		return objects;
	}

	/**
	 * This serializes a header byte and a set of objects into a byte array
	 * which can then be transmit and unserialize via
	 * {@link ByteStream#unserialize(byte[], Class...)}.
	 * 
	 * @param header
	 *            The header byte to prepend the byte array with (usually the
	 *            packet type).
	 * @param objects
	 *            The objects to serialize
	 * @return The byte array containing all the objects in serialized form.
	 * @throws SerializationException
	 *             if an object was not a serializable type.
	 */
	public static byte[] serialize(byte header, Object... objects) {
		int length = 1; // 1 for object count.
		for (Object o : objects) {
			length += getByteLength(o);
		}

		byte[] data = new byte[length + 3];

		// First put the header byte
		int offset = 0;
		offset = writeByte(data, offset, header);
		offset = writeShort(data, offset, (short) (length));
		offset = writeByte(data, offset, (byte) objects.length);

		// Then put the length of the data
		for (Object o : objects) {
			// Serialize based on type
			if (o instanceof Number) {
				if (o instanceof Byte) {
					offset = writeByte(data, offset, (Byte) o);
				} else if (o instanceof Short) {
					offset = writeShort(data, offset, (Short) o);
				} else if (o instanceof Integer) {
					offset = writeInteger(data, offset, (Integer) o);
				} else if (o instanceof Long) {
					offset = writeLong(data, offset, (Long) o);
				} else {
					throw new SerializationException(
							"Invalid object type '" + o.getClass()
									+ "'. Cannot be serialized.");
				}
			} else if (o instanceof Boolean) {
				offset = writeBoolean(data, offset, (Boolean) o);
			} else if (o instanceof String) {
				offset = writeString(data, offset, (String) o);
			} else {
				throw new SerializationException("Invalid object type '"
						+ o.getClass() + "'. Cannot be serialized.");
			}
		}

		return data;
	}

	/**
	 * Calculates the number of bytes required to serialize a given object.
	 * 
	 * @param o
	 *            The object to serialize.
	 * @return The number of bytes that should be reserved if serializing this
	 *         object via {@link ByteStream#serialize(byte, Object...)}.
	 * @throws SerializationException
	 *             if an object was not a serializable type.
	 */
	public static int getByteLength(Object o)
			throws SerializationException {
		if (o instanceof Number) {
			if (o instanceof Byte) {
				return 1;
			} else if (o instanceof Short) {
				return 2;
			} else if (o instanceof Integer) {
				return 4;
			} else if (o instanceof Long) {
				return 8;
			} else {
				throw new SerializationException("Invalid object type '"
						+ o.getClass() + "'. Cannot be serialized.");
			}
		} else if (o instanceof Boolean) {
			return 1;
		} else if (o instanceof String) {
			return 2 + ((String) o).getBytes(CHARSET).length;
		} else {
			throw new SerializationException("Invalid object type '"
					+ o.getClass() + "'. Cannot be serialized.");
		}
	}

	public static int writeByte(byte[] bytes, int offset, byte data) {
		bytes[offset] = data;
		return offset + 1;
	}

	public static int writeShort(byte[] bytes, int offset, short data) {
		bytes[offset] = (byte) ((data >> 8) & 0xff);
		bytes[offset + 1] = (byte) (data & 0xff);
		return offset + 2;
	}

	public static int writeInteger(byte[] bytes, int offset, int data) {
		bytes[offset] = (byte) ((data >> 24) & 0xff);
		bytes[offset + 1] = (byte) ((data >> 16) & 0xff);
		bytes[offset + 2] = (byte) ((data >> 8) & 0xff);
		bytes[offset + 3] = (byte) (data & 0xff);
		return offset + 4;
	}

	public static int writeLong(byte[] bytes, int offset, long data) {
		bytes[offset] = (byte) ((data >> 56) & 0xff);
		bytes[offset + 1] = (byte) ((data >> 48) & 0xff);
		bytes[offset + 2] = (byte) ((data >> 40) & 0xff);
		bytes[offset + 3] = (byte) ((data >> 32) & 0xff);
		bytes[offset + 4] = (byte) ((data >> 24) & 0xff);
		bytes[offset + 5] = (byte) ((data >> 16) & 0xff);
		bytes[offset + 6] = (byte) ((data >> 8) & 0xff);
		bytes[offset + 7] = (byte) (data & 0xff);
		return offset + 8;
	}

	public static int writeBoolean(byte[] bytes, int offset, boolean data) {
		if (data) {
			bytes[offset] = (byte) 1;
		} else {
			bytes[offset] = (byte) 0;
		}
		return offset + 1;
	}

	public static int writeString(byte[] bytes, int offset, String data) {
		byte[] stringBytes = data.getBytes(CHARSET);
		writeShort(bytes, offset, (short) stringBytes.length);
		offset += 2;

		for (int i = 0; i < stringBytes.length; i++) {
			bytes[offset + i] = stringBytes[i];
		}
		return offset + stringBytes.length;
	}

	public static byte readByte(byte[] bytes, int offset) {
		return bytes[offset];
	}

	public static short readShort(byte[] bytes, int offset) {
		short v = bytes[offset];
		v <<= 8;
		v |= (bytes[offset + 1] & 0xff);
		return v;
	}

	public static int readInteger(byte[] bytes, int offset) {
		int v = bytes[offset];
		v <<= 8;
		v |= (bytes[offset + 1] & 0xff);
		v <<= 8;
		v |= (bytes[offset + 2] & 0xff);
		v <<= 8;
		v |= (bytes[offset + 3] & 0xff);
		return v;
	}

	public static long readLong(byte[] bytes, int offset) {
		long v = bytes[offset];
		v <<= 8;
		v |= (bytes[offset + 1] & 0xff);
		v <<= 8;
		v |= (bytes[offset + 2] & 0xff);
		v <<= 8;
		v |= (bytes[offset + 3] & 0xff);
		v <<= 8;
		v |= (bytes[offset + 4] & 0xff);
		v <<= 8;
		v |= (bytes[offset + 5] & 0xff);
		v <<= 8;
		v |= (bytes[offset + 6] & 0xff);
		v <<= 8;
		v |= (bytes[offset + 7] & 0xff);
		return v;
	}

	public static boolean readBoolean(byte[] bytes, int offset) {
		return bytes[offset] == 1;
	}

	public static String readString(byte[] bytes, int offset) {
		// Get length
		short length = readShort(bytes, offset);
		return new String(Arrays.copyOfRange(bytes, offset + 2, offset + 2
				+ length), CHARSET);
	}

	public static class SerializationException extends RuntimeException {
		public SerializationException(String message) {
			super(message);
		}
	}
}
