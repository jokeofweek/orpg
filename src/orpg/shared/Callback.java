package orpg.shared;

/**
 * This interface allows the creation of a callback which will be called with typed
 * arguments.
 * 
 * @author Dominic Charley-Roy
 * @param <T>
 *            the type of object being bassed to the callback
 */
public interface Callback<T> {

	public void invoke(T obj);

}
