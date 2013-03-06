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
		try {
			while (true) {
				baseServer.getWorld().setDelta(DELTAS_PER_TICK);
				baseServer.getWorld().process();
				Thread.sleep(20);
			}
		} catch (InterruptedException e) {

		}

	}

}
