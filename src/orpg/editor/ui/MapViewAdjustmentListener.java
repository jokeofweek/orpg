package orpg.editor.ui;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JScrollPane;

import orpg.editor.controller.MapController;
import orpg.editor.controller.MapEditorController;
import orpg.shared.Constants;

public class MapViewAdjustmentListener implements AdjustmentListener {

	private MapController mapController;
	private MapEditorController editorController;
	private JScrollPane scrollPane;

	public MapViewAdjustmentListener(MapController mapController,
			MapEditorController editorController, JScrollPane scrollPane) {
		this.mapController = mapController;
		this.editorController = editorController;
		this.scrollPane = scrollPane;
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		int tileWidth = (Constants.TILE_WIDTH / editorController
				.getScaleFactor());
		int tileHeight = (Constants.TILE_HEIGHT / editorController
				.getScaleFactor());

		int segmentX = mapController.getSegmentX(scrollPane
				.getHorizontalScrollBar().getValue() / tileWidth);
		int segmentY = mapController.getSegmentY(scrollPane
				.getVerticalScrollBar().getValue() / tileHeight);

		int endSegmentX = (scrollPane.getHorizontalScrollBar().getValue() + (int) scrollPane
				.getSize().getWidth()) / tileWidth;
		int endSegmentY = (scrollPane.getVerticalScrollBar().getValue() + (int) scrollPane
				.getSize().getHeight()) / tileHeight;

		// Don't want end segment to pass map width/height
		endSegmentX = Math.min(endSegmentX, mapController.getMapWidth() - 1);
		endSegmentY = Math.min(endSegmentY, mapController.getMapHeight() - 1);

		endSegmentX = mapController.getSegmentX(endSegmentX);
		endSegmentY = mapController.getSegmentY(endSegmentY);

		for (int x = segmentX; x <= endSegmentX; x++) {
			for (int y = segmentY; y <= endSegmentY; y++) {
				mapController.getRequestManager().requestSegment(x, y);
			}
		}
	}
}
