package orpg.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientApplication {

	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket s = new Socket("localhost", 8000);
		new BaseClient(s);
		while (true) ;
	}
	
}
