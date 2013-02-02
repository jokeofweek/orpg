package orpg.mapeditor;

import java.awt.Font;
import java.io.File;
import java.net.MalformedURLException;

import org.apache.pivot.util.concurrent.TaskExecutionException;
import org.apache.pivot.wtk.BoxPane;
import org.apache.pivot.wtk.ButtonGroup;
import org.apache.pivot.wtk.FillPane;
import org.apache.pivot.wtk.FlowPane;
import org.apache.pivot.wtk.Label;
import org.apache.pivot.wtk.Orientation;
import org.apache.pivot.wtk.RadioButton;
import org.apache.pivot.wtk.ScrollPane;
import org.apache.pivot.wtk.TabPane;
import org.apache.pivot.wtk.TablePane;
import org.apache.pivot.wtk.ScrollPane.ScrollBarPolicy;
import org.apache.pivot.wtk.TablePane.Column;
import org.apache.pivot.wtk.TablePane.Row;
import org.apache.pivot.wtk.media.Image;

import orpg.mapeditor.ui.TilesView;
import orpg.shared.MapLayer;

public class MapEditorPanel extends FillPane {

	public MapEditorPanel() {

		TabPane pickerPane = new TabPane();

		// Set up the title tab table
		TablePane tilesTabTable = new TablePane();
		tilesTabTable.getColumns().add(new Column(1, true));
		tilesTabTable.getStyles().put("verticalSpacing", 10);
		tilesTabTable.getStyles().put("padding", 5);

		// Build the layer header
		BoxPane layerOptionsPane = new BoxPane(Orientation.VERTICAL);
		Label layersHeaderLabel = new Label("Layer:");
		layersHeaderLabel.getStyles().put(
				"font",
				((Font) layersHeaderLabel.getStyles().get("font"))
						.deriveFont(Font.BOLD));
		layerOptionsPane.add(layersHeaderLabel);

		// Build layer options
		FlowPane layersPane = new FlowPane();
		ButtonGroup layerGroup = new ButtonGroup();
		RadioButton layerButton;
		boolean firstLayer = true;
		for (MapLayer layer : MapLayer.values()) {
			layerButton = new RadioButton(layerGroup, layer.getName());
			layerButton.setButtonDataKey(layer.ordinal() + "");

			// Needed to select the first layer
			if (firstLayer) {
				layerGroup.setSelection(layerButton);
				firstLayer = false;
			}

			layersPane.add(layerButton);
		}
		layerOptionsPane.add(layersPane);

		Row row = new Row(-1);
		row.add(layerOptionsPane);
		tilesTabTable.getRows().add(row);

		// Build the tiles view
		TilesView tilesView = null;
		try {
			tilesView = new TilesView(Image.load(new File("gfx/tiles.png")
					.toURI().toURL()));
		} catch (MalformedURLException | TaskExecutionException e) {
			e.printStackTrace();
			System.exit(1);

		}
		ScrollPane tilesScrollPane = new ScrollPane(ScrollBarPolicy.ALWAYS,
				ScrollBarPolicy.ALWAYS);
		tilesScrollPane.setView(tilesView);

		row = new Row(1, true);
		row.add(tilesScrollPane);
		tilesTabTable.getRows().add(row);

		pickerPane.getTabs().add(tilesTabTable);

		FillPane mapPane = new FillPane();

		this.add(pickerPane);
		this.add(mapPane);
	}

}
