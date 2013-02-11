package orpg.server.data.store;

public class DataStoreException extends RuntimeException {

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

	@Override
	public String getMessage() {
		if (getMessage() == null && getCause() != null) {
			return getCause().getMessage();
		} else {
			return super.getMessage();
		}
	}
}
