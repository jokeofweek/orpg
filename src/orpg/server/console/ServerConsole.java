package orpg.server.console;

import java.io.InputStream;
import java.io.PrintStream;

public interface ServerConsole {

	public InputStream in();
	public PrintStream out();
	
}
