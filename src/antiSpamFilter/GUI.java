/* 
 * 1º Commit
 */
package antiSpamFilter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

public class GUI {

	private JFrame frame = new JFrame("AntiSpamFilter");
	private JPanel nPanel = new JPanel();
	private JPanel cPanel = new JPanel();
	private DefaultTableModel tableModel;
	private JTable table = new JTable(tableModel);
	private JScrollPane scroll = new JScrollPane();
	private ArrayList<String> rules = new ArrayList<String>();
	private JLabel labelPathRules = new JLabel("Path rules");
	private JLabel labelPathSpam = new JLabel("Path spam");
	private JLabel labelPathHam = new JLabel("Path ham");
	private JTextField pathRules = new JTextField("rules.cf");
	private JTextField pathSpam = new JTextField("spam.log");
	private JTextField pathHam = new JTextField("ham.log");

	public static void main(String[] args) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					new GUI();
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public GUI() {
		readFiles();
		addFrameContent();
		frame.setVisible(true);
	}

	private void readFiles() {
		try {
			BufferedReader bf = new BufferedReader(new FileReader(new File("rules.cf")));
			String line;
			while ((line = bf.readLine()) != null) {
				rules.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addFrameContent() {
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setPreferredSize(new Dimension(600, 500));
		frame.pack();

		pathRules.setColumns(5);
		pathRules.setHorizontalAlignment(JTextField.CENTER);
		pathSpam.setColumns(5);
		pathSpam.setHorizontalAlignment(JTextField.CENTER);
		pathHam.setColumns(5);
		pathHam.setHorizontalAlignment(JTextField.CENTER);

		nPanel.add(labelPathRules);
		nPanel.add(pathRules);
		nPanel.add(labelPathSpam);
		nPanel.add(pathSpam);
		nPanel.add(labelPathHam);
		nPanel.add(pathHam);

		tableModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				switch (column) {
				case 0:
					return false;
				case 1:
				default:
					return true;
				}
			}
		};

		tableModel.setColumnIdentifiers(new Object[] { "RULES", "WEIGHTS" });

		table.setModel(tableModel);

		scroll.add(table);
		scroll.setViewportView(table);

		cPanel.add(scroll);

		frame.add(nPanel, BorderLayout.NORTH);
		frame.add(cPanel, BorderLayout.CENTER);
	}

}
