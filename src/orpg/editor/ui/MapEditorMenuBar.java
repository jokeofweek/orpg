package orpg.editor.ui;

import java.util.Observable;
import java.util.Observer;

import org.apache.pivot.wtk.Action;
import org.apache.pivot.wtk.Menu;
import org.apache.pivot.wtk.Menu.Section;
import org.apache.pivot.wtk.MenuBar;
import org.apache.pivot.wtk.Window;

import orpg.editor.controller.MapController;
import orpg.editor.controller.MapEditorController;

public class MapEditorMenuBar extends MenuBar implements Observer {

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
		menuItem.setMenu(getFileMenu());
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
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

}
