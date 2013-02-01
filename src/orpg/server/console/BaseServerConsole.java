package orpg.server.console;

import java.io.InputStream;
import java.io.PrintStream;

public class BaseServerConsole implements ServerConsole {

	@Override
	public InputStream in() {
		return System.in;
	}

	@Override
	public PrintStream out() {
		return System.out;
	}

}
