package orpg.test.server.data.store;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import orpg.shared.data.store.DataStoreException;

public class DataStoreExceptionTest {

	@Test
	public void testDataStoreExceptionWithOnlyThrowableReturnsThrowableMessage() {
		DataStoreException e = new DataStoreException(new RuntimeException(
				"ABC"));
		assertEquals("java.lang.RuntimeException: ABC", e.getMessage());
	}

	@Test
	public void testDataStoreExceptionWithMessageReturnsMessage() {
		DataStoreException e = new DataStoreException("Hello, world!");
		assertEquals("Hello, world!", e.getMessage());
	}

	@Test
	public void testDataStoreExceptionWithBothMessageAndThrowableReturnsMessage() {
		DataStoreException e = new DataStoreException("Hello, world!",
				new RuntimeException("ABC"));
		assertEquals("Hello, world!", e.getMessage());
	}

}
