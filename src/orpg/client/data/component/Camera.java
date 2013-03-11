package orpg.client.data.component;

import orpg.shared.Constants;

import com.artemis.Component;

public class Camera extends Component {

	private int offsetX;
	private int offsetY;
	private int boundedOffsetX;
	private int boundedOffsetY;
	private int visibleWidth;
	private int visibleHeight;
	private int contentWidth;
	private int contentHeight;
	private int initialOffsetX;
	private int initialOffsetY;

	public Camera(int visibleWidth, int visibleHeight, int contentWidth,
			int contentHeight, int startX, int startY) {
		updateBoundedOffsets();
		this.visibleHeight = visibleHeight;
		this.visibleWidth = visibleWidth;
		this.contentHeight = contentHeight;
		this.contentWidth = contentWidth;

		// Set the initial offsets to factor in half a sprite
		initialOffsetX = (Constants.SPRITE_FRAME_WIDTH / 2)
				- (visibleWidth / 2);
		initialOffsetY = (Constants.SPRITE_FRAME_HEIGHT / 2)
				- (visibleHeight / 2);
		
		this.offsetX = startX + initialOffsetX;
		this.offsetY = startY + initialOffsetY;
	}

	public int getStartX() {
		return boundedOffsetX / Constants.TILE_WIDTH;
	}

	public int getEndX() {
		return Math.min((contentWidth / Constants.TILE_WIDTH) - 1,
				(boundedOffsetX + visibleWidth) / Constants.TILE_WIDTH);
	}

	public int getStartY() {
		return boundedOffsetY / Constants.TILE_HEIGHT;
	}

	public int getEndY() {
		return Math.min((contentHeight / Constants.TILE_HEIGHT) - 1,
				(boundedOffsetY + visibleHeight) / Constants.TILE_HEIGHT);
	}

	public int getOffsetX() {
		return boundedOffsetX;
	}

	public int getOffsetY() {
		return boundedOffsetY;
	}

	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
		updateBoundedOffsets();
	}

	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
		updateBoundedOffsets();
	}

	private void updateBoundedOffsets() {
		this.boundedOffsetX = Math.max(0,
				Math.min(offsetX, contentWidth - visibleWidth));
		this.boundedOffsetY = Math.max(0,
				Math.min(offsetY, contentHeight - visibleHeight));
	}

}
