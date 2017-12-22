package antiSpamFilter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.swing.JOptionPane;

/**
 * This Class is the "worker" class. Its main function is to attend all of GUI´s
 * requests and some of the AntiSpamFilterProblem class and return the
 * respective outputs.
 * 
 * @author ES1-2017-IC1-69
 *
 */
public class GUI_Worker {

	private String hamFile;
	private String spamFile;
	private String rulesFile;
	private LinkedHashMap<String, Double> rules = new LinkedHashMap<String, Double>();
	private LinkedHashMap<String, Double> manualConfig = new LinkedHashMap<String, Double>();
	private ArrayList<ArrayList<String>> spam = new ArrayList<ArrayList<String>>();
	private ArrayList<ArrayList<String>> ham = new ArrayList<ArrayList<String>>();
	private int fp = 0;
	private int fn = 0;

	/**
	 * This is the constructor of this class, which is initialized by the GUI
	 * class. Firstly initializes the FileChooser class to allow the user to
	 * choose the path to the needed files to run the program. Secondly, uploads
	 * the rules present in the rules path file to the memory. It also loads the
	 * information present in the spamFile and hamFile to memory, for an
	 * optimized.
	 */
	public GUI_Worker() {
		readFiles();
	}

	/**
	 * This class loads all the necessary files to memory for later usage.
	 */
	private void readFiles() {
		rulesFile = new FileChooser("cf", "Escolha o ficheiro Rules").getFilePath();
		hamFile = new FileChooser("log", "Escolha o ficheiro Ham").getFilePath();;
		spamFile = new FileChooser("log", "Escolha o ficheiro Spam").getFilePath();
		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new FileReader(new File(rulesFile)));
			String line;
			while ((line = bf.readLine()) != null) {
				String[] spliter = line.split("\t");
				if (spliter.length == 1) {
					rules.put(spliter[0], 0.0);
					manualConfig.put(spliter[0], 0.0);
				} else {
					double weight = Double.parseDouble(spliter[1]);
					rules.put(spliter[0], weight);
					manualConfig.put(spliter[0], weight);
				}
			}
			bf.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"The input given by the user for the rules path is incorrect. Confirm to exit.");
			System.exit(0);
		}
		try {
			bf = new BufferedReader(new FileReader(new File(spamFile)));
			String line = "";
			ArrayList<String> aux = new ArrayList<String>();
			while ((line = bf.readLine()) != null) {
				String[] split = line.split("\t");
				aux = new ArrayList<String>(Arrays.asList(split));
				aux.remove(0);
				spam.add(aux);
			}
			bf.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"The input given by the user for the spam path is incorrect. Confirm to exit.");
			System.exit(0);
		}
		try {
			bf = new BufferedReader(new FileReader(new File(hamFile)));
			String line = "";
			ArrayList<String> aux = new ArrayList<String>();
			while ((line = bf.readLine()) != null) {
				String[] split = line.split("\t");
				aux = new ArrayList<String>(Arrays.asList(split));
				aux.remove(0);
				ham.add(aux);
			}
			bf.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"The input given by the user for the ham path is incorrect. Confirm to exit.");
			System.exit(0);
		}
	}

	/**
	 * This class is used to save the current configuration of weights in the
	 * respective file
	 * 
	 * @param option
	 *            This parameter is used to let the program know if its supposed
	 *            to save the automatic configuration given by the jMetal, or to
	 *            save the manual input configuration.
	 */
	protected void writeFile(String option) {
		
		BufferedWriter pw;
		try {
			switch (option) {
			default:
			case "AutomaticConfiguration":
				File file = new File("AntiSpamConfigurationForBalancedProfessionalAndLeisureMailbox/rules.cf");
				file.getParentFile().mkdir();
				file.createNewFile();
				pw = new BufferedWriter(new FileWriter(file));
				for (String line : rules.keySet()) {
					pw.write(line + "\t" + Double.toString(rules.get(line)) + "\n");
				}
				pw.close();
				JOptionPane.showMessageDialog(null, "The Automatic config. file was saved!");
				break;
			case "ManualConfiguration":
				pw = new BufferedWriter(new FileWriter(rulesFile));
				for (String line : manualConfig.keySet()) {
					pw.write(line + "\t" + Double.toString(manualConfig.get(line)) + "\n");
				}
				pw.close();
				JOptionPane.showMessageDialog(null, "The Manual config. file was saved!");
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * One of two methods to update the memory with the current values of each
	 * weight. In this particular one, the memory is updated by an array of
	 * double values.
	 * 
	 * @param x
	 *            The current weights to be loaded into the memory.
	 */
	protected void updateMapByVector(double[] x, int opt) {
		if (x instanceof double[]) {
			switch (opt) {
			default:
			case 0:
				int i = 0;
				for (String rule : rules.keySet()) {
					rules.put(rule, x[i]);
					i++;
				}
				break;
			case 1:
				int j = 0;
				for (String rule : manualConfig.keySet()) {
					manualConfig.put(rule, x[j]);
					j++;
				}
				break;
			}
		}
	}

	/**
	 * One of two methods to update the memory with the current values of each
	 * weight. In this particular one, the memory is loaded only with the
	 * specified value for the given rule. This method is only for the manual
	 * input Table.
	 * 
	 * @param rule
	 *            The rule to be updated
	 * @param weight
	 *            The respective weight
	 */
	protected void updateMapSingleValue(String rule, double weight) {
		manualConfig.put(rule, weight);
	}

	/**
	 * Method used to calculate the number of False Negatives present in the
	 * current selected table
	 * 
	 * @return Number of False Negatives
	 */
	protected int calculateFN(int tab) {
		LinkedHashMap<String, Double> auxRules = new LinkedHashMap<String, Double>();
		if (tab == 0)
			auxRules = rules;
		else if (tab == 1) {
			auxRules = manualConfig;
		}
		this.fn = 0;
		for (ArrayList<String> l : ham) {
			double weight = 0;
			for (String str : l) {
				if (auxRules.containsKey(str)) {
					weight += auxRules.get(str);
				} else {
					System.out.println("No rule found");
				}
			}
			if (weight > 5.0) {
				fn++;
			}
		}
		return Math.abs(fn);
	}

	/**
	 * Method used to calculate the number of False Positives present in the
	 * current selected table.
	 * 
	 * @return Number of False Positives
	 */
	protected int calculateFP(int tab) {
		LinkedHashMap<String, Double> auxRules = new LinkedHashMap<String, Double>();
		if (tab == 0)
			auxRules = rules;
		else if (tab == 1) {
			auxRules = manualConfig;
		}
		this.fp = 0;
		for (ArrayList<String> l : spam) {
			double weight = 0;
			for (String str : l) {
				if (auxRules.containsKey(str))
					weight += auxRules.get(str);
			}
			if (weight < 5.0) {
				fp++;
			}
		}
		return Math.abs(fp);
	}

	/**
	 * This is the method that will determine which of the optimized array of
	 * weights is the best for my problem. Probably the most important method of
	 * the whole project.
	 */
	protected void setNewConfiguration() {
		BufferedReader bf;
		try {
			bf = new BufferedReader(
					new FileReader(new File("experimentBaseDirectory/RESULTADOS/AntiSpamFilterProblem.rf")));
			String line;
			int currentNum = 0;
			int num = 0;
			double currentMinimal = 0.0;
			double bestMinimal = 500.0;
			while ((line = bf.readLine()) != null) {
				currentNum++;
				String[] split = line.split(" ");
				double auxMax = Math.max(Double.parseDouble(split[0]), Double.parseDouble(split[1]));
				double auxMin = Math.min(Double.parseDouble(split[0]), Double.parseDouble(split[1]));
				currentMinimal = (int) Math.abs(auxMax - auxMin);
				if (auxMax != auxMin) {
					if (currentMinimal < bestMinimal) {
						bestMinimal = currentMinimal;
						num = currentNum;
					}
				} else {
					num = currentNum;
				}
			}
			bf = new BufferedReader(
					new FileReader(new File("experimentBaseDirectory/RESULTADOS/AntiSpamFilterProblem.rs")));
			int cNum = 1;
			ArrayList<String> spliter = new ArrayList<String>();
			while ((line = bf.readLine()) != null) {
				if (cNum != num) {
					cNum++;
				} else {
					String[] split = line.split(" ");
					spliter.addAll(Arrays.asList(split));
					break;
				}
			}
			int a = 0;
			for (String rule : rules.keySet()) {
				double b = Double.parseDouble(spliter.get(a));
				rules.put(rule, b);
				manualConfig.put(rule, b);
				a++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getRulesFile() {
		return rulesFile;
	}

	public String getHamFile() {
		return hamFile;
	}

	public String getSpamFile() {
		return spamFile;
	}

	public HashMap<String, Double> getRules() {
		return rules;
	}

	public LinkedHashMap<String, Double> getManualConfig() {
		return manualConfig;
	}

	public int getRulesSize() {
		return rules.size();
	}
}
