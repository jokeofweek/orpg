package orpg.shared.net;

public enum ClientPacketType {

	PING,
	CREATE_ACCOUNT,
	LOGIN,
	
	USE_CHARACTER,
	
	CLIENT_LOAD_MAP,

	// Editor packets
	EDITOR_READY,
	EDITOR_EDIT_MAP,
	EDITOR_SAVE_MAP,
	EDITOR_LOAD_MAP,
	EDITOR_REQUEST_SEGMENT
	
}
