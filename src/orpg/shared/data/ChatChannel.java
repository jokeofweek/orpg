package orpg.shared.data;

public enum ChatChannel {

	// Note there cannot be more than 127 chat channels.

	LOCAL(""), GOSSIP("Gossip"), HELP("Help");

	private String name;

	private ChatChannel(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
