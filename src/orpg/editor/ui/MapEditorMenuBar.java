package orpg.editor.ui;

import org.apache.pivot.wtk.Keyboard.KeyStroke;
import org.apache.pivot.wtk.Menu;
import org.apache.pivot.wtk.Menu.Section;
import org.apache.pivot.wtk.MenuBar;
import org.apache.pivot.wtk.content.MenuItemData;

import orpg.editor.controller.MapController;
import orpg.editor.controller.MapEditorController;

public class MapEditorMenuBar extends MenuBar {

	private MapEditorController editorController;
	private MapController mapController;

	public MapEditorMenuBar(MapEditorController editorController,
			MapController mapController) {
		this.editorController = editorController;
		this.mapController = mapController;

		MenuBar.Item menuItem = new MenuBar.Item("File");
		menuItem.setMenu(getFileMenu());
		this.getItems().add(menuItem);

		menuItem = new MenuBar.Item("Edit");
		menuItem.setMenu(getEditMenu());
		this.getItems().add(menuItem);

		menuItem = new MenuBar.Item("View");
		menuItem.setMenu(getViewMenu());
		this.getItems().add(menuItem);

	}

	public Menu getFileMenu() {
		Menu menu = new Menu();
		Menu.Section section = new Section();

		Menu.Item openItem = new Menu.Item("Open");
		section.add(openItem);

		menu.getSections().add(section);
		return menu;
	}

	public Menu getEditMenu() {
		Menu menu = new Menu();
		Menu.Section section = new Section();

		// Setup the redo menu item
		Menu.Item redoMenuItem = new Menu.Item(new MenuItemData("Redo"));
		((MenuItemData) redoMenuItem.getButtonData())
				.setKeyboardShortcut(KeyStroke.COMMAND_ABBREVIATION + "-Y");
		redoMenuItem.setAction(editorController.getRedoAction());
		section.add(redoMenuItem);

		// Setup the undo menu item
		Menu.Item undoMenuItem = new Menu.Item(new MenuItemData("Undo"));
		((MenuItemData) undoMenuItem.getButtonData())
				.setKeyboardShortcut(KeyStroke.COMMAND_ABBREVIATION + "-Z");
		undoMenuItem.setAction(editorController.getUndoAction());
		section.add(undoMenuItem);

		menu.getSections().add(section);
		return menu;
	}

	public Menu getViewMenu() {
		Menu menu = new Menu();
		Menu.Section section = new Section();

		// Setup the zoom in menu item
		Menu.Item zoomInMenuItem = new Menu.Item(new MenuItemData("Zoom In"));
		((MenuItemData) zoomInMenuItem.getButtonData())
				.setKeyboardShortcut(KeyStroke.COMMAND_ABBREVIATION + "-EQUALS");
		zoomInMenuItem.setAction(editorController.getZoomInAction());
		section.add(zoomInMenuItem);

		// Set up the zoom out menu item
		Menu.Item zoomOutMenuItem = new Menu.Item(new MenuItemData("Zoom Out"));
		((MenuItemData) zoomOutMenuItem.getButtonData())
				.setKeyboardShortcut(KeyStroke.COMMAND_ABBREVIATION + "-MINUS");
		zoomOutMenuItem.setAction(editorController.getZoomOutAction());
		section.add(zoomOutMenuItem);

		menu.getSections().add(section);
		return menu;
	}
}
