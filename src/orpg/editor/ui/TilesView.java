package orpg.editor.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Observable;
import java.util.Observer;

import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentMouseButtonListener;
import org.apache.pivot.wtk.ComponentMouseListener;
import org.apache.pivot.wtk.ImageView;
import org.apache.pivot.wtk.Mouse.Button;
import org.apache.pivot.wtk.media.Image;

import orpg.editor.controller.MapEditorController;
import orpg.shared.Constants;

public class TilesView extends ImageView implements ComponentMouseListener,
		ComponentMouseButtonListener, Observer {

	private Image image;

	private boolean leftDown;
	private boolean rightDown;
	private boolean inMultiSelect;
	private MapEditorController editorController;

	public TilesView(MapEditorController editorController, Image image) {
		super(image);
		this.editorController = editorController;
		this.editorController.addObserver(this);
		getComponentMouseButtonListeners().add(this);
		getComponentMouseListeners().add(this);

	}

	@Override
	public void paint(Graphics2D graphics) {
		super.paint(graphics);
		graphics.setColor(Color.red);

		graphics.drawRect(editorController.getTileRange().getStartX()
				* Constants.TILE_WIDTH, editorController.getTileRange()
				.getStartY() * Constants.TILE_HEIGHT, (editorController
				.getTileRange().getEndX()
				- editorController.getTileRange().getStartX() + 1)
				* Constants.TILE_WIDTH, (editorController.getTileRange()
				.getEndY() - editorController.getTileRange().getStartY() + 1)
				* Constants.TILE_HEIGHT);
	}

	@Override
	public boolean mouseClick(Component component, Button button, int x, int y,
			int count) {
		return false;
	}

	@Override
	public boolean mouseDown(Component component, Button button, int x, int y) {
		if (button == Button.LEFT) {
			leftDown = true;
		} else if (button == Button.RIGHT) {
			rightDown = true;
		}
		mouseMove(component, x, y);
		return false;
	}

	@Override
	public boolean mouseUp(Component component, Button button, int x, int y) {
		if (button == Button.LEFT) {
			leftDown = false;
		} else if (button == Button.RIGHT) {
			rightDown = false;
			inMultiSelect = false;
		}
		return false;
	}

	@Override
	public boolean mouseMove(Component component, int x, int y) {
		if (leftDown) {
			// Detect if our selected change, and if so update it. Only
			// update once to avoid flickering.
			editorController.updateTileRange(x / Constants.TILE_WIDTH, y
					/ Constants.TILE_HEIGHT, x / Constants.TILE_WIDTH, y
					/ Constants.TILE_HEIGHT);
			repaint();
		} else if (rightDown) {
			if (inMultiSelect) {
				// Essentially we only update our tile range if we move right or
				// down.
				int newX = x / Constants.TILE_WIDTH;
				int newY = y / Constants.TILE_HEIGHT;

				if (newX > editorController.getTileRange().getStartX()
						&& newY > editorController.getTileRange().getStartY()) {
					editorController.updateTileRange(editorController
							.getTileRange().getStartX(), editorController
							.getTileRange().getStartY(), newX, newY);
				}
			} else {
				// If it is the first rightclick, then just update the starting
				// position and mark that we are in multi-select mode.
				editorController.updateTileRange(x / Constants.TILE_WIDTH, y
						/ Constants.TILE_HEIGHT, x / Constants.TILE_WIDTH, y
						/ Constants.TILE_HEIGHT);
				inMultiSelect = true;
			}
		}
		return false;
	}

	@Override
	public void mouseOut(Component component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseOver(Component component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Observable o, Object arg) {
		if (o == editorController) {
			repaint();
		}
	}
}
