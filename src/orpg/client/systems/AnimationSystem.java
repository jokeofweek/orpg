package orpg.client.systems;

import orpg.client.BaseClient;
import orpg.client.ClientConstants;
import orpg.client.data.component.AnimatedPlayer;
import orpg.client.ui.ViewBox;
import orpg.shared.Constants;
import orpg.shared.data.Direction;
import orpg.shared.data.component.Moveable;
import orpg.shared.data.component.Position;
import orpg.shared.data.component.Renderable;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AnimationSystem extends EntityProcessingSystem {
	@Mapper
	ComponentMapper<Position> positionMapper;
	@Mapper
	ComponentMapper<AnimatedPlayer> animationMapper;
	@Mapper
	ComponentMapper<Moveable> moveableMapper;

	private BaseClient baseClient;

	public AnimationSystem(BaseClient baseClient) {
		super(Aspect.getAspectForAll(Position.class, AnimatedPlayer.class));
		this.baseClient = baseClient;
	}

	public void animate(Entity entity, Direction direction) {
		AnimatedPlayer animation = animationMapper.get(entity);
		switch (direction) {
		case UP:
			animation.setXOffset(0);
			animation.setYOffset(32);
			break;
		case DOWN:
			animation.setXOffset(0);
			animation.setYOffset(-32);
			break;
		case LEFT:
			animation.setXOffset(32);
			animation.setYOffset(0);
			break;
		case RIGHT:
			animation.setXOffset(-32);
			animation.setYOffset(0);
			break;
		}

		animation.setAnimationDirection(direction);
		animation.setAnimating(true);
	}

	@Override
	protected void process(Entity e) {
		Position position = positionMapper.get(e);
		AnimatedPlayer animation = animationMapper.get(e);

		if (animation.isAnimating()) {
			switch (animation.getAnimationDirection()) {
			case UP:
				animation
						.setYOffset(animation.getYOffset()
								- (int) ((world.getDelta() * ClientConstants.WALK_SPEED)));
				if (animation.getYOffset() < 0) {
					animation.setYOffset(0);
				}
				break;
			case DOWN:
				animation
						.setYOffset(animation.getYOffset()
								+ (int) ((world.getDelta() * ClientConstants.WALK_SPEED)));
				if (animation.getYOffset() > 0) {
					animation.setYOffset(0);
				}
				break;
			case LEFT:
				animation
						.setXOffset(animation.getXOffset()
								- (int) ((world.getDelta() * ClientConstants.WALK_SPEED)));
				if (animation.getXOffset() < 0) {
					animation.setXOffset(0);
				}
				break;
			case RIGHT:
				animation
						.setXOffset(animation.getXOffset()
								+ (int) ((world.getDelta() * ClientConstants.WALK_SPEED)));
				if (animation.getXOffset() > 0) {
					animation.setXOffset(0);
				}
				break;
			}

			if (animation.getXOffset() == 0 && animation.getYOffset() == 0) {
				animation.setAnimating(false);
				moveableMapper.get(e).setMoving(false);
			}
		}

	}
}
