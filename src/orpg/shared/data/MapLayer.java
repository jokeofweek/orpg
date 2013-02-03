package orpg.shared.data;

public enum MapLayer {

	// should be kept in order.
	GROUND("Ground"), MASK("Mask"), MASK_2("Mask 2"), FRINGE("Fringe");

	private String name;

	private MapLayer(String name) {
		this.name = name;
	}

	/**
	 * @return the readable name of the layer
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the layer on top of which the player should be rendered.
	 */
	public static MapLayer getLayerBeforePlayer() {
		return MASK_2;
	}

}
