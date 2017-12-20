package antiSpamFilter;

import java.io.File;

import javax.swing.JFileChooser;

public class FileChooser {

	private String hamFile;
	private String spamFile;

	public FileChooser() {
		JFileChooser fcHam = new JFileChooser();
		fcHam.setDialogTitle("escolha o ficheiro ham");
		fcHam.setCurrentDirectory(new File(System.getProperty("user.home")));
		int resultHam = fcHam.showOpenDialog(null);
		if (resultHam == JFileChooser.APPROVE_OPTION) {
			hamFile = fcHam.getSelectedFile().getAbsolutePath();
		}
		JFileChooser fcSpam = new JFileChooser();
		fcSpam.setDialogTitle("escolha o ficheiro spam");
		fcSpam.setCurrentDirectory(new File(System.getProperty("user.home")));
		int resultSpam = fcSpam.showOpenDialog(null);
		if (resultSpam == JFileChooser.APPROVE_OPTION) {
			spamFile = fcSpam.getSelectedFile().getAbsolutePath();
		}

	}

	public String getHamFile() {
		return hamFile;
	}

	public String getSpamFile() {
		return spamFile;
	}

}
