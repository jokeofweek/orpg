package orpg.shared.data.component;

import orpg.shared.data.annotations.Editable;

import com.artemis.Component;

public class TestComponent extends Component {

	@Editable(name="x", description="X position")
	public int x;

	@Editable(name="y", description="Y position")
	public int y;

	@Editable(name="Name", description="The name of the entity")
	public String name;
	
	public String getName() {
		return name;
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	
}
