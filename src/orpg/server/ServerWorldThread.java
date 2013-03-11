package orpg.server;

import com.artemis.World;

public class ServerWorldThread implements Runnable {

	private static final int MILLIS_PER_TICK = 20;
	private static final float DELTAS_PER_TICK = 20 / 1000.0f;

	private BaseServer baseServer;

	public ServerWorldThread(BaseServer baseServer) {
		this.baseServer = baseServer;
	}

	@Override
	public void run() {
		long tick = System.currentTimeMillis();
		long lastTick = System.currentTimeMillis();

		while (true) {
			lastTick = System.currentTimeMillis();
			baseServer.getWorld().setDelta(lastTick - tick);
			tick = lastTick;
			baseServer.getWorld().process();
			Thread.yield();
		}

	}
}
