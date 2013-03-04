package orpg.shared.data;

import orpg.shared.Constants;
import orpg.shared.Strings;

public enum TileFlag {

	BLOCKED(Strings.TILE_FLAG_BLOCKED,
			Strings.TILE_FLAG_BLOCKED_DESCRIPTION), NPC_AVOID(
			Strings.TILE_FLAG_NPC_AVOID,
			Strings.TILE_FLAG_NPC_AVOID_DESCRIPTION);

	private String name;
	private String description;
	private int mask;

	private TileFlag(String name, String description) {
		this.name = name;
		this.description = description;
		this.mask = Constants.TWO_POWERS[this.ordinal()];
	}

	/**
	 * @return the readable name of the attribute
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return a description of what the attribute represents
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the mask to apply to a flags bitset to extract this flag
	 */
	public int getMask() {
		return mask;
	}

}
