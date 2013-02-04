package orpg.editor.ui;

import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.KeyEvent;
import java.security.KeyStore;
import java.util.Observable;
import java.util.Observer;

import org.apache.pivot.wtk.Action;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Keyboard.KeyStroke;
import org.apache.pivot.wtk.Menu;
import org.apache.pivot.wtk.Menu.Section;
import org.apache.pivot.wtk.MenuBar;
import org.apache.pivot.wtk.Window;
import org.apache.pivot.wtk.content.ButtonData;
import org.apache.pivot.wtk.content.MenuItemData;

import orpg.editor.controller.MapController;
import orpg.editor.controller.MapEditorController;

public class MapEditorMenuBar extends MenuBar {

	private MapEditorController editorController;
	private MapController mapController;
	private Menu.Item redoMenuItem;
	private Menu.Item undoMenuItem;

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
		
		redoMenuItem = new Menu.Item(new MenuItemData("Redo"));
		((MenuItemData)redoMenuItem.getButtonData()).setKeyboardShortcut(KeyStroke.COMMAND_ABBREVIATION + "-y");
		redoMenuItem.setAction(editorController.getRedoAction());
		section.add(redoMenuItem);

		undoMenuItem = new Menu.Item(new MenuItemData("Undo"));
		((MenuItemData)undoMenuItem.getButtonData()).setKeyboardShortcut(KeyStroke.COMMAND_ABBREVIATION + "-z");
		undoMenuItem.setAction(editorController.getUndoAction());
		section.add(undoMenuItem);

		menu.getSections().add(section);
		return menu;
	}
}
