package orpg.editor.data;

public class TileRange implements Cloneable {

	private int startX;
	private int startY;
	private int endX;
	private int endY;
	
	public TileRange() {
		
	}
	
	public TileRange(TileRange t) {
		this.startX = t.getStartX();
		this.startY = t.getStartY();
		this.endX = t.getEndX();
		this.endY = t.getEndY();
	}
	
	public int getStartX() {
		return startX;
	}
	public void setStartX(int startX) {
		this.startX = startX;
	}
	public int getStartY() {
		return startY;
	}
	public void setStartY(int startY) {
		this.startY = startY;
	}
	public int getEndX() {
		return endX;
	}
	public void setEndX(int endX) {
		this.endX = endX;
	}
	public int getEndY() {
		return endY;
	}
	public void setEndY(int endY) {
		this.endY = endY;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new TileRange(this);
	}
	
}
