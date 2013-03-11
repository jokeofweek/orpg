package orpg.client.ui;

import orpg.client.ClientConstants;
import orpg.client.data.component.AnimatedPlayer;
import orpg.client.data.component.Camera;
import orpg.shared.Constants;
import orpg.shared.data.component.Position;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;

public class CameraSystem extends EntityProcessingSystem {

	@Mapper
	ComponentMapper<AnimatedPlayer> animationMapper;
	@Mapper
	ComponentMapper<Position> positionMapper;
	@Mapper
	ComponentMapper<Camera> cameraMapper;

	public CameraSystem() {
		super(Aspect.getAspectForAll(Camera.class));
	}

	@Override
	protected void process(Entity e) {
		AnimatedPlayer animation = animationMapper.get(e);
		Position position = positionMapper.get(e);
		Camera camera = cameraMapper.get(e);

		int dX = 0;
		int dY = 0;

		if (animation.isAnimating()) {
			switch (animation.getAnimationDirection()) {
			case UP:
				dY = (int) (-world.delta * 2 * ClientConstants.WALK_SPEED);
				break;
			case DOWN:
				dY = (int) (world.delta * 2 * ClientConstants.WALK_SPEED);
				break;
			case LEFT:
				dX = (int) (-world.delta * 2 * ClientConstants.WALK_SPEED);
				break;
			case RIGHT:
				dX = (int) (world.delta * 2 * ClientConstants.WALK_SPEED);
				break;
			}
		}

		camera.setOffsetX(camera.getOffsetX() + dX);
		camera.setOffsetY(camera.getOffsetY() + dY);
	}

}
