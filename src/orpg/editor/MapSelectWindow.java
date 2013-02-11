package orpg.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import orpg.shared.Strings;
import orpg.shared.data.Pair;

public class MapSelectWindow extends JFrame {

	private BaseEditor baseEditor;

	public MapSelectWindow(BaseEditor baseEditor,
			Pair<Integer, String>[] mapNames) {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle(Strings.ENGINE_NAME);
		this.setupContent(mapNames);

		this.setSize(800, 600);
		this.setLocationRelativeTo(null);
		this.setVisible(true);

		this.baseEditor = baseEditor;
		this.requestFocusInWindow();
	}

	private void setupContent(Pair<Integer, String>[] mapNames) {
		JPanel contentPanel = new JPanel(new BorderLayout());

		final JList mapList = new JList(mapNames);
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
				baseEditor.requestEditMap(((Pair<Integer, String>) mapList
						.getSelectedValue()).getFirst());
			}
		});

		contentPanel.add(editButton, BorderLayout.SOUTH);

		add(contentPanel);
	}

	private static class JListMapNameRenderer implements ListCellRenderer {

		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			// TODO Auto-generated method stub
			return null;
		}

	}

}
