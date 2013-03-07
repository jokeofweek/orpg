package orpg.client.data.component;

import orpg.shared.data.Direction;

import com.artemis.Component;

public class AnimatedPlayer extends Animated {

	private int xOffset;
	private int yOffset;
	private Direction animationDirection;

	public AnimatedPlayer() {
		super();
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.animationDirection = animationDirection;

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

	public Direction getAnimationDirection() {
		return animationDirection;
	}

	public void setAnimationDirection(Direction animationDirection) {
		this.animationDirection = animationDirection;
	}

	@Override
	public int getFrame() {
		if (!isAnimating()) {
			return 0;
		}
		
		switch (animationDirection) {
		case UP:
			return (yOffset > 16) ? 1 : 3;
		case DOWN:
			return (yOffset > -16) ? 3 : 1;
		case LEFT:
			return (xOffset > 16) ? 1 : 3;
		case RIGHT:
			return (xOffset > -16) ? 3 : 1;
		}
		
		return 0;

	}
}
