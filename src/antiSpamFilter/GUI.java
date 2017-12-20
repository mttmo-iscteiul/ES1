/* 
 * This is the version that contains the GUI with only one action listener developed
 * and with the manualInputCalculation available for use.
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
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

public class GUI {

	private JFrame frame = new JFrame("AntiSpamFilter");
	private JTabbedPane tabs = new JTabbedPane();
	private JPanel nPanel = new JPanel();
	private JPanel editablePanel = new JPanel();
	private JPanel nonEditablePanel = new JPanel();
	private JPanel sPanel = new JPanel();
	private DefaultTableModel editable;
	private DefaultTableModel nonEditable;
	private JTable editableTable = new JTable(editable);
	private JTable nonEditableTable = new JTable(nonEditable);
	private JScrollPane editableScroll = new JScrollPane();
	private JScrollPane nonEditableScroll = new JScrollPane();
	private ArrayList<String> rules = new ArrayList<String>();
	private JLabel labelPathRules = new JLabel("Path rules");
	private JLabel labelPathSpam = new JLabel("Path spam");
	private JLabel labelPathHam = new JLabel("Path ham");
	private JButton generate = new JButton("Auto Config");
	private JTextField pathRules = new JTextField("rules.cf");
	private JTextField pathSpam = new JTextField("spam.log");
	private JTextField pathHam = new JTextField("ham.log");
	private JButton calculate = new JButton("Calculate");
	private JLabel fpLabel = new JLabel("FP");
	private JLabel fnLabel = new JLabel("FN");
	private JTextField fp = new JTextField();
	private JTextField fn = new JTextField();

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
			bf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addFrameContent() {
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setPreferredSize(new Dimension(520, 575));
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

		editable = new DefaultTableModel() {
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

		nonEditable = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		editable.setColumnIdentifiers(new Object[] { "RULES", "WEIGHTS" });
		nonEditable.setColumnIdentifiers(new Object[] { "RULES", "WEIGHTS" });
		for (String rule : rules) {
			nonEditable.addRow(new Object[] { rule, 0.0 });
			editable.addRow(new Object[] { rule, 0.0 });
		}

		editableTable.setModel(editable);
		nonEditableTable.setModel(nonEditable);

		editableScroll.add(editableTable);
		editableScroll.setViewportView(editableTable);
		nonEditableScroll.add(nonEditableTable);
		nonEditableScroll.setViewportView(nonEditableTable);

		editablePanel.add(editableScroll);
		nonEditablePanel.add(nonEditableScroll);

		tabs.add("Automatic Input Table", nonEditablePanel);
		tabs.add("Manual Input Table", editablePanel);

		tabs.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				tabbedChangedAction(arg0);
			}
		});

		generate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				generateAction();
			}
		});

		calculate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				calculateAction();
			}
		});

		fp.setColumns(3);
		fp.setHorizontalAlignment(JTextField.CENTER);
		fn.setColumns(3);
		fn.setHorizontalAlignment(JTextField.CENTER);

		sPanel.add(generate);
		sPanel.add(fpLabel);
		sPanel.add(fp);
		sPanel.add(fnLabel);
		sPanel.add(fn);
		sPanel.add(calculate);

		frame.add(nPanel, BorderLayout.NORTH);
		frame.add(tabs, BorderLayout.CENTER);
		frame.add(sPanel, BorderLayout.SOUTH);
	}

	private void tabbedChangedAction(ChangeEvent e) {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				JTabbedPane pane = (JTabbedPane) e.getSource();
				if (pane.getSelectedIndex() == 1)
					generate.setText("Random Config");
				else
					generate.setText("Auto Config");
				return null;
			}
		};
		worker.execute();
	}

	private void generateAction() {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				if (tabs.getSelectedIndex() == 0) {
					generate.setEnabled(false);
					System.out.println("It is suposed to process the weights");
					Thread.currentThread().sleep(3000);
					generate.setEnabled(true);
				} else {
					for (String str: rules) {
						double weight = ((int) (Math.random() * (5.0 - -5.0))) + -5.0;
						editable.setValueAt(weight, rules.indexOf(str), 1);
					}
				}
				return null;
			}
		};
		worker.execute();
	}

	private void calculateAction() {
		SwingWorker<String[], Void> worker = new SwingWorker<String[], Void>() {
			@Override
			protected String[] doInBackground() throws Exception {
				calculate.setEnabled(false);
				System.out.println("Calculate FP &b FN using current table");
				ManualInputCalculation mic = new ManualInputCalculation(editable);
				String[] fpn = new String[2];
				fpn[0] = mic.getFp();
				fpn[1] = mic.getFn();
				calculate.setEnabled(true);
				return fpn;
			}

			@Override
			public void done() {
				try {
					String[] fpn = get();
					fp.setText(fpn[0]);
					fn.setText(fpn[1]);
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}

			}
		};
		worker.execute();
	}

}
