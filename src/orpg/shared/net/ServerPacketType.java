package orpg.shared.net;

public enum ServerPacketType {
	PONG,
	HELLO,
	GOODBYE,
	CONNECTED,
	DISCONNECT,
	ERROR,
	LOGIN_OK,
	EDITOR_LOGIN_OK,
	
	// Editor packet types
	EDITOR_MAP_LIST,
	EDITOR_MAP_DATA,
}
