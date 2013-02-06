package orpg.editor.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import orpg.editor.controller.MapEditorController;
import orpg.shared.Constants;

public class TilesView extends JPanel implements MouseMotionListener,
		MouseListener, Observer {

	private BufferedImage image;

	private boolean leftDown;
	private boolean rightDown;
	private boolean inMultiSelect;
	private MapEditorController editorController;

	public TilesView(MapEditorController editorController, BufferedImage image) {
		this.editorController = editorController;
		this.editorController.addObserver(this);
		this.image = image;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D graphics = (Graphics2D) g;
		graphics.drawImage(image, 0, 0, null);
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
	public void update(Observable o, Object arg) {
		if (o == editorController) {
			repaint();
		}
	}

	@Override
	public int getWidth() {
		return image.getWidth();
	}

	@Override
	public int getHeight() {
		return image.getHeight();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(getWidth(), getHeight());
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
			mouseMoved(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getComponent() == this) {
			if (SwingUtilities.isLeftMouseButton(e)) {
				leftDown = false;
			} else if (SwingUtilities.isRightMouseButton(e)) {
				rightDown = false;
				inMultiSelect = false;
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
		leftDown = false;
		rightDown = false;
		inMultiSelect = false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {

		int x = e.getX();
		int y = e.getY();
		if (e.getComponent() == this) {
			if (leftDown) {
				// Detect if our selected change, and if so update it.
				editorController.updateTileRange(x / Constants.TILE_WIDTH, y
						/ Constants.TILE_HEIGHT, x / Constants.TILE_WIDTH, y
						/ Constants.TILE_HEIGHT);
				repaint();
			} else if (rightDown) {
				if (inMultiSelect) {
					// Essentially we only update our tile range if we move
					// right or
					// down.
					int newX = x / Constants.TILE_WIDTH;
					int newY = y / Constants.TILE_HEIGHT;

					if (newX >= editorController.getTileRange().getStartX()
							&& newY >= editorController.getTileRange()
									.getStartY()) {
						editorController.updateTileRange(editorController
								.getTileRange().getStartX(), editorController
								.getTileRange().getStartY(), newX, newY);
					}
				} else {
					// If it is the first rightclick, then just update the
					// starting
					// position and mark that we are in multi-select mode.
					editorController
							.updateTileRange(x / Constants.TILE_WIDTH, y
									/ Constants.TILE_HEIGHT, x
									/ Constants.TILE_WIDTH, y
									/ Constants.TILE_HEIGHT);
					inMultiSelect = true;
				}
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}
}
