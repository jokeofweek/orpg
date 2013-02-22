package orpg.test.net;

import static org.junit.Assert.*;

import org.junit.Test;

import orpg.shared.net.InputByteBuffer;
import orpg.shared.net.OutputByteBuffer;

public class ByteBufferCompressionTest {

	@Test
	public void testEntireCompressionAndDecompressionOnBasicData() {
		OutputByteBuffer out = new OutputByteBuffer();
		for (int i = 0; i < 1000; i++) {
			out.putInt(5);
		}

		out.compress();

		InputByteBuffer in = new InputByteBuffer(out.getBytes());

		in.decompress();

		for (int i = 0; i < 1000; i++) {
			assertEquals(5, in.getInt());
		}

		assertTrue(in.atEnd());
	}

	@Test
	public void testEntireCompressionAndDecompressionOnRandomizdData() {
		long[] longs = new long[50];

		OutputByteBuffer out = new OutputByteBuffer();
		for (int i = 0; i < longs.length; i++) {
			longs[i] = (long) (Math.random() * Long.MAX_VALUE - (Long.MAX_VALUE / 2l));
			out.putLong(longs[i]);
		}

		out.compress();

		InputByteBuffer in = new InputByteBuffer(out.getBytes());

		in.decompress();

		for (int i = 0; i < longs.length; i++) {
			assertEquals(longs[i], in.getLong());
		}

		assertTrue(in.atEnd());
	}

	@Test
	public void testEntireCompressionAndDecompressionOnEmptyBuffer() {
		OutputByteBuffer out = new OutputByteBuffer();
		out.compress();
		InputByteBuffer in = new InputByteBuffer(out.getBytes());
		assertFalse(in.atEnd());
		in.decompress();
		assertTrue(in.atEnd());
	}

	@Test
	public void testPartialCompressionAndDecompression() {
		String during = "This is during the compression.";
		String after = "This is after compression.";
		String before = "Before compression!";

		OutputByteBuffer out = new OutputByteBuffer();
		out.putInt(1);
		out.putInt(2);
		out.putInt(3);
		out.putString(before);
		out.setCompressionMarker();
		out.putInt(4);
		out.putString(during);
		out.putInt(5);
		out.compress();
		out.putString(after);

		InputByteBuffer in = new InputByteBuffer(out.getBytes());
		assertEquals(1, in.getInt());
		assertEquals(2, in.getInt());
		assertEquals(3, in.getInt());
		assertEquals(before, in.getString());

		in.decompress();
		assertEquals(4, in.getInt());
		assertEquals(during, in.getString());
		assertEquals(5, in.getInt());

		assertEquals(after, in.getString());
	}

	@Test
	public void testMultiplePartialCompressionAndDecompression() {
		String during = "This is during the compression ";
		String after = "This is after compression ";
		String before = "Before compression ";

		OutputByteBuffer out = new OutputByteBuffer();
		out.putInt(1);
		out.putInt(2);
		out.putInt(3);
		out.putString(before + "1");
		out.setCompressionMarker();
		out.putInt(4);
		out.putString(during + "1");
		out.putInt(5);
		out.compress();
		out.putString(after + "1");
		out.putString(before + "2");
		out.setCompressionMarker();
		out.putBoolean(true);
		out.putString(during + "2");
		out.compress();
		out.putString(after + "2");

		InputByteBuffer in = new InputByteBuffer(out.getBytes());
		assertEquals(1, in.getInt());
		assertEquals(2, in.getInt());
		assertEquals(3, in.getInt());
		assertEquals(before + "1", in.getString());

		in.decompress();
		assertEquals(4, in.getInt());
		assertEquals(during + "1", in.getString());
		assertEquals(5, in.getInt());

		assertEquals(after + "1", in.getString());
		assertEquals(before + "2", in.getString());

		in.decompress();
		assertEquals(true, in.getBoolean());
		assertEquals(during + "2", in.getString());

		assertEquals(after + "2", in.getString());
	}

}
