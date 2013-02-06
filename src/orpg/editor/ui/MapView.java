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

	public MapView(MapController controller,
			MapEditorController editorController) {
		this.mapController = controller;
		this.editorController = editorController;

		this.mapController.addObserver(this);
		this.editorController.addObserver(this);

		try {
			this.image = ImageIO.read(new File("gfx/tiles.png"));
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

		for (int z = 0; z < l; z++) {
			dY = 0;
			for (int y = 0; y < h; y++) {
				dX = 0;
				for (int x = 0; x < w; x++) {
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
		}

		// Render grid above everything if necessary
		if (editorController.isGridEnabled()) {
			graphics.setComposite(AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, 0.45f));
			graphics.setColor(Color.gray);
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					graphics.drawRect(x * tileWidth, y * tileHeight, tileWidth,
							tileHeight);
				}
			}
			graphics.setComposite(AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, 1.0f));
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
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

		if (e.getComponent() == this) {
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

}
