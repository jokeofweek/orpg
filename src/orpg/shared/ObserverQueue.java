package orpg.shared;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;

/**
 * This class acts as a layer between an {@link Observer} object and it's
 * observers. The observer queue runs in its own thread in order to prevent
 * blocking the observer, and the observer queue is then put in charge of
 * forwarding the object to observers.
 * 
 * @author Dominic Charley-Roy
 * 
 */
public class ObserverQueue extends Observable implements Observer, Runnable {

	private Queue<Object> messageQueue;
	private int waitTime;

	public ObserverQueue() {
		this(50);
	}

	/**
	 * This creates a new queue with a specified time it should sleep when no
	 * messages are present.
	 * 
	 * @param waitTime
	 *            the time to sleep when no messages are present.
	 */
	public ObserverQueue(int waitTime) {
		this.messageQueue = new LinkedList<Object>();
		this.waitTime = waitTime;
	}

	@Override
	public void run() {
		while (true) {
			if (this.messageQueue.isEmpty()) {
				try {
					Thread.sleep(this.waitTime);
				} catch (InterruptedException e) {

				}
			} else {
				setChanged();
				notifyObservers(messageQueue.remove());
			}

		}
	}

	@Override
	public void update(Observable o, Object arg) {
		this.messageQueue.add(arg);
	}

}
