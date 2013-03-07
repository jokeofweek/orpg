package orpg.client.systems;

import orpg.client.BaseClient;
import orpg.client.data.component.Animated;
import orpg.client.data.component.AnimatedPlayer;
import orpg.shared.data.Direction;
import orpg.shared.data.component.Moveable;
import orpg.shared.data.component.Position;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;

public class MovementSystem extends EntityProcessingSystem {

	@Mapper
	ComponentMapper<Moveable> movementMapper;
	@Mapper
	ComponentMapper<Position> positionMapper;
	@Mapper
	ComponentMapper<AnimatedPlayer> animatedMapper;

	private BaseClient baseClient;

	public MovementSystem(BaseClient baseClient) {
		super(Aspect.getAspectForAll(Position.class, Moveable.class));
		this.baseClient = baseClient;
	}

	@Override
	protected void process(Entity e) {
		Position position = positionMapper.get(e);
		Moveable moveable = movementMapper.get(e);

		if (moveable.isMoving() && !moveable.isMoveProcessed()) {
			moveable.setMoveProcessed(true);
			// Apply the move
			switch (moveable.getDirection()) {
			case UP:
				position.setY(position.getY() - 1);
				break;
			case DOWN:
				position.setY(position.getY() + 1);
				break;
			case LEFT:
				position.setX(position.getX() - 1);
				break;
			case RIGHT:
				position.setX(position.getX() + 1);
				break;
			}

			// TODO: Update segment here

			// Animate if necessary
			if (animatedMapper.getSafe(e) != null) {
				world.getSystem(AnimationSystem.class).animate(e,
						moveable.getDirection());
			}
		}
	}

	public boolean canMove(Entity entity, int x, int y) {
		return baseClient.getMap().isWalkable(x, y);
	}

}
