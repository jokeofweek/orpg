package orpg.server.data;

import orpg.shared.data.Map;

public class MapDescriptor {

	private int id;
	private short segmentsWide;
	private short segmentsHigh;
	private short segmentWidth;
	private short segmentHeight;

	public MapDescriptor(int id, short segmentsWide, short segmentsHigh,
			short segmentWidth, short segmentHeight) {
		super();
		this.id = id;
		this.segmentsWide = segmentsWide;
		this.segmentsHigh = segmentsHigh;
		this.segmentWidth = segmentWidth;
		this.segmentHeight = segmentHeight;
	}

	public MapDescriptor(Map map) {
		this.id = map.getId();
		this.segmentWidth = map.getSegmentWidth();
		this.segmentHeight = map.getSegmentHeight();
		this.segmentsWide = map.getSegmentsWide();
		this.segmentsHigh = map.getSegmentsHigh();
	}

	public int getId() {
		return id;
	}

	public short getSegmentsWide() {
		return segmentsWide;
	}

	public short getSegmentsHigh() {
		return segmentsHigh;
	}

	public short getSegmentWidth() {
		return segmentWidth;
	}

	public short getSegmentHeight() {
		return segmentHeight;
	}

}
