package orpg.shared.net;

public enum ServerPacketType {
	PONG,
	HELLO,
	GOODBYE,
	CONNECTED,
	DISCONNECT,
	ERROR,
	EDITOR_LOGIN_OK,
	
	// Editor packet types
	EDITOR_MAP_LIST,
	EDITOR_MAP_DATA,
}
