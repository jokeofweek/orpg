package orpg.test.shared.data;
import static org.junit.Assert.*;

import org.junit.Test;

import orpg.shared.data.Validator;

public class ValidatorTest {

	@Test
	public void isAccountNameValid() {
		assertTrue(Validator.isAccountNameValid("a"));
		assertTrue(Validator.isAccountNameValid("a123"));
		assertTrue(Validator.isAccountNameValid("z_Z_123"));
		assertTrue(Validator.isAccountNameValid("a_b.c_1234"));
		assertTrue(Validator.isAccountNameValid("a_b.c_1234"));
		assertFalse(Validator.isAccountNameValid(""));
		assertFalse(Validator.isAccountNameValid("."));
		assertFalse(Validator.isAccountNameValid(".."));
		assertFalse(Validator.isAccountNameValid("../"));
		assertFalse(Validator.isAccountNameValid("\""));
		assertFalse(Validator.isAccountNameValid("_"));
		assertFalse(Validator.isAccountNameValid("Test;"));
		assertFalse(Validator.isAccountNameValid("account@test"));
		assertFalse(Validator.isAccountNameValid(" test_account"));
		assertFalse(Validator.isAccountNameValid("test-account"));
	}

}
