package orpg.shared.data;

public class MapSaveData {

	private Segment[] segments;
	
	public MapSaveData(Segment[] segments) {
		this.segments = segments;
	}
	
	public Segment[] getSegments() {
		return segments;
	}
	
}
