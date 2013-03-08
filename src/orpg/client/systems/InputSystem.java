package orpg.client.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import orpg.client.BaseClient;
import orpg.client.data.component.Animated;
import orpg.client.data.component.AnimatedPlayer;
import orpg.client.data.component.HandlesInput;
import orpg.client.net.packets.MoveRequestPacket;
import orpg.shared.data.Direction;
import orpg.shared.data.component.Moveable;
import orpg.shared.data.component.Named;
import orpg.shared.data.component.Position;

public class InputSystem extends EntityProcessingSystem {

	@Mapper
	ComponentMapper<Position> positionMapper;
	@Mapper
	ComponentMapper<Moveable> moveableMapper;

	private BaseClient baseClient;

	public InputSystem(BaseClient baseClient) {
		super(Aspect.getAspectForAll(HandlesInput.class));
		this.baseClient = baseClient;
	}

	@Override
	protected void process(Entity e) {
		Moveable moveable = moveableMapper.get(e);

		// Handle movement input
		if (moveable != null && !moveable.isMoving()) {
			MovementSystem movementSystem = world
					.getSystem(MovementSystem.class);
			Position position = positionMapper.get(e);
			int x = position.getX();
			int y = position.getY();

			if (Gdx.input.isKeyPressed(Input.Keys.LEFT)
					&& movementSystem.canMove(e, x - 1, y)) {
				moveable.setDirection(Direction.LEFT);
				moveable.setMoving(true);
				
				baseClient.sendPacket(MoveRequestPacket.LEFT);
				baseClient.getSegmentRequestManager()
						.requestSurroundingSegments(x - 1, y);
			} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)
					&& movementSystem.canMove(e, x + 1, y)) {
				moveable.setDirection(Direction.RIGHT);
				moveable.setMoving(true);

				baseClient.sendPacket(MoveRequestPacket.RIGHT);
				baseClient.getSegmentRequestManager()
						.requestSurroundingSegments(x + 1, y);
			} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)
					&& movementSystem.canMove(e, x, y + 1)) {

				moveable.setDirection(Direction.DOWN);
				moveable.setMoving(true);

				baseClient.sendPacket(MoveRequestPacket.DOWN);
				baseClient.getSegmentRequestManager()
						.requestSurroundingSegments(x, y + 1);
			} else if (Gdx.input.isKeyPressed(Input.Keys.UP)
					&& movementSystem.canMove(e, x, y - 1)) {

				moveable.setDirection(Direction.UP);
				moveable.setMoving(true);

				baseClient.sendPacket(MoveRequestPacket.UP);
				baseClient.getSegmentRequestManager()
						.requestSurroundingSegments(x, y - 1);
			}

		}

	}

}
