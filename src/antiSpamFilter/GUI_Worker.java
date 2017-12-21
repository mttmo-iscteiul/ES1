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
 * This Class is the "worker" class. Its main function is to attend all of GUI´s requests
 * and return the respective outputs.
 * @author ES1-2017-IC1-69
 *
 */
public class GUI_Worker {

	private FileChooser fc;
	private String hamFile;
	private String spamFile;
	private String rulesFile = "rules.cf";
	private LinkedHashMap<String, Double> rules = new LinkedHashMap<String, Double>();
	private LinkedHashMap<String, Double> automaticConfig = new LinkedHashMap<String, Double>();
	private ArrayList<ArrayList<String>> spam = new ArrayList<ArrayList<String>>();
	private ArrayList<ArrayList<String>> ham = new ArrayList<ArrayList<String>>();
	private int fp = 0;
	private int fn = 0;
	protected String rulesPath = "rules.cf";

	public static void main(String[] args) {
		new GUI_Worker();
	}

	/**
	 * This is the constructor of this class, which is initialized and uploads the default rules to the memory,
	 * or, if already exists a file that contains the previous configurations, it will upload that one instead.
	 * It also loads the spam.log and ham.log files to memory, for an optimized "search for" method in both files. 
	 */
	public GUI_Worker() {
		fc = new FileChooser();
		readFiles();
	}

	/**
	 * This class loads all the necessary files to memory for later usage.
	 */
	private void readFiles() {
		hamFile = fc.getHamFile();
		spamFile = fc.getSpamFile();
		BufferedReader bf;
		try {
			bf = new BufferedReader(new FileReader(new File(rulesFile)));
			String line;
			while ((line = bf.readLine()) != null) {
				String[] spliter = line.split("\t");
				if (spliter.length == 1) {
					rules.put(spliter[0], 0.0);
				} else {
					double weight = Double.parseDouble(spliter[1]);
					rules.put(spliter[0], weight);
				}
			}
			bf.close();
		} catch (IOException e) {
			e.printStackTrace();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * One of two methods to update the memory with the current values of each weight.
	 * In this particular one, the memory is updated by an array of double values.
	 * @param x The current weights to be loaded into the memory.
	 */
	protected void updateMapByVector(double[] x) {
		int i = 0;
		for (String rule : rules.keySet()) {
			rules.put(rule, x[i]);
			i++;
		}
	}

	/**
	 *  One of two methods to update the memory with the current values of each weight.
	 * In this particular one, the memory is loaded only with the specified value for
	 * the given rule.
	 * @param rule The rule to be updated
	 * @param weight The respective weight
	 */
	protected void updateMapSingleValue(String rule, double weight) {
		rules.put(rule, weight);
	}

	/**
	 * Method used to calculate the number of False Negatives
	 * @return Number of False Negatives
	 */
	protected int calculateFN() {
		this.fn = 0;
		for (ArrayList<String> l : ham) {
			double weight = 0;
			for (String str : l) {
				if (rules.containsKey(str)) {
					weight += rules.get(str);
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
	 * Method used to calculate the number of False Positives.
	 * @return Number of False Positives
	 */
	protected int calculateFP() {
		this.fp = 0;
		for (ArrayList<String> l : spam) {
			double weight = 0;
			for (String str : l) {
				if (rules.containsKey(str)) 
					weight += rules.get(str);
			}
			if (weight < 5.0) {
				fp++;
			}
		}
		return Math.abs(fp);
	}

	/**
	 * This is the method that will determine which of the optimized array of weights
	 * is the best for my problem. Probably the most important method of the whole project.
	 */
	protected void setNewConfiguration() {
		BufferedReader bf;
		try {
			bf = new BufferedReader(new FileReader(new File(
					"experimentBaseDirectory/RESULTADOS/AntiSpamFilterProblem.NSGAII.rf")));
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
			bf = new BufferedReader(new FileReader(new File(
					"experimentBaseDirectory/RESULTADOS/AntiSpamFilterProblem.NSGAII.rs")));
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
				automaticConfig.put(rule, b);
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

	public int getRulesSize() {
		return rules.size();
	}
}
