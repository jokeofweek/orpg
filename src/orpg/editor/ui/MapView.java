package orpg.editor.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;

import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentMouseButtonListener;
import org.apache.pivot.wtk.ComponentMouseListener;
import org.apache.pivot.wtk.ScrollPane;
import org.apache.pivot.wtk.Mouse.Button;
import orpg.editor.controller.MapController;
import orpg.editor.controller.MapEditorController;
import orpg.editor.data.change.MapEditorTileEraseChange;
import orpg.editor.data.change.MapEditorTileUpdateChange;
import orpg.shared.Constants;
import orpg.shared.data.MapLayer;

public class MapView extends Component implements Observer,
		ComponentMouseButtonListener, ComponentMouseListener {

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
		installSkin(MapView.class);

		this.tileWidth = Constants.TILE_WIDTH
				/ editorController.getScaleFactor();
		this.tileHeight = Constants.TILE_HEIGHT
				/ editorController.getScaleFactor();

		this.getComponentMouseListeners().add(this);
		this.getComponentMouseButtonListeners().add(this);
	}

	public MapController getMapController() {
		return mapController;
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
			invalidate();
		}
		repaint();

	}

	@Override
	public boolean mouseClick(Component component, Button button, int x, int y,
			int count) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseDown(Component component, Button button, int x, int y) {
		if (component == this) {
			if (button == Button.LEFT) {
				leftDown = true;
			} else if (button == Button.RIGHT) {
				rightDown = true;
			}
			mouseMove(component, x, y);
			return true;
		}
		return false;
	}

	@Override
	public boolean mouseUp(Component component, Button button, int x, int y) {
		if (component == this) {
			if (button == Button.LEFT) {
				leftDown = false;
			} else if (button == Button.RIGHT) {
				rightDown = false;
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean mouseMove(Component component, int x, int y) {
		if (component == this) {
			if (leftDown) {
				// Make sure we fit in (could be possible not to, because of
				// scale)
				if (x / tileWidth >= mapController.getMapWidth()
						|| y / tileHeight >= mapController.getMapHeight()) {
					return false;
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
					return false;
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
			return true;
		}
		return false;
	}

	@Override
	public void mouseOut(Component component) {
		leftDown = false;
		rightDown = false;
	}

	@Override
	public void mouseOver(Component component) {

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
	public void paint(Graphics2D graphics) {
		int h = mapController.getMapHeight();
		int w = mapController.getMapWidth();
		int l = MapLayer.values().length;
		short[][][] tiles = mapController.getMapTiles();

		graphics.setBackground(Color.black);
		graphics.setColor(Color.black);
		graphics.clearRect(0, 0, getWidth(), getHeight());
		int dx = 0;
		int dy = 0;

		for (int y = 0; y < h; y++) {
			dx = 0;
			for (int x = 0; x < w; x++) {
				for (int z = 0; z < l; z++) {
					if (tiles[y][x][z] == 0) {
						if (z == 0) {
							graphics.drawImage(image, dx, dy, dx + tileWidth,
									dy + tileHeight, 0, 0,
									Constants.TILE_WIDTH,
									Constants.TILE_HEIGHT, null);
						}
					} else {
						graphics.drawImage(
								image,
								dx,
								dy,
								dx + tileWidth,
								dy + tileHeight,
								(tiles[y][x][z] % Constants.TILESET_WIDTH)
										* Constants.TILE_WIDTH,
								(tiles[y][x][z] / Constants.TILESET_WIDTH)
										* Constants.TILE_HEIGHT,
								(1 + (tiles[y][x][z] % Constants.TILESET_WIDTH))
										* Constants.TILE_WIDTH,
								(1 + (tiles[y][x][z] / Constants.TILESET_WIDTH))
										* Constants.TILE_HEIGHT, null);
					}
				}
				dx += tileWidth;
			}
			dy += tileHeight;
		}
	}
}
