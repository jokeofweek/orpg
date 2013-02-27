package orpg.client.ui;

import orpg.shared.Constants;

public class ViewBox {

	private int offsetX;
	private int offsetY;
	private int visibleWidth;
	private int visibleHeight;
	private int contentWidth;
	private int contentHeight;

	public ViewBox(int visibleWidth, int visibleHeight, int contentWidth,
			int contentHeight) {
		this.offsetX = 0;
		this.offsetY = 0;
		this.visibleHeight = visibleHeight;
		this.visibleWidth = visibleWidth;
		this.contentHeight = contentHeight;
		this.contentWidth = contentWidth;
	}

	public void scroll(int dX, int dY) {
		this.offsetX = Math.max(0,
				Math.min(offsetX + dX, contentWidth - visibleWidth));
		this.offsetY = Math.max(0,
				Math.min(offsetY + dY, contentHeight - visibleHeight));
	}

	public void center(int x, int y) {

		this.offsetX = Math.max(
				0,
				Math.min(x - (visibleWidth / 2), contentWidth
						- visibleWidth));
		this.offsetY = Math.max(
				0,
				Math.min(y - (visibleHeight / 2), contentHeight
						- visibleHeight));
	}

	public int getStartX() {
		return offsetX / Constants.TILE_WIDTH;
	}

	public int getEndX() {
		return (offsetX + visibleWidth) / Constants.TILE_WIDTH;
	}

	public int getStartY() {
		return offsetY / Constants.TILE_HEIGHT;
	}

	public int getEndY() {
		return (offsetY + visibleHeight) / Constants.TILE_HEIGHT;
	}

	public int getOffsetX() {
		return offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}
}
