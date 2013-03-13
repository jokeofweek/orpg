package orpg.editor.util;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;

import javax.swing.JList;

import com.l2fprod.common.beans.editor.ComboBoxPropertyEditor;

public class EnumPropertyEditor extends ComboBoxPropertyEditor {

	public EnumPropertyEditor(Class<? extends Enum> enumClass) {
		setAvailableValues(enumClass.getEnumConstants());
	}

}
