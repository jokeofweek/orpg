package orpg.test.server.data;

import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;

import org.junit.Before;
import org.junit.Test;

import orpg.server.data.Account;

public class AccountTest {

	private Account account;

	@Before
	public void setUp() throws Exception {
		account = new Account();
	}

	@Test
	public void testUpdatePasswordChangesPassword()
			throws NoSuchAlgorithmException {
		account.updatePassword("test".toCharArray());
		assertTrue(account.passwordMatches("test".toCharArray()));
		account.updatePassword("hello".toCharArray());
		assertTrue(account.passwordMatches("hello".toCharArray()));
		account.updatePassword("234FDFDSfds./fdsF?DSF?DS>".toCharArray());
		assertTrue(account.passwordMatches("234FDFDSfds./fdsF?DSF?DS>"
				.toCharArray()));
		account.updatePassword("".toCharArray());
		assertTrue(account.passwordMatches("".toCharArray()));
	}

	@Test
	public void testUpdatePasswordClearsArray()
			throws NoSuchAlgorithmException {
		char[] pass = new char[] { 'a', 'b', 'c' };
		account.updatePassword(pass);
		assertArrayEquals(new char[] { '\0', '\0', '\0' }, pass);
		pass = new char[] { 'a', 'b', 'c', 'd', 'e', '.' };
		account.updatePassword(pass);
		assertArrayEquals(
				new char[] { '\0', '\0', '\0', '\0', '\0', '\0' }, pass);
	}

	@Test
	public void testMatchPasswordClearsArray()
			throws NoSuchAlgorithmException {
		account.updatePassword("abc".toCharArray());

		char[] pass = new char[] { 'a', 'b', 'c' };
		account.passwordMatches(pass);
		assertArrayEquals(new char[] { '\0', '\0', '\0' }, pass);
		pass = new char[] { 'a', 'b', 'c', 'd', 'e', '.' };
		account.updatePassword(pass);
		assertArrayEquals(
				new char[] { '\0', '\0', '\0', '\0', '\0', '\0' }, pass);
	}

}
