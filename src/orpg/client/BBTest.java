package orpg.client;

import java.io.IOException;
import java.util.Arrays;

import orpg.shared.ByteStream;

public class BBTest {

	public static void main(String[] args) throws IOException {
		byte[] bytes = ByteStream.serialize((byte) 1, true,
				"Hello, world!", Byte.MIN_VALUE, Short.MAX_VALUE,
				Integer.MAX_VALUE, Long.MAX_VALUE, false, "Goodbye, world...");
		bytes = Arrays.copyOfRange(bytes, 3, bytes.length);
		Object[] objects = ByteStream.unserialize(bytes, Boolean.class,
				String.class, Byte.class, Short.class, Integer.class,
				Long.class, Boolean.class, String.class);

		for (Object o : objects) {
			System.out.println(o);
		}
	}

}
