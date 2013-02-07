package orpg.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import orpg.editor.controller.MapController;
import orpg.editor.controller.MapEditorController;
import orpg.editor.ui.MapView;
import orpg.editor.ui.TilesView;
import orpg.shared.Strings;
import orpg.shared.data.Map;
import orpg.shared.data.MapLayer;

public class MapEditorWindow extends JFrame implements Observer {

	private MapEditorController editorController;
	private MapController mapController;

	private JCheckBoxMenuItem gridToggleMenuItem;
	private JCheckBoxMenuItem hoverPreviewToggleMenuItem;

	public MapEditorWindow() {
		Map map = new Map(100, 100);

		this.editorController = new MapEditorController();
		this.mapController = new MapController(map);

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle(Strings.ENGINE_NAME);
		this.setupContent();
		this.setupMenuBar();
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setSize(800, 600);
		this.requestFocusInWindow();
	}

	private void setupContent() {
		JPanel contentPane = new JPanel(new BorderLayout());

		JPanel mapContainer = new JPanel(new BorderLayout());
		contentPane.add(getTabPane(), BorderLayout.WEST);

		JScrollPane mapScrollPane = new JScrollPane(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		MapView mapView = new MapView(mapController, editorController,
				mapScrollPane);
		mapScrollPane.setViewportView(mapView);
		mapScrollPane.getHorizontalScrollBar().setUnitIncrement(16);
		mapScrollPane.getHorizontalScrollBar().setAutoscrolls(true);
		mapScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		mapScrollPane.setBackground(Color.black);
		mapContainer.add(mapScrollPane);
		mapContainer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		contentPane.add(mapContainer);
		this.add(contentPane);
	}

	private JComponent getTabPane() {

		JTabbedPane tabPane = new JTabbedPane();

		// Set up the tile tab panel
		JPanel tilesTabPanel = new JPanel(new BorderLayout());

		// Build the layer header
		JPanel layerOptionsPane = new JPanel();
		layerOptionsPane.setLayout(new BoxLayout(layerOptionsPane,
				BoxLayout.PAGE_AXIS));

		JLabel layersHeaderLabel = new JLabel("Layer:");
		layersHeaderLabel.setAlignmentX(LEFT_ALIGNMENT);
		layersHeaderLabel.setFont(layersHeaderLabel.getFont().deriveFont(
				Font.BOLD));
		layerOptionsPane.add(layersHeaderLabel);

		// Build layer options
		JComponent layersPane = getLayersPane();
		layersPane.setAlignmentX(LEFT_ALIGNMENT);
		layerOptionsPane.add(layersPane);

		tilesTabPanel.add(layerOptionsPane, BorderLayout.NORTH);

		// Build the tiles view
		TilesView tilesView = null;
		try {
			tilesView = new TilesView(editorController, ImageIO.read(new File(
					"gfx/tiles.png").toURI().toURL()));
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JScrollPane tilesScrollPane = new JScrollPane(tilesView,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		tilesTabPanel.add(tilesScrollPane);
		tabPane.addTab("Tiles", tilesTabPanel);
		tabPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));

		return tabPane;
	}

	public JComponent getLayersPane() {
		JPanel layersPanel = new JPanel();

		ItemListener layerItemListener = new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				JRadioButton button = (JRadioButton) e.getSource();
				if (button.isSelected()) {
					editorController.setCurrentLayer(MapLayer.values()[Integer
							.parseInt(button.getActionCommand())]);
				}
			}
		};

		// Setup a radio button for each layer
		final ButtonGroup layerGroup = new ButtonGroup();
		JRadioButton layerButton;
		for (MapLayer layer : MapLayer.values()) {
			layerButton = new JRadioButton(layer.getName());
			layerButton.setActionCommand(layer.ordinal() + "");
			layerButton.addItemListener(layerItemListener);
			layerGroup.add(layerButton);

			// Needed to select the first layer
			if (layer == editorController.getCurrentLayer()) {
				layerGroup.setSelected(layerButton.getModel(), true);
			}

			layersPanel.add(layerButton);
		}

		return layersPanel;
	}

	private void setupMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		// File menu
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(fileMenu);

		// Edit menu
		JMenu editMenu = new JMenu("Edit");
		editMenu.setMnemonic(KeyEvent.VK_E);
		menuBar.add(editMenu);

		JMenuItem undoItem = new JMenuItem(editorController.getUndoAction());
		undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				KeyEvent.CTRL_DOWN_MASK));
		editMenu.add(undoItem);

		JMenuItem redoItem = new JMenuItem(editorController.getRedoAction());
		redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,
				KeyEvent.CTRL_DOWN_MASK));
		editMenu.add(redoItem);

		// View menu
		JMenu viewMenu = new JMenu("View");
		viewMenu.setMnemonic(KeyEvent.VK_V);
		menuBar.add(viewMenu);

		JMenuItem zoomInItem = new JMenuItem(editorController.getZoomInAction());
		zoomInItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS,
				KeyEvent.CTRL_DOWN_MASK));

		viewMenu.add(zoomInItem);

		JMenuItem zoomOutItem = new JMenuItem(
				editorController.getZoomOutAction());
		zoomOutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS,
				KeyEvent.CTRL_DOWN_MASK));
		viewMenu.add(zoomOutItem);

		gridToggleMenuItem = new JCheckBoxMenuItem("Grid",
				editorController.isGridEnabled());
		viewMenu.add(gridToggleMenuItem);
		gridToggleMenuItem.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				editorController.setGridEnabled(gridToggleMenuItem.getState());
			}
		});

		hoverPreviewToggleMenuItem = new JCheckBoxMenuItem("Mouse Preview",
				editorController.isHoverPreviewEnabled());
		viewMenu.add(hoverPreviewToggleMenuItem);
		hoverPreviewToggleMenuItem.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				editorController
						.setHoverPreviewEnabled(hoverPreviewToggleMenuItem
								.getState());
			}
		});

		this.setJMenuBar(menuBar);

	}

	@Override
	public void update(Observable o, Object arg) {
		if (o == editorController) {
			this.repaint();

			// Update toggle menu items.
			gridToggleMenuItem.setState(editorController.isGridEnabled());
			hoverPreviewToggleMenuItem.setState(editorController
					.isHoverPreviewEnabled());
		}
	}
}
