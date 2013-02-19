package orpg.editor.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import orpg.editor.controller.MapEditorController;
import orpg.shared.Constants;

public class TilesView extends JPanel implements MouseMotionListener,
		MouseListener, Observer {

	private Image[] images;

	private boolean leftDown;
	private boolean rightDown;
	private boolean inMultiSelect;
	private int multiSelectX;
	private int multiSelectY;
	private MapEditorController editorController;

	public TilesView(MapEditorController editorController,
			Image[] images) {
		this.editorController = editorController;
		this.editorController.addObserver(this);
		this.images = images;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D graphics = (Graphics2D) g;
		int tilesetHeight = Constants.TILE_HEIGHT * Constants.TILESET_HEIGHT;
		int currentHeight = 0;
		
		for (Image image : images){
			graphics.drawImage(image, 0, currentHeight, null);
			currentHeight += tilesetHeight;
		}
		
		graphics.setColor(Color.red);

		graphics.drawRect(editorController.getTileRange().getStartX()
				* Constants.TILE_WIDTH, editorController.getTileRange()
				.getStartY() * Constants.TILE_HEIGHT, (editorController
				.getTileRange().getEndX()
				- editorController.getTileRange().getStartX() + 1)
				* Constants.TILE_WIDTH,
				(editorController.getTileRange().getEndY()
						- editorController.getTileRange().getStartY() + 1)
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
		return Constants.TILE_WIDTH * Constants.TILESET_WIDTH;
	}

	@Override
	public int getHeight() {
		return Constants.TILE_HEIGHT * Constants.TILESET_HEIGHT
				* Constants.TILESETS;
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
			mouseDragged(e);
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
	}

	@Override
	public void mouseDragged(MouseEvent e) {

		int x = e.getX();
		int y = e.getY();
		if (e.getComponent() == this) {
			if (leftDown) {
				// Detect if our selected change, and if so update it.
				editorController.updateTileRange(x / Constants.TILE_WIDTH,
						y / Constants.TILE_HEIGHT, x
								/ Constants.TILE_WIDTH, y
								/ Constants.TILE_HEIGHT);
				repaint();
			} else if (rightDown) {
				if (inMultiSelect) {
					// Update tile range based on quadrant
					int newX = Math
							.max(0,
									Math.min(
											x / Constants.TILE_WIDTH,
											(getWidth() / Constants.TILE_WIDTH) - 1));
					int newY = Math
							.max(0,
									Math.min(
											y / Constants.TILE_HEIGHT,
											(getHeight() / Constants.TILE_HEIGHT) - 1));

					if (newX < multiSelectX) {
						if (newY < multiSelectY) {
							editorController.updateTileRange(newX, newY,
									multiSelectX, multiSelectY);
						} else {
							editorController.updateTileRange(newX,
									multiSelectY, multiSelectX, newY);
						}
					} else {
						if (newY < multiSelectY) {
							editorController.updateTileRange(multiSelectX,
									newY, newX, multiSelectY);
						} else {
							editorController.updateTileRange(multiSelectX,
									multiSelectY, newX, newY);
						}
					}

				} else {
					// If it is the first rightclick, then just update the
					// starting
					// position and mark that we are in multi-select mode.
					editorController.updateTileRange(x
							/ Constants.TILE_WIDTH, y
							/ Constants.TILE_HEIGHT, x
							/ Constants.TILE_WIDTH, y
							/ Constants.TILE_HEIGHT);
					inMultiSelect = true;
					multiSelectX = x / Constants.TILE_WIDTH;
					multiSelectY = y / Constants.TILE_HEIGHT;
				}
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}
}
