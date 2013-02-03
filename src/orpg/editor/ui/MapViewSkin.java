package orpg.editor.ui;

import java.awt.Color;
import java.awt.Graphics2D;

import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.skin.ComponentSkin;

import orpg.editor.controller.MapController;
import orpg.shared.Constants;
import orpg.shared.data.MapLayer;

public class MapViewSkin extends ComponentSkin {
	
	@Override
	public void layout() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getPreferredHeight(int height) {
		return ((MapView)getComponent()).getHeight();
	}

	@Override
	public int getPreferredWidth(int width) {
		return ((MapView)getComponent()).getWidth();
	}

	@Override
	public void paint(Graphics2D graphics) {
		
	}


}
