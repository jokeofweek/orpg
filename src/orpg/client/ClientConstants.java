package orpg.client;

public final class ClientConstants {

	public static final float WALK_SPEED = 128.0f; // 64 pixels per second

	// Entity groups
	public static final String GROUP_PLAYERS = "players";
	public static final String GROUP_COLLIDEABLE = "collideable";
	public static final String GROUP_MAP = "map";
	public static final String GROUP_MAP_SEGMENT = "map_%d_%d";

	public static final int GAME_WIDTH = 1024;
	public static final int GAME_HEIGHT = 574;

	// Messages to keep in chat history.
	public static final int CHAT_HISTORY = 20;

	// Chat constants
	public static final int CHAT_LINES = 7;
	public static final int CHAT_X = 8;
	public static final int CHAT_Y = GAME_HEIGHT + 20;
	public static final int CHAT_WIDTH = 1016;
	public static final int CHAT_INPUT_HEIGHT = 20;
	public static final int CHAT_INPUT_X = 4;
	public static final int CHAT_INPUT_Y = 743;

	private ClientConstants() {
	}

}
