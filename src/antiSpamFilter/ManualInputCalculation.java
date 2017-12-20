package antiSpamFilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.table.DefaultTableModel;

public class ManualInputCalculation {

	private HashMap<String, Double> rulesAndWeights = new HashMap<String, Double>();
	private ArrayList<ArrayList<String>> spam = new ArrayList<ArrayList<String>>();
	private ArrayList<ArrayList<String>> ham = new ArrayList<ArrayList<String>>();
	private int fp = 0;
	private int fn = 0;

	public ManualInputCalculation(DefaultTableModel dtm) {
		tableToMap(dtm);
		readFiles();
		calculation();
	}

	private void tableToMap(DefaultTableModel dtm) {
		for (int i = 0; i < dtm.getRowCount(); i++) {
			rulesAndWeights.put((String) dtm.getValueAt(i, 0), (Double) dtm.getValueAt(i, 1));
		}
	}

	public void readFiles() {
		try {
			BufferedReader bf = new BufferedReader(new FileReader(new File("spam.log")));
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
			e.printStackTrace();
		}
		try {
			BufferedReader bf = new BufferedReader(new FileReader(new File("ham.log")));
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
			e.printStackTrace();
		}
	}

	public void calculation() {
		for (ArrayList<String> l : spam) {
			double weight = 0;
			for (String str : l) {
				if (rulesAndWeights.containsKey(str)) {
					weight += rulesAndWeights.get(str);
				} else {
					System.out.println("No rule found");
				}
			}
			if (weight < 5.0) {
				fp++;
				System.out.println(weight);
			}
		}

		for (ArrayList<String> l : ham) {
			double weight = 0;
			for (String str : l) {
				if (rulesAndWeights.containsKey(str)) {
					weight += rulesAndWeights.get(str);
				} else {
					System.out.println("No rule found");
				}
			}
			if (weight > 5.0) {
				fn++;
			}
		}
	}

	public String getFp() {
		return Integer.toString(fp);
	}

	public String getFn() {
		return Integer.toString(fn);
	}

}
