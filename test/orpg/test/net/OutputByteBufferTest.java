package orpg.test.net;

import static org.junit.Assert.*;

import org.junit.Test;

import orpg.shared.Constants;
import orpg.shared.net.InputByteBuffer;
import orpg.shared.net.OutputByteBuffer;

public class OutputByteBufferTest {

	@Test
	public void testGetBytesReturnsEmptyForEmptyBuffer() {
		// Preallocate space
		OutputByteBuffer out = new OutputByteBuffer(25);
		assertEquals(0, out.getBytes().length);
	}

	@Test
	public void testPutBytePutsBytesCorrectly() {
		OutputByteBuffer out = new OutputByteBuffer();
		byte[] bytes = new byte[0x100];
		for (int i = Byte.MIN_VALUE; i <= Byte.MAX_VALUE; i++) {
			bytes[i + 128] = (byte) i;
			out.putByte((byte) i);
		}

		assertArrayEquals(bytes, out.getBytes());
	}
	
	@Test
	public void testResetClearsBuffer() {
		OutputByteBuffer out = new OutputByteBuffer();
		byte[] empty = new byte[0];
		out.reset();
		assertArrayEquals(out.getBytes(), empty);
		out.putBoolean(true);
		out.putString("ABCTEST");
		out.reset();
		assertArrayEquals(out.getBytes(), empty);
	}

	@Test
	public void testPutShortPutsBytesCorrectly() {
		OutputByteBuffer out = new OutputByteBuffer();
		byte[] bytes = new byte[2002];
		for (short i = 0; i <= 1000; i++) {
			bytes[i * 2] = (byte) (((i - 500) >> 8) & 0xff);
			bytes[(i * 2) + 1] = (byte) ((i - 500) & 0xff);
			out.putShort((short) (i - 500));
		}

		assertArrayEquals(bytes, out.getBytes());
	}

	@Test
	public void testPutBooleanPutsBytesCorrectly() {
		OutputByteBuffer out = new OutputByteBuffer();
		out.putBoolean(true);
		out.putBoolean(false);
		out.putBoolean(true);
		out.putBoolean(true);
		out.putBoolean(false);
		byte[] bytes = new byte[] { 1, 0, 1, 1, 0 };
		assertArrayEquals(bytes, out.getBytes());
	}

	@Test
	public void testPutStringPutsStringCorrectly() {
		OutputByteBuffer out = new OutputByteBuffer();
		String strings[] = new String[] {
				"abcdefghijklmnopqrstuvwxyz"
						+ "abcdefghijklmnopqrstuvwxyz"
						+ "abcdefghijklmnopqrstuvwxyz"
						+ "abcdefghijklmnopqrstuvwxyz"
						+ "abcdefghijklmnopqrstuvwxyz"
						+ "abcdefghijklmnopqrstuvwxyz"
						+ "abcdefghijklmnopqrstuvwxyz"
						+ "abcdefghijklmnopqrstuvwxyz"
						+ "abcdefghijklmnopqrstuvwxyz"
						+ "abcdefghijklmnopqrstuvwxyz"
						+ "abcdefghijklmnopqrstuvwxyz"
						+ "abcdefghijklmnopqrstuvwxyz"
						+ "abcdefghijklmnopqrstuvwxyz"
						+ "abcdefghijklmnopqrstuvwxyz"
						+ "abcdefghijklmnopqrstuvwxyz"
						+ "abcdefghijklmnopqrstuvwxyz"
						+ "abcdefghijklmnopqrstuvwxyz"
						+ "abcdefghijklmnopqrstuvwxyz"
						+ "abcdefghijklmnopqrstuvwxyz"
						+ "abcdefghijklmnopqrstuvwxyz"
						+ "abcdefghijklmnopqrstuvwxyz"
						+ "abcdefghijklmnopqrstuvwxyz", "Hello, world!",
				"éeèôçéÉÀÈÇÄËÜ¨D" };

		byte[][] stringBytes = new byte[strings.length][];
		byte[] tmp;
		int i = 0;
		int totalLength = 0;

		for (String s : strings) {
			out.putString(s);
			tmp = s.getBytes(Constants.CHARSET);
			stringBytes[i] = new byte[4 + tmp.length];
			totalLength += 4 + tmp.length;
			stringBytes[i][0] = (byte) ((tmp.length >> 24) & 0xff);
			stringBytes[i][1] = (byte) ((tmp.length >> 16) & 0xff);
			stringBytes[i][2] = (byte) ((tmp.length >> 8) & 0xff);
			stringBytes[i][3] = (byte) (tmp.length & 0xff);
			System.arraycopy(tmp, 0, stringBytes[i], 4, tmp.length);
			i++;
		}

		byte[] testBytes = new byte[totalLength];
		i = 0;
		for (byte[] tmp2 : stringBytes) {
			System.arraycopy(tmp2, 0, testBytes, i, tmp2.length);
			i += tmp2.length;
		}

		assertArrayEquals(testBytes, out.getBytes());

	}

	@Test
	public void testPutCharArray() {
		OutputByteBuffer out = new OutputByteBuffer();
		out.putCharArray(new char[0]);
		assertArrayEquals(new byte[4], out.getBytes());

		out.reset();
		out.putCharArray(new char[] { 'A', 'B', 'C' });
		System.out.println(new String(new InputByteBuffer(out.getBytes()).getCharArray()));
		assertArrayEquals(new byte[] { (byte) 0, (byte) 0, (byte) 0,
				(byte) 3, (byte) 0, (byte) 65, (byte) 0, (byte) 66,
				(byte) 0, (byte) 67 }, out.getBytes());
		
	}

}
