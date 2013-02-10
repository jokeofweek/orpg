package orpg.shared.net;

public enum ClientPacketType {

	PING,
	EDITOR_LOGIN,

	// Editor packets
	EDITOR_READY,
	EDITOR_MAP_SAVE,
	EDITOR_MAP_LOAD
	
}
