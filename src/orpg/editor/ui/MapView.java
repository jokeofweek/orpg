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
import org.apache.pivot.wtk.Mouse.Button;
import orpg.editor.controller.MapController;
import orpg.editor.controller.MapEditorController;
import orpg.editor.data.MapEditorTileUpdateChange;
import orpg.editor.data.MapEditorTileEraseChange;
import orpg.shared.Constants;
import orpg.shared.data.MapLayer;

public class MapView extends Component implements Observer,
		ComponentMouseButtonListener, ComponentMouseListener {

	private MapController mapController;
	private MapEditorController editorController;
	private Image image;

	private boolean leftDown;
	private boolean rightDown;

	public MapView(MapController controller,
			MapEditorController editorController) {
		this.mapController = controller;
		this.editorController = editorController;
		this.mapController.addObserver(this);

		try {
			this.image = ImageIO.read(new File("gfx/tiles.png"));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		installSkin(MapView.class);

		this.getComponentMouseListeners().add(this);
		this.getComponentMouseButtonListeners().add(this);
	}

	public MapController getMapController() {
		return mapController;
	}

	@Override
	public void update(Observable o, Object arg) {
		if ((Object) o instanceof MapController) {
			repaint();
		}
	}

	@Override
	public boolean mouseClick(Component component, Button button, int x,
			int y, int count) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseDown(Component component, Button button, int x,
			int y) {
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
				// If a left-click, then signal that we've made a change
				editorController.getChangeManager().addChange(
						new MapEditorTileUpdateChange(editorController,
								mapController, x / Constants.TILE_WIDTH, y
										/ Constants.TILE_HEIGHT));
			} else if (rightDown) {
				// If a right-click, then erase the tile
				editorController.getChangeManager().addChange(
						new MapEditorTileEraseChange(editorController,
								mapController, x / Constants.TILE_WIDTH, y
										/ Constants.TILE_HEIGHT));
			}
			return true;
		}
		return false;
	}

	@Override
	public void mouseOut(Component component) {

	}

	@Override
	public void mouseOver(Component component) {

	}

	@Override
	public int getWidth() {
		return mapController.getMapWidth() * Constants.TILE_WIDTH;
	}

	@Override
	public int getHeight() {
		return mapController.getMapHeight() * Constants.TILE_HEIGHT;
	}

	@Override
	public void paint(Graphics2D graphics) {
		// MapController controller = ((MapView)getComponent()).getController();
		int h = mapController.getMapHeight();
		int w = mapController.getMapWidth();
		int l = MapLayer.values().length;
		short[][][] tiles = mapController.getMapTiles();

		graphics.setBackground(Color.white);
		graphics.setColor(Color.black);

		int dx = 0;
		int dy = 0;

		for (int y = 0; y < h; y++) {
			dx = 0;
			for (int x = 0; x < w; x++) {
				for (int z = 0; z < l; z++) {
					if (tiles[y][x][z] == 0) {
						if (z == 0) {
							graphics.drawImage(image, dx, dy, dx
									+ Constants.TILE_HEIGHT, dy
									+ Constants.TILE_WIDTH, 0, 0,
									Constants.TILE_WIDTH,
									Constants.TILE_HEIGHT, null);
						}
					} else {
						graphics.drawImage(
								image,
								dx,
								dy,
								dx + Constants.TILE_HEIGHT,
								dy + Constants.TILE_WIDTH,
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
				dx += Constants.TILE_WIDTH;
			}
			dy += Constants.TILE_HEIGHT;
		}
	}
}
