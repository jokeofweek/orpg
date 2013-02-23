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
		int segmentX = mapController.getSegmentX(scrollPane
				.getHorizontalScrollBar().getValue()
				/ (Constants.TILE_WIDTH / editorController.getScaleFactor()));
		int segmentY = mapController.getSegmentY(scrollPane
				.getVerticalScrollBar().getValue()
				/ (Constants.TILE_HEIGHT / editorController.getScaleFactor()));
		mapController.getRequestManager().requestSegment(segmentX, segmentY);
	}

}
