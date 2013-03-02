package orpg.client.ui;

import orpg.shared.Constants;

public class ViewBox {

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

	public ViewBox(int visibleWidth, int visibleHeight, int contentWidth,
			int contentHeight) {
		this.offsetX = 0;
		this.offsetY = 0;
		this.visibleHeight = visibleHeight;
		this.visibleWidth = visibleWidth;
		this.contentHeight = contentHeight;
		this.contentWidth = contentWidth;

		// Set the initial offsets to factor in half a sprite
		initialOffsetX = (Constants.SPRITE_FRAME_WIDTH / 2)
				- (visibleWidth / 2);
		initialOffsetY = (Constants.SPRITE_FRAME_HEIGHT / 2)
				- (visibleHeight / 2);
	}

	public void scroll(int dX, int dY) {
		this.offsetX += dX;
		this.offsetY += dY;
		updateBoundedOffsets();
	}

	public void centerAtTile(int x, int y) {
		x = x * Constants.TILE_WIDTH;
		y = y * Constants.TILE_WIDTH;
		this.offsetX = x + initialOffsetX;
		this.offsetY = y + initialOffsetY;
		updateBoundedOffsets();
	}

	private void updateBoundedOffsets() {
		this.boundedOffsetX = Math.max(0,
				Math.min(offsetX, contentWidth - visibleWidth));
		this.boundedOffsetY = Math.max(0,
				Math.min(offsetY, contentHeight - visibleHeight));
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
}
