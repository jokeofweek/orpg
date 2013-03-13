package orpg.shared;

/**
 * This class allows the creation of a callback which will be called with typed
 * arguments.
 * 
 * @author Dominic Charley-Roy
 * @param <T>
 *            the type of object being bassed to the callback
 */
public abstract class Callback<T> {

	public abstract void invoke(T obj);

}
