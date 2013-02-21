package orpg.shared.state;

public interface State {

	public void enter();
	
	public void exit();
	
	public void displayError(String errorMessage);
}
