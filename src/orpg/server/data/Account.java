package orpg.server.data;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import orpg.shared.Constants;
import orpg.shared.data.AccountCharacter;

public class Account {

	private int id;
	private String name;
	private String email;
	private String salt;
	private String passwordHash;
	private List<AccountCharacter> characters;

	public Account() {
		this.characters = new ArrayList<AccountCharacter>(0);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public List<AccountCharacter> getCharacters() {
		return characters;
	}

	public void setCharacters(List<AccountCharacter> characters) {
		this.characters = characters;
	}

	public AccountCharacter findCharacter(String name) {
		for (AccountCharacter character : characters) {
			if (character.getName().equals(name)) {
				return character;
			}
		}

		return null;
	}

	private String hashPassword(char[] password, String salt)
			throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-512");

		// Add the salt to the digest
		digest.update(salt.getBytes(Constants.CHARSET));

		// Add the bytes from the password
		byte[] bytes = new byte[(password.length * 2)];
		for (int i = 0; i < password.length; i++) {
			bytes[2 * i] = (byte) ((password[i] >> 8) & 0xff);
			bytes[(2 * i) + 1] = (byte) (password[i] & 0xff);
		}
		digest.update(bytes);

		// Hash
		byte[] saltedBytes = digest.digest();

		// Clear bytes array
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = 0;
		}

		// Generate the hex string of the password
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < saltedBytes.length; i++) {
			builder.append(Integer.toHexString(saltedBytes[i]));
		}

		return builder.toString();
	}

	private String generateSalt() {
		char[] characters = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G',
				'H', 'I', 'J', 'K', 'L', 'O', 'M', 'N', 'O', 'P', 'Q', 'R',
				'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
				'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
				'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1',
				'2', '3', '4', '5', '6', '7', '8', '9', '.', '_', '?', '#',
				'%', ';' };
		Random random = new Random(System.currentTimeMillis());
		String generated = null;
		do {
			int length = random.nextInt(10) + 10;
			StringBuilder saltBuilder = new StringBuilder(length);
			for (int i = 0; i < length; i++) {
				saltBuilder
						.append(characters[random.nextInt(characters.length)]);
			}
			generated = saltBuilder.toString();
		} while (generated.equals(this.salt));
		return generated;

	}

	/**
	 * This updates the account with a given password. Note that the array
	 * passed to this function is cleared after the test is done.
	 * 
	 * @param password
	 *            the new password.
	 * @throws NoSuchAlgorithmException
	 */
	public void updatePassword(char[] password) throws NoSuchAlgorithmException {
		// Setup the salt
		this.salt = generateSalt();
		this.passwordHash = hashPassword(password, salt);

		// Clear password array
		for (int i = 0; i < password.length; i++) {
			password[i] = '\0';
		}
	}

	/**
	 * This tests whether a given password matches the user's password. Note
	 * that the array passed to this function is cleared after the test is done.
	 * 
	 * @param password
	 *            the password to test.
	 * @return whether the password matches the account password or not.
	 * @throws NoSuchAlgorithmException
	 */
	public boolean passwordMatches(char[] password)
			throws NoSuchAlgorithmException {
		boolean matches = hashPassword(password, salt)
				.equals(this.passwordHash);
		// Clear password array
		for (int i = 0; i < password.length; i++) {
			password[i] = '\0';
		}

		return matches;
	}

}
