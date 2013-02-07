package orpg.editor.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import orpg.editor.controller.MapController;
import orpg.editor.controller.MapEditorController;
import orpg.editor.data.change.MapEditorTileEraseChange;
import orpg.editor.data.change.MapEditorTileUpdateChange;
import orpg.shared.Constants;
import orpg.shared.data.MapLayer;

public class MapView extends JComponent implements Observer, MouseListener,
		MouseMotionListener {

	private MapController mapController;
	private MapEditorController editorController;
	private Image image;

	private boolean leftDown;
	private boolean rightDown;

	private int tileWidth;
	private int tileHeight;

	private boolean inComponent;
	private int hoverOverTileX;
	private int hoverOverTileY;

	private JScrollPane container;
	
	public MapView(MapController controller,
			MapEditorController editorController,
			JScrollPane container) {
		this.mapController = controller;
		this.editorController = editorController;
		this.container = container;
		this.mapController.addObserver(this);
		this.editorController.addObserver(this);

		try {
			this.image = ImageIO.read(new File(Constants.CLIENT_DATA_PATH + "gfx/tiles.png"));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		this.tileWidth = Constants.TILE_WIDTH
				/ editorController.getScaleFactor();
		this.tileHeight = Constants.TILE_HEIGHT
				/ editorController.getScaleFactor();

		this.addMouseListener(this);
		this.addMouseMotionListener(this);
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

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D graphics = (Graphics2D) g;
		int h = mapController.getMapHeight();
		int w = mapController.getMapWidth();
		int l = MapLayer.values().length;
		short[][][] tiles = mapController.getMapTiles();

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
		int startX = Math.max(0,
				(container.getHorizontalScrollBar().getValue() / tileWidth) - 4);
		int startY = Math.max(0,
				(container.getVerticalScrollBar().getValue() / tileHeight) - 4);
		int endX = Math.min(startX + 6 + (container.getWidth() / tileWidth),
				mapController.getMapWidth());
		int endY = Math.min(startY + 6 + (container.getHeight() / tileHeight),
				mapController.getMapHeight());

		for (int z = 0; z < l; z++) {
			dY = startY * tileHeight;
			for (int y = startY; y < endY; y++) {
				dX = startX * tileWidth;
				for (int x = startX; x < endX; x++) {
					if (tiles[y][x][z] == 0) {
						if (z == 0) {
							graphics.drawImage(image, dX, dY, dX + tileWidth,
									dY + tileHeight, 0, 0,
									Constants.TILE_WIDTH,
									Constants.TILE_HEIGHT, null);
						}
					} else {
						graphics.drawImage(
								image,
								dX,
								dY,
								dX + tileWidth,
								dY + tileHeight,
								(tiles[y][x][z] % Constants.TILESET_WIDTH)
										* Constants.TILE_WIDTH,
								(tiles[y][x][z] / Constants.TILESET_WIDTH)
										* Constants.TILE_HEIGHT,
								(1 + (tiles[y][x][z] % Constants.TILESET_WIDTH))
										* Constants.TILE_WIDTH,
								(1 + (tiles[y][x][z] / Constants.TILESET_WIDTH))
										* Constants.TILE_HEIGHT, null);
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
				graphics.drawImage(image, hoverOverTileX * tileWidth,
						hoverOverTileY * tileHeight, (hoverOverTileX + xWide)
								* tileWidth, (hoverOverTileY + yWide)
								* tileHeight, editorController.getTileRange()
								.getStartX() * Constants.TILE_WIDTH,
						editorController.getTileRange().getStartY()
								* Constants.TILE_HEIGHT, (editorController
								.getTileRange().getEndX() + 1)
								* Constants.TILE_WIDTH, (editorController
								.getTileRange().getEndY() + 1)
								* Constants.TILE_HEIGHT, null);
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

				// Make sure there is a change:
				int startX = editorController.getTileRange().getStartX();
				int startY = editorController.getTileRange().getStartY();
				int diffX = editorController.getTileRange().getEndX() - startX
						+ 1;
				int diffY = editorController.getTileRange().getEndY() - startY
						+ 1;
				int currentTile = startX + (startY * Constants.TILESET_WIDTH);

				short[][][] tiles = mapController.getMapTiles();
				int layer = editorController.getCurrentLayer().ordinal();

				boolean changed = false;
				// Convert to positional values
				int pX = x / tileWidth;
				int pY = y / tileHeight;

				// Check each x/y value
				for (int cY = pY; cY < Math.min(mapController.getMapHeight(),
						pY + diffY) && !changed; cY++) {
					for (int cX = pX; cX < Math.min(
							mapController.getMapWidth(), pX + diffX); cX++) {
						if (tiles[cY][cX][layer] != currentTile + (cX - pX)) {
							changed = true;
							break;
						}
					}
					currentTile += Constants.TILESET_WIDTH;
				}

				// If there was a change, add it
				if (changed) {

					editorController.getChangeManager().addChange(
							new MapEditorTileUpdateChange(editorController,
									mapController, x / tileWidth, y
											/ tileHeight));
				}

			} else if (rightDown) {
				// Make sure we fit in (could be possible not to, because of
				// scale)
				if (x / tileWidth >= mapController.getMapWidth()
						|| y / tileHeight >= mapController.getMapHeight()) {
					return;
				}

				// If a right-click, then erase the tile if not already empty.
				if (mapController.getMapTiles()[y / tileHeight][x / tileWidth][editorController
						.getCurrentLayer().ordinal()] != 0) {

					editorController.getChangeManager().addChange(
							new MapEditorTileEraseChange(editorController,
									mapController, x / tileWidth, y
											/ tileHeight));
				}
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
