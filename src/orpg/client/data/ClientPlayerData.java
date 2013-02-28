package orpg.client.data;

import orpg.client.BaseClient;
import orpg.shared.data.AccountCharacter;
import orpg.shared.data.Direction;

public class ClientPlayerData {

	private int xOffset;
	private int yOffset;
	private Direction moveDirection;
	private boolean moving;
	private AccountCharacter character;

	public ClientPlayerData(AccountCharacter character) {
		this.character = character;
	}

	public int getXOffset() {
		return xOffset;
	}

	public void setXOffset(int xOffset) {
		this.xOffset = xOffset;
	}

	public int getYOffset() {
		return yOffset;
	}

	public void setYOffset(int yOffset) {
		this.yOffset = yOffset;
	}

	public void setMoveDirection(Direction moveDirection) {
		this.moveDirection = moveDirection;
	}

	public Direction getMoveDirection() {
		return moveDirection;
	}

	public boolean isMoving() {
		return moving;
	}

	public void setMoving(boolean isMoving) {
		this.moving = isMoving;
	}

	public void setCharacter(AccountCharacter character) {
		this.character = character;
	}

	public AccountCharacter getCharacter() {
		return character;
	}

	public void move(Direction direction) {
		int oldX = character.getX();
		int oldY = character.getY();

		switch (direction) {
		case UP:
			xOffset = 0;
			yOffset = 32;
			moveDirection = Direction.UP;
			moving = true;
			character.setY(character.getY() - 1);
			break;
		case DOWN:
			xOffset = 0;
			yOffset = -32;
			moveDirection = Direction.DOWN;
			moving = true;
			character.setY(character.getY() + 1);
			break;
		case LEFT:
			xOffset = 32;
			yOffset = 0;
			moveDirection = Direction.LEFT;
			moving = true;
			character.setX(character.getX() - 1);
			break;
		case RIGHT:
			xOffset = -32;
			yOffset = 0;
			moveDirection = Direction.RIGHT;
			moving = true;
			character.setX(character.getX() + 1);
			break;
		}

		// Update the player on the map in case we moved segments.
		character.getMap().updatePlayer(character, oldX, oldY);

	}

}
