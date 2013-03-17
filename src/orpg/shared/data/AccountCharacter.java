package orpg.shared.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import orpg.shared.ChatChannel;
import orpg.shared.Constants;

public class AccountCharacter {

	private String name;
	private short sprite;
	private int id;
	private Map map;
	private int x;
	private int y;
	private Direction direction;
	private int chatChannelSubscriptions;

	public AccountCharacter() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public short getSprite() {
		return sprite;
	}

	public void setSprite(short sprite) {
		this.sprite = sprite;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Set<ChatChannel> getChatChannelSubscriptions() {
		// Convert the bitset into a list of chat channels
		Set<ChatChannel> subscriptions = new HashSet<ChatChannel>(
				ChatChannel.values().length);
		int pos = 0;

		// Chat channel subscriptions are stored as a bitset
		// where each bit corresponds to the ordinal chat channel. If
		// a bit is on, then the user is subscribed to that channel.
		int currentChannels = this.chatChannelSubscriptions;
		while (currentChannels != 0) {
			if (currentChannels % 2 == 1) {
				subscriptions.add(ChatChannel.values()[pos]);
			}
			pos++;
			currentChannels >>= 1;
		}

		return subscriptions;
	}

	public void setChatChannelSubscriptions(Set<ChatChannel> channels) {
		int results = 0;

		// Chat channel subscriptions are stored as a bitset
		// where each bit corresponds to the ordinal chat channel. If
		// a bit is on, then the user is subscribed to that channel.
		for (ChatChannel channel : channels) {
			results |= Constants.TWO_POWERS[channel.ordinal()];
		}

		this.chatChannelSubscriptions = results;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public boolean canMove(Direction direction) {
		if (direction == Direction.UP) {
			return this.getMap().isWalkable(x, y - 1);
		} else if (direction == Direction.DOWN) {
			return this.getMap().isWalkable(x, y + 1);
		} else if (direction == Direction.LEFT) {
			return this.getMap().isWalkable(x - 1, y);
		} else {
			return this.getMap().isWalkable(x + 1, y);
		}
	}
}
