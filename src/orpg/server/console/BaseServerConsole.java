package orpg.server.console;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class BaseServerConsole implements ServerConsole {

	private Handler handler = new BaseServerConsoleHandler();

	@Override
	public InputStream in() {
		return System.in;
	}

	@Override
	public PrintStream out() {
		return System.out;
	}

	@Override
	public Handler getHandler() {
		return handler;
	}

	private static class BaseServerConsoleHandler extends Handler {

		@Override
		public void publish(LogRecord record) {
			System.out.println(String.format("%s: [%s] %s",
					record.getLoggerName(), record.getLevel(),
					record.getMessage()));
		}

		@Override
		public void flush() {
			System.out.flush();
		}

		@Override
		public void close() throws SecurityException {
			System.out.close();
		}

	}

}
