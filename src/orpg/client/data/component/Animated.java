package orpg.client.data.component;

import com.artemis.Component;

public abstract class Animated extends Component {

	private boolean isAnimating;

	public boolean isAnimating() {
		return isAnimating;
	}

	public void setAnimating(boolean isAnimating) {
		this.isAnimating = isAnimating;
	}

	public abstract int getFrame();

}
