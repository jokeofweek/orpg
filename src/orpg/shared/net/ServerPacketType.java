package orpg.shared.net;

public enum ServerPacketType {
	// Pre-login packets
	PONG,
	HELLO,
	GOODBYE,
	CONNECTED,
	DISCONNECT,
	ERROR,
	LOGIN_OK,
	EDITOR_LOGIN_OK,
	
	// Shared packet types
	SHARED_AUTO_TILE_LIST,
	
	// Client packet types
	CLIENT_IN_GAME,
	CLIENT_NEW_MAP,
	CLIENT_SEGMENT_DATA,
	CLIENT_JOIN_MAP,
	CLIENT_LEFT_MAP,
	CLIENT_MOVE,
	CLIENT_SYNC_ENTITY_PROPERTIES,
	CLIENT_MESSAGE,
	
	// Editor packet types
	EDITOR_MAP_LIST,
	EDITOR_MAP_DATA,
	EDITOR_MAP_SEGMENT_DATA
}
