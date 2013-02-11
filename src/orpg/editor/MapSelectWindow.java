package orpg.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import orpg.shared.Strings;

public class MapSelectWindow extends JFrame {

	private BaseEditor baseEditor;

	public MapSelectWindow(BaseEditor baseEditor, int totalMaps) {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle(Strings.ENGINE_NAME);
		this.setupContent(totalMaps);
		
		this.setSize(800, 600);
		this.setLocationRelativeTo(null);
		this.setVisible(true);

		this.baseEditor = baseEditor;
		this.requestFocusInWindow();
	}

	private void setupContent(int totalMaps) {
		JPanel contentPanel = new JPanel(new BorderLayout());

		Integer[] maps = new Integer[totalMaps];
		for (int i = 0; i < totalMaps; i++) {
			maps[i] = (i + 1);
		}
		final JList mapList = new JList(maps);
		JScrollPane mapListScrollPane = new JScrollPane(mapList,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		mapListScrollPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(5, 5, 5, 5),
				BorderFactory.createLineBorder(Color.black, 1)));
		contentPanel.add(mapListScrollPane);
	
		JButton editButton = new JButton(Strings.EDIT_COMMAND);
		editButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				baseEditor.requestEditMap((Integer) mapList.getSelectedValue());
			}
		});
		
		contentPanel.add(editButton, BorderLayout.SOUTH);
		
		add(contentPanel);
	}

}
