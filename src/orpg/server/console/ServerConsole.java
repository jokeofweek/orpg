package orpg.server.console;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.logging.Handler;

public interface ServerConsole {

	public InputStream in();
	public PrintStream out();
	public Handler getHandler();
}
