package orpg.client;

public final class ClientConstants {

	public static final float WALK_SPEED = 128.0f; // 64 pixels per second

	// Entity groups
	public static final String GROUP_PLAYERS = "players";
	public static final String GROUP_COLLIDEABLE = "collideable";
	public static final String GROUP_MAP = "map";
	public static final String GROUP_MAP_SEGMENT = "map_%d_%d";
	
	private ClientConstants() {}
	
}
