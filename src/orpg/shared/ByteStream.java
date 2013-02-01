package orpg.shared;

import java.nio.charset.Charset;
import java.util.Arrays;

public class ByteStream {
	
	public static final String CHARSET_NAME = "UTF-8";
	private static final Charset CHARSET = Charset.forName(CHARSET_NAME);

	public static Object[] unserialize(byte[] data, Class... classes) throws ArrayIndexOutOfBoundsException {
		int offset = 0;
		
		// Get first byte for number of objects
		Object[] objects = new Object[readByte(data, offset)];
		offset++;
		
		// If we have a mismatch between object count and class count.
		if (objects.length != classes.length) {
			throw new IllegalArgumentException("Class count does not match object count.");
		}
		
		for (int i = 0; i < objects.length; i++) {
			System.out.println(offset + "," + data.length);
			if (classes[i] == Byte.class) {
				objects[i] = readByte(data, offset);
			} else if (classes[i] == Short.class) {
				objects[i] = readShort(data, offset);
			} else if (classes[i] == Integer.class) {
				//objects[i] = readInteger(data, offset);
			} else if (classes[i] == Long.class) {
				// objects[i] = readLong(data, offset);
			} else if (classes[i] == Boolean.class) {
				objects[i] = readBoolean(data, offset);
			} else if (classes[i] == String.class) {
				objects[i] = readString(data, offset);
			} else {
				throw new IllegalArgumentException("Invalid object type '" + classes[i] + "'. Cannot be deserialized.");
			}
			
			// Add to the offset
			offset += getByteLength(objects[i]);
		}
		
		return objects;
	}
	
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
		offset = writeByte(data, offset, (byte)objects.length);

		// Then put the length of the data
		for (Object o : objects) {
			if (o instanceof Number) {
				if (o instanceof Byte) {
					offset = writeByte(data, offset, (Byte)o);
				} else if (o instanceof Short) {
					offset = writeShort(data, offset, (Short)o);
				} else if (o instanceof Integer) {
					offset = writeInteger(data, offset, (Integer)o);
				} else {
					throw new IllegalArgumentException("Invalid object type '"
							+ o.getClass() + "'. Cannot be serialized.");
				}
			} else if (o instanceof Boolean){
				offset = writeBoolean(data, offset, (Boolean)o);
			} else if (o instanceof String) {
				offset = writeString(data, offset, (String)o);
			} else {
				throw new IllegalArgumentException("Invalid object type '"
						+ o.getClass() + "'. Cannot be serialized.");
			}
		}
		
		return data;
	}
	
	public static int getByteLength(Object o) {
		if (o instanceof Number) {
			if (o instanceof Byte) {
				return 1;
			} else if (o instanceof Short) {
				return 2;
			} else if (o instanceof Integer) {
				return 4;
			} else if (o instanceof Long) {
				return 8;
			} else if (o instanceof Float) {
				return 4;
			} else if (o instanceof Double) {
				return 8;
			} else {
				throw new IllegalArgumentException("Invalid object type '"
						+ o.getClass() + "'. Cannot be serialized.");
			}
		} else if (o instanceof Boolean) {
			return 1;
		} else if (o instanceof String) {
			return 2 + ((String) o).getBytes(CHARSET).length;
		} else {
			throw new IllegalArgumentException("Invalid object type '"
					+ o.getClass() + "'. Cannot be serialized.");
		}
	}

	public static int writeByte(byte[] bytes, int offset, byte data) {
		bytes[offset] = data;
		return offset + 1;
	}

	public static int writeShort(byte[] bytes, int offset, short data) {
		bytes[offset] = (byte) (data >> 8);
		bytes[offset + 1] = (byte) (data & 0xff);
		return offset + 2;
	}

	public static int writeInteger(byte[] bytes, int offset, int data) {
		bytes[offset] = (byte) (data >> 24);
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
			bytes[offset] = (byte)1;
		} else {
			bytes[offset] = (byte)0;
		}
		return offset+ 1; 
	}
	
	public static int writeString(byte[] bytes, int offset, String data) {
		byte[] stringBytes = data.getBytes(CHARSET);
		writeShort(bytes, offset, (short)stringBytes.length);
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
		short s = bytes[offset];
		s <<= 8;
		s += bytes[offset + 1];
		return s;
	}
	
	public static boolean readBoolean(byte[] bytes, int offset){
		return bytes[offset] == 1;
	}
	
	public static String readString(byte[] bytes, int offset) {
		// Get length
		short length = readShort(bytes, offset);
		return new String(Arrays.copyOfRange(bytes, offset + 2, offset + 2 + length + 1), CHARSET);
	}
}
