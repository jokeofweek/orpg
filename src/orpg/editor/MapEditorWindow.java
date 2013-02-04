package orpg.editor;

import java.awt.Font;
import java.io.File;
import java.net.MalformedURLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.apache.pivot.util.concurrent.TaskExecutionException;
import org.apache.pivot.wtk.BoxPane;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonGroup;
import org.apache.pivot.wtk.ButtonGroupListener;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.FlowPane;
import org.apache.pivot.wtk.Frame;
import org.apache.pivot.wtk.Keyboard.KeyStroke;
import org.apache.pivot.wtk.Label;
import org.apache.pivot.wtk.MenuBar;
import org.apache.pivot.wtk.Orientation;
import org.apache.pivot.wtk.RadioButton;
import org.apache.pivot.wtk.ScrollPane;
import org.apache.pivot.wtk.ScrollPane.ScrollBarPolicy;
import org.apache.pivot.wtk.TabPane;
import org.apache.pivot.wtk.TablePane;
import org.apache.pivot.wtk.TablePane.Column;
import org.apache.pivot.wtk.TablePane.Row;
import org.apache.pivot.wtk.Window.ActionMapping;
import org.apache.pivot.wtk.media.Image;

import orpg.editor.controller.MapController;
import orpg.editor.controller.MapEditorController;
import orpg.editor.ui.MapEditorMenuBar;
import orpg.editor.ui.MapView;
import orpg.editor.ui.TilesView;
import orpg.shared.data.Map;
import orpg.shared.data.MapLayer;

public class MapEditorWindow extends BasicWindow implements Observer {

	private MapEditorController editorController;
	private MapController mapController;
	private TablePane content;
	private MenuBar menuBar;

	public MapEditorWindow(WindowManager windowManager) {
		super(windowManager);
		Map map = new Map(100, 100);

		this.editorController = new MapEditorController();
		this.mapController = new MapController(map);

		this.setupContent();
		this.setupMenuBar();
	}

	private void setupContent() {
		content = new TablePane();
		content.getColumns().add(new Column(-1));
		content.getColumns().add(new Column(1, true));

		Row row = new Row(1, true);
		row.add(getTabPane());

		MapView mapView = new MapView(mapController, editorController);
		ScrollPane mapScrollPane = new ScrollPane(ScrollBarPolicy.ALWAYS,
				ScrollBarPolicy.ALWAYS);
		mapScrollPane.setView(mapView);
		row.add(mapScrollPane);

		content.getRows().add(row);
		content.getStyles().put("padding", 5);
		content.getStyles().put("horizontalSpacing", 5);
	}

	private Component getTabPane() {
		TabPane tabPane = new TabPane();

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

		layerOptionsPane.add(getLayersPane());

		Row row = new Row(-1);
		row.add(layerOptionsPane);
		tilesTabTable.getRows().add(row);

		// Build the tiles view
		TilesView tilesView = null;
		try {
			tilesView = new TilesView(editorController,
					Image.load(new File("gfx/tiles.png").toURI().toURL()));
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (TaskExecutionException e) {
			e.printStackTrace();
			System.exit(1);
		}
		ScrollPane tilesScrollPane = new ScrollPane(
				ScrollBarPolicy.ALWAYS, ScrollBarPolicy.ALWAYS);
		tilesScrollPane.setView(tilesView);

		row = new Row(1, true);
		row.add(tilesScrollPane);
		tilesTabTable.getRows().add(row);

		tabPane.getTabs().add(tilesTabTable);

		return tabPane;
	}

	public Component getLayersPane() {
		FlowPane layersPane = new FlowPane();
		ButtonGroup layerGroup = new ButtonGroup();
		RadioButton layerButton;
		for (MapLayer layer : MapLayer.values()) {
			layerButton = new RadioButton(layerGroup, layer.getName());
			layerButton.setButtonDataKey(layer.ordinal() + "");

			// Needed to select the first layer
			if (layer == editorController.getCurrentLayer()) {
				layerGroup.setSelection(layerButton);
			}

			layersPane.add(layerButton);
		}

		layerGroup.getButtonGroupListeners().add(
				new ButtonGroupListener() {

					@Override
					public void selectionChanged(ButtonGroup buttonGroup,
							Button previousSelection) {
						editorController.setCurrentLayer(MapLayer.values()[Integer
								.parseInt(buttonGroup.getSelection()
										.getButtonDataKey())]);
					}

					@Override
					public void buttonRemoved(ButtonGroup buttonGroup,
							Button button) {
					}

					@Override
					public void buttonAdded(ButtonGroup buttonGroup,
							Button button) {
					}
				});

		return layersPane;
	}

	private void setupMenuBar() {
		this.menuBar = new MapEditorMenuBar(editorController,
				mapController);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o == editorController) {
			content.repaint();
		}
	}

	@Override
	public void enter(Frame applicationFrame) {}


	@Override
	public Component getContent() {
		return content;
	}
	
	@Override
	public MenuBar getMenuBar() {
		return this.menuBar;
	}
	
	@Override
	public List<ActionMapping> getActionMappings() {
		List<ActionMapping> actionMappings = new ArrayList<ActionMapping>(2);
		actionMappings.add(new ActionMapping(KeyStroke.decode(KeyStroke.COMMAND_ABBREVIATION + "-y"), editorController.getRedoAction()));
		actionMappings.add(new ActionMapping(KeyStroke.decode(KeyStroke.COMMAND_ABBREVIATION + "-z"), editorController.getUndoAction()));
		return actionMappings;
	}
	
	@Override
	public void exit(Frame applicationFrame) {}

}
