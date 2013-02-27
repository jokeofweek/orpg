package orpg.shared;

public enum ErrorMessage {

	ACCOUNT_ALREADY_EXISTS("An account already exists with that name."), 
	ACCOUNT_NAME_HAS_INVALID_CHARACTERS("The account name contains invalid characters."),
	GENERIC_ACCOUNT_CREATION_ERROR("An error occured while creating the account. Please try again later."), 
	LOGIN_INVALID_CREDENTIALS("This set of credentials is not valid. Please try again."),
	GENERIC_LOGIN_ERROR("An error occured while attempting to login. Please try again later."),
	GENERIC_USE_CHARACTER_ERROR("An error occured while selecting this character. Please try again later."),
	CHARACTER_IN_USE("This character is already logged in!");
	
	private String message;
	
	private ErrorMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

}
