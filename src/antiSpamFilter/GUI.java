/* 
 * This version has another class that works behind the GUI class. GUI ready
 */

package antiSpamFilter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

/**
 * This Class is the Graphical User Interface of the project.
 * 
 * @author ES1-2017-IC1-69
 */
public class GUI {

	private GUI_Worker g_Worker;
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
	private JLabel labelPathRules = new JLabel("Path rules");
	private JLabel labelPathSpam = new JLabel("Path spam");
	private JLabel labelPathHam = new JLabel("Path ham");
	private JButton generate = new JButton("Auto Config");
	private JTextField pathRules = new JTextField();
	private JTextField pathSpam = new JTextField();
	private JTextField pathHam = new JTextField();
	private JButton calculate = new JButton("Calculate");
	private JButton saveAuto = new JButton("Save Auto.");
	private JButton saveMan = new JButton("Save Man.");
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

	/**
	 * This method is used to construct from scratch the specified Graphical
	 * User Interface
	 */
	public GUI() {
		startGUI_Worker();
		addFrameContent();
		addGUI_WorkerContent();
		frame.setVisible(true);
		// NSGAIIReady();
		// g_Worker.defaultPath();
	}

	/**
	 * This method "builds" the Graphical User Interface with all the necessary
	 * Swing components
	 */
	@SuppressWarnings("serial")
	private void addFrameContent() {
		frame.setLayout(new BorderLayout());
		frame.setPreferredSize(new Dimension(575, 575));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();

		pathRules.setColumns(5);
		pathRules.setHorizontalAlignment(JTextField.CENTER);
		pathRules.setText(g_Worker.getRulesFile());
		pathRules.setEditable(false);
		pathSpam.setColumns(5);
		pathSpam.setHorizontalAlignment(JTextField.CENTER);
		pathSpam.setText(g_Worker.getSpamFile());
		pathSpam.setEditable(false);
		pathHam.setColumns(5);
		pathHam.setHorizontalAlignment(JTextField.CENTER);
		pathHam.setText(g_Worker.getHamFile());
		pathHam.setEditable(false);

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

		editableTable.getModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent arg0) {
				if (tabs.getSelectedIndex() == 1) {
					if (generate.isEnabled()) {
						int key = editableTable.getSelectedRow();
						String rule = (String) editableTable.getValueAt(key, 0);
						String weight = (String) editableTable.getValueAt(key, 1);
						g_Worker.updateMapSingleValue(rule, Double.parseDouble(weight));
					}
				}
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

		saveAuto.setEnabled(false);
		saveAuto.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveAutoFile();
			}
		});

		saveMan.setEnabled(false);
		saveMan.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveManFile();
			}
		});

		fp.setColumns(3);
		fp.setHorizontalAlignment(JTextField.CENTER);
		fn.setColumns(3);
		fn.setHorizontalAlignment(JTextField.CENTER);

		sPanel.add(saveAuto);
		sPanel.add(generate);
		sPanel.add(fpLabel);
		sPanel.add(fp);
		sPanel.add(fnLabel);
		sPanel.add(fn);
		sPanel.add(calculate);
		sPanel.add(saveMan);

		frame.add(nPanel, BorderLayout.NORTH);
		frame.add(tabs, BorderLayout.CENTER);
		frame.add(sPanel, BorderLayout.SOUTH);
	}

	private void startGUI_Worker() {
		this.g_Worker = new GUI_Worker();
	}

	private void addGUI_WorkerContent() {
		for (String rule : g_Worker.getRules().keySet()) {
			editable.addRow(new Object[] { rule, g_Worker.getRules().get(rule) });
			nonEditable.addRow(new Object[] { rule, g_Worker.getRules().get(rule) });
		}
	}

	/**
	 * This is the Swing Worker that will be responsible for "flagging" the
	 * program, every time the tabs are switched with one another. Also
	 * disenable the generate JButton.
	 * 
	 * @param This
	 *            parameter is used to get the source of the event, to let the
	 *            program know which tabs was selected.
	 */
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

	/**
	 * This is the Swing Worker that will be responsible for handling the
	 * generate JButton, which is the trigger to the jMetall to run.
	 * 
	 */
	private void generateAction() {
		SwingWorker<Integer, Void> worker = new SwingWorker<Integer, Void>() {
			@Override
			protected Integer doInBackground() throws Exception {
				generate.setEnabled(false);
				int j = 0;
				if (tabs.getSelectedIndex() == 0) {
					new AntiSpamFilterAutomaticConfiguration(g_Worker);
					g_Worker.setNewConfiguration();
				} else {
					double[] rWeights = new double[335];
					for (int i = 0; i < rWeights.length; i++) {
						double weight = ((int) (Math.random() * (5.0 - -5.0))) + -5.0;
						rWeights[i] = weight;
					}
					g_Worker.updateMapByVector(rWeights, 1);
					j = 1;
				}
	
				return j;
			}

			@Override
			public void done() {
				try {
					int opt = get();
					switch (opt) {
					case 0:
						int i = 0;
						for (String rule : g_Worker.getRules().keySet()) {
							editable.setValueAt(g_Worker.getRules().get(rule), i, 1);
							nonEditable.setValueAt(g_Worker.getRules().get(rule), i, 1);
							i++;
						}
						saveAuto.setEnabled(true);
						break;
					case 1:
						int j = 0;
						for(String rule: g_Worker.getManualConfig().keySet()) {
							editable.setValueAt(g_Worker.getManualConfig().get(rule), j, 1);
							j++;
						}
						saveMan.setEnabled(true);
						break;
					}
					generate.setEnabled(true);
					
				} catch(InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
				
			}
		};
		worker.execute();
	}

	/**
	 * This is the Swing Worker responsible for calculate the FP´s and FN´s,
	 * using the the values of the current tab.
	 */
	private void calculateAction() {
		SwingWorker<String[], Void> worker = new SwingWorker<String[], Void>() {
			@Override
			protected String[] doInBackground() throws Exception {
				calculate.setEnabled(false);
				int tab = 0;
				if (tabs.getSelectedIndex() == 1) {
					tab = 1;
				}
				String[] fpn = new String[2];
				fpn[0] = Integer.toString(g_Worker.calculateFP(tab));
				fpn[1] = Integer.toString(g_Worker.calculateFN(tab));
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

	private void saveAutoFile() {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				g_Worker.writeFile("AutomaticConfiguration");
				return null;
			}
		};
		worker.execute();
	}

	private void saveManFile() {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() {
				g_Worker.writeFile("ManualConfiguration");
				return null;
			}
		};
		worker.execute();
	}

}
