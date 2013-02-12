package orpg.shared.data;

public class Validator {

	public static boolean isAccountNameValid(String name) {
		return name != null && name.length() < 255
				&& name.matches("^[A-Za-z0-9][A-Za-z0-9\\\\/_\\.]*$");
	}

}
