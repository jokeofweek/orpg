package orpg.editor.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Observable;
import java.util.Observer;

import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentMouseButtonListener;
import org.apache.pivot.wtk.ComponentMouseListener;
import org.apache.pivot.wtk.Dimensions;
import org.apache.pivot.wtk.ImageView;
import org.apache.pivot.wtk.Mouse.Button;

import orpg.editor.controller.MapController;
import orpg.shared.Constants;
import orpg.shared.data.Map;
import orpg.shared.data.MapLayer;

public class MapView extends ImageView implements Observer,
		ComponentMouseButtonListener, ComponentMouseListener {

	private MapController controller;
	private Dimensions realDimensions;

	private boolean leftDown;
	private boolean rightDown;

	public MapView(MapController controller) {
		this.controller = controller;
		this.controller.addObserver(this);

		// Setup the dimensions
		this.realDimensions = new Dimensions(this.controller.getMapWidth()
				* Constants.TILE_WIDTH, this.controller.getMapHeight()
				* Constants.TILE_HEIGHT);

		this.getComponentMouseListeners().add(this);
		this.getComponentMouseButtonListeners().add(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		if ((Object) o instanceof MapController) {
			repaint();
		}
	}

	@Override
	public void paint(Graphics2D graphics) {
		// TODO Auto-generated method stub
		super.paint(graphics);
		int h = controller.getMapHeight();
		int w = controller.getMapWidth();
		int l = MapLayer.values().length;
		short[][][] tiles = controller.getMapTiles();

		graphics.setBackground(Color.white);
		graphics.setColor(Color.black);

		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				for (int z = 0; z < l; z++) {
					if (tiles[y][x][z] == 0) {
						if (z == 0) {
							graphics.drawRect(x * Constants.TILE_WIDTH, y
									* Constants.TILE_HEIGHT,
									Constants.TILE_WIDTH,
									Constants.TILE_HEIGHT);
						}
					} else {
						graphics.fillRect(x * Constants.TILE_WIDTH, y
								* Constants.TILE_HEIGHT,
								Constants.TILE_WIDTH,
								Constants.TILE_HEIGHT);
					}
				}
			}
		}
	}

	@Override
	public Dimensions getPreferredSize() {
		return this.realDimensions;
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
		if (button == Button.LEFT) {
			leftDown = true;
		} else if (button == Button.RIGHT) {
			rightDown = true;
		}
		if (button == Button.LEFT) {
			controller.updateTile(x / Constants.TILE_WIDTH, y
					/ Constants.TILE_HEIGHT, MapLayer.GROUND, (short) 1);
		}
		return false;
	}

	@Override
	public boolean mouseUp(Component component, Button button, int x, int y) {
		if (button == Button.LEFT) {
			leftDown = false;
		} else if (button == Button.RIGHT) {
			rightDown = false;
		}
		return false;
	}

	@Override
	public boolean mouseMove(Component component, int x, int y) {
		if (leftDown) {
			controller.updateTile(x / Constants.TILE_WIDTH, y
					/ Constants.TILE_HEIGHT, MapLayer.GROUND, (short) 1);
		} else if (rightDown) {
			controller.updateTile(x / Constants.TILE_WIDTH, y
					/ Constants.TILE_HEIGHT, MapLayer.GROUND, (short) 0);
		}
		return false;
	}

	@Override
	public void mouseOut(Component component) {

	}

	@Override
	public void mouseOver(Component component) {

	}

}
