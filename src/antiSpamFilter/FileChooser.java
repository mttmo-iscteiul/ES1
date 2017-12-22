package antiSpamFilter;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * This class is responsible for receiving the paths given by the user, from
 * where will give them to the GUI_Worker class to upload them into the
 * application memory
 * 
 * @author ES1-2017-IC1-69
 *
 */
public class FileChooser {

	private FileNameExtensionFilter filter;
	private String filePath;

	/**
	 * This constructor initializes all three functions of this class to receive
	 * the paths from the user.
	 * @param extension This the extension file pretended to select
	 * @param header This is the frame title pretended to appear 
	 */

	public FileChooser(String extension, String header) {
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle(header);
		filter = new FileNameExtensionFilter(extension, extension);
		fc.setFileFilter(filter);
		fc.setAcceptAllFileFilterUsed(false);
		fc.setCurrentDirectory(new File(System.getProperty("user.home")));
		int resultRules = fc.showOpenDialog(null);
		if (resultRules == JFileChooser.APPROVE_OPTION) {
			filePath = fc.getSelectedFile().getAbsolutePath();
		} else if (resultRules == 1) {
			System.exit(0);
		} else {
			JOptionPane.showMessageDialog(null, "Non valid input");
			System.exit(0);
		}

	}

	/**
	 * This method returns the path from the current file selected
	 * 
	 * @return Current file path selected
	 *
	 */

	public String getFilePath() {
		return filePath;
	}
}
