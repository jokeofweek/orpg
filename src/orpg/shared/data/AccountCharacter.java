package orpg.shared.data;

public class AccountCharacter {

	private String name;
	private short sprite;
	private int id;
	private Map map;
	private int x;
	private int y;
	private Direction direction;

	public AccountCharacter() {
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

	public short getSprite() {
		return sprite;
	}

	public void setSprite(short sprite) {
		this.sprite = sprite;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public boolean canMove(Direction direction) {
		if (direction == Direction.UP) {
			return this.getMap().isWalkable(x, y - 1);
		} else if (direction == Direction.DOWN) {
			return this.getMap().isWalkable(x, y + 1);
		} else if (direction == Direction.LEFT) {
			return this.getMap().isWalkable(x - 1, y);
		} else {
			return this.getMap().isWalkable(x + 1, y);
		}
	}
}
