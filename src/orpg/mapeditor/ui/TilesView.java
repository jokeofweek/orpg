package orpg.mapeditor.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.pivot.util.concurrent.TaskExecutionException;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentMouseButtonListener;
import org.apache.pivot.wtk.ComponentMouseListener;
import org.apache.pivot.wtk.Dimensions;
import org.apache.pivot.wtk.ImageView;
import org.apache.pivot.wtk.Mouse.Button;
import org.apache.pivot.wtk.Panel;
import org.apache.pivot.wtk.media.Image;

import orpg.shared.Constants;

public class TilesView extends ImageView implements ComponentMouseListener,
		ComponentMouseButtonListener {

	private Image image;

	private int selectedX;
	private int selectedY;
	private boolean leftDown;
	private boolean rightDown;

	public TilesView(Image image) {
		super(image);
		getComponentMouseButtonListeners().add(this);
		getComponentMouseListeners().add(this);

	}

	@Override
	public void paint(Graphics2D graphics) {
		super.paint(graphics);
		graphics.setColor(Color.red);
		graphics.drawRect(selectedX * Constants.TILE_WIDTH, selectedY
				* Constants.TILE_HEIGHT, Constants.TILE_WIDTH,
				Constants.TILE_HEIGHT);
	}

	@Override
	public boolean mouseClick(Component component, Button button, int x, int y,
			int count) {
		return false;
	}

	@Override
	public boolean mouseDown(Component component, Button button, int x, int y) {
		if (button == Button.LEFT)
			leftDown = true;
		else if (button == Button.RIGHT)
			rightDown = true;
		return false;
	}

	@Override
	public boolean mouseUp(Component component, Button button, int x, int y) {
		if (button == Button.LEFT)
			leftDown = false;
		else if (button == Button.RIGHT)
			rightDown = false;
		return false;
	}

	@Override
	public boolean mouseMove(Component component, int x, int y) {
		if (leftDown) {
			// Detect if our selected change, and if so update it. Only
			// update once to avoid flickering.
			selectedX = x / Constants.TILE_WIDTH;
			selectedY = y / Constants.TILE_WIDTH;
			repaint();
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
}
