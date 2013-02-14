package orpg.shared.net;

public enum ClientPacketType {

	PING,
	CREATE_ACCOUNT,
	LOGIN,
	EDITOR_LOGIN,

	// Editor packets
	EDITOR_READY,
	EDITOR_EDIT_MAP,
	EDITOR_SAVE_MAP,
	EDITOR_MAP_LOAD
	
}
