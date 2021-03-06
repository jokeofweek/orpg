package orpg.shared.data.store;

public class DataStoreException extends Exception {

	private static final long serialVersionUID = -8361746835945010443L;

	public DataStoreException(String message) {
		super(message);
	}

	public DataStoreException(Throwable throwable) {
		super(throwable);
	}

	public DataStoreException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
