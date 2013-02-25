package orpg.editor.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import orpg.editor.controller.MapController;
import orpg.editor.controller.MapEditorController;
import orpg.shared.Constants;
import orpg.shared.data.Map;
import orpg.shared.data.MapLayer;

public class MapView extends JComponent implements Observer, MouseListener,
		MouseMotionListener {

	private MapController mapController;
	private MapEditorController editorController;
	private Image[] images;
	private Image loadingTile;

	private boolean leftDown;
	private boolean rightDown;

	private int tileWidth;
	private int tileHeight;

	private boolean inComponent;
	private int hoverOverTileX;
	private int hoverOverTileY;

	private JScrollPane scrollPane;

	public MapView(final MapController controller,
			MapEditorController editorController, JScrollPane scrollPane,
			Image[] images, Image loadingTile) {
		this.mapController = controller;
		this.editorController = editorController;
		this.scrollPane = scrollPane;
		this.mapController.addObserver(this);
		this.editorController.addObserver(this);
		this.images = images;
		this.loadingTile = loadingTile;

		this.tileWidth = Constants.TILE_WIDTH
				/ editorController.getScaleFactor();
		this.tileHeight = Constants.TILE_HEIGHT
				/ editorController.getScaleFactor();

		this.addMouseListener(this);
		this.addMouseMotionListener(this);

		// Setup the scroll bars to make requests for segments
		AdjustmentListener listener = new MapViewAdjustmentListener(controller,
				editorController, scrollPane);
		scrollPane.getHorizontalScrollBar().addAdjustmentListener(listener);
		scrollPane.getVerticalScrollBar().addAdjustmentListener(listener);
	}

	public MapController getMapController() {
		return mapController;
	}

	@Override
	public int getWidth() {
		return mapController.getMapWidth() * tileWidth;
	}

	@Override
	public int getHeight() {
		return mapController.getMapHeight() * tileHeight;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(getWidth(), getHeight());
	}

	private void renderTile(Graphics2D g, int x, int y, int tile) {
		int srcX = (tile % Constants.TILESET_WIDTH) * Constants.TILE_WIDTH;
		int srcY = ((tile / Constants.TILESET_HEIGHT) % Constants.TILESET_HEIGHT)
				* Constants.TILE_HEIGHT;

		g.drawImage(this.images[(tile / Constants.TILESET_WIDTH)
				/ Constants.TILESET_HEIGHT], x, y, x + tileWidth, y
				+ tileHeight, srcX, srcY, srcX + Constants.TILE_WIDTH, srcY
				+ Constants.TILE_HEIGHT, null);

	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D graphics = (Graphics2D) g;
		int h = mapController.getMapHeight();
		int w = mapController.getMapWidth();
		int l = MapLayer.values().length;

		graphics.setBackground(Color.black);
		graphics.setColor(Color.black);
		graphics.clearRect(0, 0, getWidth(), getHeight());
		int dX = 0;
		int dY = 0;

		// Example of alphablending. Use setComposite to do per layer blending.
		// AlphaComposite regular =
		// AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
		// AlphaComposite blended =
		// AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
		AlphaComposite regular = AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, 1.0f);

		// Determine renderable area
		int startX = Math.max(0, (scrollPane.getHorizontalScrollBar()
				.getValue() / tileWidth) - 4);
		int startY = Math
				.max(0,
						(scrollPane.getVerticalScrollBar().getValue() / tileHeight) - 4);
		int endX = Math.min(startX + 6 + (scrollPane.getWidth() / tileWidth),
				mapController.getMapWidth());
		int endY = Math.min(startY + 6 + (scrollPane.getHeight() / tileHeight),
				mapController.getMapHeight());
		short tile;

		for (int z = 0; z < l; z++) {
			dY = startY * tileHeight;
			for (int y = startY; y < endY; y++) {
				dX = startX * tileWidth;
				for (int x = startX; x < endX; x++) {
					tile = mapController.getTile(x, y, z);
					if (tile == 0) {
						if (z == 0) {
							graphics.drawImage(this.images[0], dX, dY, dX
									+ tileWidth, dY + tileHeight, 0, 0,
									Constants.TILE_WIDTH,
									Constants.TILE_HEIGHT, null);
						}
					} else if (tile == Map.LOADING_TILE) {
						if (z == 0) {
							graphics.drawImage(this.loadingTile, dX, dY, dX
									+ tileWidth, dY + tileHeight, 0, 0,
									Constants.TILE_WIDTH,
									Constants.TILE_HEIGHT, null);
						}
					} else {
						renderTile(graphics, dX, dY, tile);
					}

					dX += tileWidth;
				}
				dY += tileHeight;
			}

			// If we are hovering over the map, render overlay after.
			if (inComponent && editorController.isHoverPreviewEnabled()
					&& z == editorController.getCurrentLayer().ordinal()) {
				graphics.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER,
						Constants.MAP_EDITOR_TILE_OVERLAY_TRANSPARENCY));
				graphics.setColor(Color.blue);
				// Render the entire tilerange
				int xWide = editorController.getTileRange().getEndX()
						- editorController.getTileRange().getStartX() + 1;
				int yWide = editorController.getTileRange().getEndY()
						- editorController.getTileRange().getStartY() + 1;

				int overlayTile = (editorController.getTileRange().getStartY() * Constants.TILESET_WIDTH)
						+ editorController.getTileRange().getStartX();
				for (int y = 0; y < yWide; y++) {
					for (int x = 0; x < xWide; x++) {
						renderTile(graphics, (hoverOverTileX + x) * tileWidth,
								(hoverOverTileY + y) * tileHeight, overlayTile
										+ x);
					}
					overlayTile += Constants.TILESET_WIDTH;
				}

				graphics.setComposite(regular);
			}
		}

		// Render grid above everything if necessary
		if (editorController.isGridEnabled()) {
			graphics.setComposite(AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER,
					Constants.MAP_EDITOR_GRID_TRANSPARENCY));
			graphics.setColor(Color.gray);
			// Render the entire tilerange
			for (int y = startY; y < endY; y++) {
				for (int x = startX; x < endX; x++) {
					graphics.drawRect(x * tileWidth, y * tileHeight, tileWidth,
							tileHeight);
				}
			}
			graphics.setComposite(regular);
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getComponent() == this) {
			if (SwingUtilities.isLeftMouseButton(e)) {
				leftDown = true;
			} else if (SwingUtilities.isRightMouseButton(e)) {
				rightDown = true;
			}
			mouseDragged(e);
			e.consume();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {

		if (e.getComponent() == this) {
			if (SwingUtilities.isLeftMouseButton(e)) {
				leftDown = false;
			} else if (SwingUtilities.isRightMouseButton(e)) {
				rightDown = false;
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		inComponent = true;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		inComponent = false;
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

		if (e.getComponent() == this) {
			if (inComponent) {
				updateHoverPosition(e.getX(), e.getY());
			}
			if (leftDown) {
				// Make sure we fit in (could be possible not to, because of
				// scale)
				if (x / tileWidth >= mapController.getMapWidth()
						|| y / tileHeight >= mapController.getMapHeight()
						|| x < 0 || y < 0) {
					return;
				}

				editorController.getCurrentTool().leftClick(editorController,
						mapController, x / tileWidth, y / tileHeight);

			} else if (rightDown) {
				// Make sure we fit in (could be possible not to, because of
				// scale)
				if (x / tileWidth >= mapController.getMapWidth()
						|| y / tileHeight >= mapController.getMapHeight()
						|| x < 0 || y < 0) {
					return;
				}

				editorController.getCurrentTool().rightClick(editorController,
						mapController, x / tileWidth, y / tileHeight);
			}
		}

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (inComponent) {
			updateHoverPosition(e.getX(), e.getY());
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		int oldTileWidth = tileWidth;
		int oldTileHeight = tileHeight;
		this.tileWidth = Constants.TILE_WIDTH
				/ editorController.getScaleFactor();
		this.tileHeight = Constants.TILE_HEIGHT
				/ editorController.getScaleFactor();

		// If we zoomed in / out, then we must invalidate everyhing first
		// else just repaint
		if (tileHeight != oldTileHeight || tileWidth != oldTileWidth) {
			revalidate();
			getParent().getParent().repaint();
		}
		repaint();

	}

	public void updateHoverPosition(int x, int y) {
		int tmpX = this.hoverOverTileX;
		int tmpY = this.hoverOverTileY;

		this.hoverOverTileX = x / tileWidth;
		this.hoverOverTileY = y / tileHeight;

		if (tmpX != hoverOverTileX || tmpY != hoverOverTileY) {
			repaint();
		}
	}

}
