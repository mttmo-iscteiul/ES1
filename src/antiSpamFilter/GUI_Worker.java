package antiSpamFilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class GUI_Worker {

	private FileChooser fc;
	private String hamFile;
	private String spamFile;
	private String rulesFile = "rules.cf";
	private HashMap<String, Double> rules = new HashMap<String, Double>();
	private ArrayList<ArrayList<String>> spam = new ArrayList<ArrayList<String>>();
	private ArrayList<ArrayList<String>> ham = new ArrayList<ArrayList<String>>();
	private int fp = 0;
	private int fn = 0;
	
	public GUI_Worker() {
		fc = new FileChooser();
		readFiles();
	}
		
	private void readFiles() {
		hamFile = fc.getHamFile();
		spamFile = fc.getSpamFile();
		BufferedReader bf;
		try {
			bf = new BufferedReader(new FileReader(new File(rulesFile)));
			String line;
			while ((line = bf.readLine()) != null) {
				rules.put(line, 0.0);
			}
			bf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			bf = new BufferedReader(new FileReader(spamFile));
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
			bf = new BufferedReader(new FileReader(hamFile));
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
	
	protected void updateMap(double[] x) {
		int i = 0;
		for(String rule: rules.keySet()) {
			rules.put(rule, x[i]);
			i++;
		}
	}
	
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
