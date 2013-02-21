package orpg.shared.state;

import java.util.EmptyStackException;
import java.util.Observable;
import java.util.Stack;

public abstract class StateManager extends Observable {

	private Stack<State> stateStack;
	private Object switchingLock;

	public StateManager() {
		this.stateStack = new Stack<State>();
		this.switchingLock = new Object();
	}

	protected abstract void onChangeState();

	/**
	 * @return if there is any states in the state manager.
	 */
	public boolean hasCurrentState() {
		return stateStack.isEmpty();
	}

	/**
	 * This fetches the current state from the state manager.
	 * 
	 * @return the current state.
	 * @throws IllegalStateException
	 *             if there is no state
	 */
	public State getCurrentState() throws IllegalStateException {
		try {
			return stateStack.peek();
		} catch (EmptyStackException e) {
			throw new IllegalStateException();
		}
	}

	/**
	 * This pops the current state from the stack of states, returning to the
	 * previous stack.
	 * 
	 * @throws IllegalStateException
	 *             if there is no state
	 */
	public void popState() throws IllegalStateException {
		synchronized (switchingLock) {
			getCurrentState().exit();
			stateStack.pop();

			if (!stateStack.isEmpty()) {
				getCurrentState().enter();
			}
		}
		
		onChangeState();
	}

	/**
	 * This enters a new state, pushing it on top of the state stack.
	 * 
	 * @param state
	 *            the new state to enter.
	 */
	public void pushState(State state) {
		synchronized (switchingLock) {
			if (!stateStack.isEmpty()) {
				getCurrentState().exit();
			}

			stateStack.push(state);
			state.enter();
		}
		
		onChangeState();
	}

	/**
	 * This switches the current state with a new state. Note that this differs
	 * from {@link StateManager#pushState(State)} as it pops off the current
	 * one, where as pushState does not.
	 * 
	 * @param state
	 * @throws IllegalStateException
	 *             if there is no state
	 */
	public void switchState(State state) throws IllegalStateException {
		synchronized (switchingLock) {
			getCurrentState().exit();
			stateStack.pop();
			stateStack.push(state);
			state.enter();
		}
		
		onChangeState();
	}

}
