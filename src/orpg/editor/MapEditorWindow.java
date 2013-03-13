package orpg.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import orpg.editor.controller.EditorController;
import orpg.editor.controller.MapController;
import orpg.editor.controller.MapEditorController;
import orpg.editor.data.MapEditorTab;
import orpg.editor.map.tool.EntityPencilTool;
import orpg.editor.map.tool.FillTool;
import orpg.editor.map.tool.PencilTool;
import orpg.editor.ui.MapView;
import orpg.editor.ui.TilesView;
import orpg.shared.Constants;
import orpg.shared.Strings;
import orpg.shared.data.Map;
import orpg.shared.data.MapLayer;
import orpg.shared.data.TileFlag;

public class MapEditorWindow extends JFrame implements Observer,
		EditorWindow<Map> {

	private MapEditorController editorController;
	private MapController mapController;

	private JTabbedPane tabPane;
	private JCheckBoxMenuItem gridToggleMenuItem;
	private JCheckBoxMenuItem hoverPreviewToggleMenuItem;
	private JTextField mapNameTextField;

	private BaseEditor baseEditor;

	public MapEditorWindow(BaseEditor baseEditor, MapController mapController) {
		this.baseEditor = baseEditor;
		this.mapController = mapController;
		this.editorController = new MapEditorController(baseEditor, this,
				this.mapController);

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle(Strings.ENGINE_NAME);
		this.setupContent();
		this.setupMenuBar();
		this.pack();
		this.setVisible(true);
		this.setSize(800, 600);
		this.setLocationRelativeTo(null);
		this.requestFocusInWindow();

		this.load(baseEditor);
	}

	private void setupContent() {
		// Load tilesets
		BufferedImage[] tilesets = new BufferedImage[Constants.TILESETS];
		int i = 0;
		try {
			for (i = 0; i < Constants.TILESETS; i++) {
				tilesets[i] = ImageIO.read(new File(
						Constants.CLIENT_ASSETS_PATH + "tiles_" + i + ".png")
						.toURI().toURL());
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Could not load tiles_" + i
					+ ".png. Shutting down.");
			e.printStackTrace();
			System.exit(1);
		}

		BufferedImage loadingTile = null;
		try {
			loadingTile = ImageIO.read(new File(Constants.CLIENT_ASSETS_PATH
					+ "loading_tile.png").toURI().toURL());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"Could not load loading_tile.png. Shutting down.");
			e.printStackTrace();
			System.exit(1);
		}

		JPanel contentPane = new JPanel(new BorderLayout());

		JPanel mapContainer = new JPanel(new BorderLayout());
		contentPane.add(getTabPane(tilesets), BorderLayout.WEST);

		JScrollPane mapScrollPane = new JScrollPane(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		MapView mapView = new MapView(mapController, editorController,
				mapScrollPane, tilesets, loadingTile, baseEditor);
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

	private JComponent getTabPane(Image[] tilesets) {

		this.tabPane = new JTabbedPane();

		tabPane.addTab("Tiles", getTilesTab(tilesets));
		tabPane.addTab("Attributes", getAttributesTab());
		tabPane.addTab("Properties", getPropertiesTab());
		tabPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));

		// Setup the tab change listener by associating an index with a tool
		tabPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				editorController.setCurrentTab(MapEditorTab.values()[tabPane
						.getSelectedIndex()]);
			}
		});

		return tabPane;
	}

	public JComponent getTilesTab(Image[] tilesets) {
		// Set up the tile tab panel
		JPanel tilesTabPanel = new JPanel(new BorderLayout());

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.add(getLayersPanel());
		topPanel.add(getToolsPanel());

		tilesTabPanel.add(topPanel, BorderLayout.NORTH);

		// Build the tiles view
		TilesView tilesView = null;
		tilesView = new TilesView(baseEditor, editorController, tilesets);

		JScrollPane tilesScrollPane = new JScrollPane(tilesView,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		tilesTabPanel.add(tilesScrollPane);
		tilesTabPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		return tilesTabPanel;
	}

	public JComponent getAttributesTab() {
		// Set up the tile tab panel
		JPanel parentPanel = new JPanel(new BorderLayout());

		// Build the attributes header
		JPanel attributeSelectPanel = new JPanel();
		attributeSelectPanel.setLayout(new BoxLayout(attributeSelectPanel,
				BoxLayout.PAGE_AXIS));

		JLabel headerLabel = new JLabel("Attribute:");
		headerLabel.setAlignmentX(LEFT_ALIGNMENT);
		headerLabel.setFont(headerLabel.getFont().deriveFont(Font.BOLD));
		attributeSelectPanel.add(headerLabel);

		JComponent attributesPane = getAttributesPane();
		attributesPane.setAlignmentX(LEFT_ALIGNMENT);
		attributeSelectPanel.add(attributesPane);

		parentPanel.add(attributeSelectPanel, BorderLayout.NORTH);

		parentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		return parentPanel;

	}

	public JComponent getPropertiesTab() {
		JPanel panel = new JPanel(new GridLayout(0, 2));
		panel.add(new JLabel("Name:"));
		mapNameTextField = new JTextField();
		panel.add(mapNameTextField);
		return panel;

	}

	public JComponent getLayersPanel() {
		JPanel parentPanel = new JPanel();
		parentPanel.setLayout(new BoxLayout(parentPanel, BoxLayout.PAGE_AXIS));

		JLabel headerLabel = new JLabel("Layer:");
		headerLabel.setAlignmentX(LEFT_ALIGNMENT);
		headerLabel.setFont(headerLabel.getFont().deriveFont(Font.BOLD));
		parentPanel.add(headerLabel);

		// Build layer options
		JPanel optionsPanel = new JPanel();
		optionsPanel.setAlignmentX(LEFT_ALIGNMENT);

		ItemListener itemListener = new ItemListener() {

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
			layerButton.addItemListener(itemListener);
			layerGroup.add(layerButton);

			// Needed to select the first layer
			if (layer == editorController.getCurrentLayer()) {
				layerGroup.setSelected(layerButton.getModel(), true);
			}

			optionsPanel.add(layerButton);
		}

		parentPanel.add(optionsPanel);

		return parentPanel;
	}

	public JComponent getToolsPanel() {
		JPanel parentPanel = new JPanel();
		parentPanel.setLayout(new BoxLayout(parentPanel, BoxLayout.PAGE_AXIS));

		JLabel headerLabel = new JLabel("Tools:");
		headerLabel.setAlignmentX(LEFT_ALIGNMENT);
		headerLabel.setFont(headerLabel.getFont().deriveFont(Font.BOLD));
		parentPanel.add(headerLabel);

		// Build options
		JPanel optionsPanel = new JPanel();
		optionsPanel.setAlignmentX(LEFT_ALIGNMENT);

		ItemListener itemListener = new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				JRadioButton button = (JRadioButton) e.getSource();
				if (button.isSelected()) {
					if (button.getActionCommand().equals("pencil")) {
						editorController.setCurrentTool(PencilTool
								.getInstance());
					} else if (button.getActionCommand().equals("fill")) {
						editorController.setCurrentTool(FillTool.getInstance());
					} else if (button.getActionCommand()
							.equals("entity-pencil")) {
						editorController.setCurrentTool(EntityPencilTool
								.getInstance());
					}
				}
			}
		};

		ButtonGroup toolGroup = new ButtonGroup();
		JRadioButton toolButton;

		// Pencil tool
		toolButton = new JRadioButton("Pencil");
		toolButton.setActionCommand("pencil");
		toolButton.addItemListener(itemListener);
		toolGroup.add(toolButton);
		toolButton.setSelected(true);
		optionsPanel.add(toolButton);

		// Fill tool
		toolButton = new JRadioButton("Fill");
		toolButton.setActionCommand("fill");
		toolButton.addItemListener(itemListener);
		toolGroup.add(toolButton);
		optionsPanel.add(toolButton);
		
		// Entity pencil tool
		toolButton = new JRadioButton("Entity Pencil");
		toolButton.setActionCommand("entity-pencil");
		toolButton.addItemListener(itemListener);
		toolGroup.add(toolButton);
		optionsPanel.add(toolButton);

		parentPanel.add(optionsPanel);

		return parentPanel;
	}

	public JComponent getAttributesPane() {
		JPanel attributesPanel = new JPanel();

		ItemListener attributeItemListesner = new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				JRadioButton button = (JRadioButton) e.getSource();
				if (button.isSelected()) {
					editorController
							.setCurrentTileFlag(TileFlag.values()[Integer
									.parseInt(button.getActionCommand())]);
				}
			}
		};

		// Setup a radio button for each layer
		final ButtonGroup group = new ButtonGroup();
		JRadioButton button;
		for (TileFlag tileAttribute : TileFlag.values()) {
			button = new JRadioButton(tileAttribute.getName());
			button.setActionCommand(tileAttribute.ordinal() + "");
			button.addItemListener(attributeItemListesner);
			group.add(button);

			// Needed to select the first layer
			if (tileAttribute == editorController.getCurrentTileFlag()) {
				group.setSelected(button.getModel(), true);
			}

			attributesPanel.add(button);
		}

		return attributesPanel;
	}

	private void setupMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		// File menu
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(fileMenu);

		JMenuItem saveItem = new JMenuItem(editorController.getSaveAction());
		saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				KeyEvent.CTRL_DOWN_MASK));
		fileMenu.add(saveItem);

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
			// Update the tab in case an external source changed our tab
			this.tabPane.setSelectedIndex(editorController.getCurrentTab()
					.ordinal());

			this.repaint();

			// Update toggle menu items.
			gridToggleMenuItem.setState(editorController.isGridEnabled());
			hoverPreviewToggleMenuItem.setState(editorController
					.isHoverPreviewEnabled());
		}
	}

	@Override
	public void load(BaseEditor baseEditor) {
		mapNameTextField.setText(mapController.getMapName());
	}

	@Override
	public void beforeSave(BaseEditor baseEditor) {
		mapController.setMapName(mapNameTextField.getText());
	}

	@Override
	public List<String> validate(BaseEditor baseEditor) {
		return null;
	}

}
