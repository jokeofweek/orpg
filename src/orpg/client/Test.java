package orpg.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import orpg.shared.Priority;

public class Test {

	public static void main(String... args) throws UnknownHostException, IOException {
		Socket s = new Socket("localhost", 8000);
		s.getOutputStream().write(new byte[]{0, 0, 1, 5});

		int b;
		while (true){
			b = s.getInputStream().read();
			if (b != -1) {
				System.out.println(b);
			}
		}
	}
	
}
