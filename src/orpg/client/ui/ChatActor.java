package orpg.client.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import orpg.client.BaseClient;
import orpg.client.ClientConstants;
import orpg.shared.data.ChatChannel;
import orpg.shared.data.Message;
import orpg.shared.data.Pair;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.sun.org.apache.xml.internal.serialize.LineSeparator;

public class ChatActor extends Actor implements Observer {

	private Skin skin;
	private BaseClient baseClient;
	private BitmapFont bitmapFont;
	private Matrix4 matrix;
	private int x;
	private int y;
	private int width;
	private int lineHeight;

	private List<Pair<String, Color>> cachedLines;
	private Object updateLock;

	public ChatActor(BaseClient baseClient, Skin skin, int x, int y, int width) {
		this.baseClient = baseClient;
		this.skin = skin;
		this.x = x;
		this.y = y;
		this.width = width;
		this.matrix = new Matrix4();

		this.bitmapFont = skin.getFont("default-font");
		this.bitmapFont.setScale(1, -1);
		// Must multiply by -1 due to scale
		this.lineHeight = (int) (-1 * (bitmapFont.getLineHeight()));

		// Connect to chat controller
		this.updateLock = new Object();
		this.cachedLines = new ArrayList<Pair<String, Color>>(
				ClientConstants.CHAT_LINES);
		this.baseClient.getChatController().addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		// Whenever we get notified of a new message from the controller
		// we must remove as many lines as necessary to make this new message
		// fit. We then cache the lines and their color so that we don't
		// have to recaclulate bounds every time.
		Message message = (Message) arg;
		String representation = (message.getChannel() == ChatChannel.LOCAL ? ""
				: message.getChannel().getName() + ": ")
				+ message.getEmitter()
				+ " says: " + message.getContents();

		TextBounds bounds = bitmapFont.getWrappedBounds(representation, width);
		// calculate number of lines (negate due to scale)
		int lineSpan = (int) Math.ceil(-bounds.height / lineHeight);
		int remainingLines = lineSpan;

		// split the string into its lines
		List<String> lines = new ArrayList<String>(lineSpan);

		// loop until we have only 1 line left
		while (remainingLines > 1) {

			// estimate where we split up our line
			int position = representation.length() / lineSpan;
			bounds = bitmapFont.getBounds(representation, 0, position);
			if (bounds.width < width) {
				// grow the string till we cant
				while (position < representation.length() - 1) {
					position++;
					bounds = bitmapFont.getBounds(representation, 0, position);
					if (bounds.width > width) {
						position--;
						break;
					}
				}
			} else {
				// shrink till we cross width
				while (position > 0) {
					position--;
					bounds = bitmapFont.getBounds(representation, 0, position);
					if (bounds.width < width) {
						break;
					}
				}
			}

			lines.add(representation.substring(0, position));
			representation = representation.substring(position);
			remainingLines--;
		}

		lines.add(representation);

		// remove from the front of the list until we have enough space
		synchronized (updateLock) {
			while (cachedLines.size() + lineSpan > ClientConstants.CHAT_HISTORY) {
				cachedLines.remove(0);
			}
			for (String line : lines) {
				cachedLines.add(new Pair<String, Color>(line, Color.WHITE));
			}
		}

	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		int offset = 0;
		synchronized (updateLock) {
			Pair<String, Color> line;
			for (int i = Math.max(0, cachedLines.size()
					- ClientConstants.CHAT_LINES); i < cachedLines.size(); i++) {
				line = cachedLines.get(i);
				bitmapFont.setColor(line.getSecond());
				bitmapFont.draw(batch, line.getFirst(), x, y + offset);
				offset += lineHeight;
			}
		}

		// super.draw(batch, parentAlpha);
		// float xOffset = x;
		// bitmapFont.setColor(Color.YELLOW);
		// xOffset += bitmapFont.draw(batch, "Dominic: ", xOffset, y).width;
		// bitmapFont.setColor(Color.WHITE);
		// xOffset += bitmapFont.draw(batch, " Hello, world! ", xOffset,
		// y).width;

	}
}
