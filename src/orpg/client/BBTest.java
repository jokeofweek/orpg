package orpg.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import orpg.shared.ByteStream;

public class BBTest {

	public static void main(String[] args) throws IOException {
		byte[] bytes = ByteStream.serialize((byte)1, true, "Hello, world.", false);
		
		bytes = Arrays.copyOfRange(bytes, 3, bytes.length);
		Object[] objects = ByteStream.unserialize(bytes, Boolean.class, String.class, Boolean.class);
		
		for (Object o : objects) {
			System.out.println(o);
		}
	}

}
