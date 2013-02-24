package orpg.shared.data;

public enum TileAttribute {

	BLOCKED("Blocked"), NPC_AVOID("NPC Avoid");

	private String name;

	private TileAttribute(String name) {
		this.name = name;
	}

	/**
	 * @return the readable name of the attribute
	 */
	public String getName() {
		return this.name;
	}

}
