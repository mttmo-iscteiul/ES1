package antiSpamFilter;

import java.io.File;
import java.io.IOException;

/**
 * This class contains the external applications invoked in this application to
 * process the output files .TeX and .R
 * 
 * @author ES1-2017-IC1-69
 *
 */

public class ExternalApplications {

	public ExternalApplications() {
		PdfLatex();
		HvBoxplotR();
	}

	/**
	 * Method to invoke pdfLatex, which will process the .TeX file.
	 */

	private void PdfLatex() {
		try {
			ProcessBuilder builder = new ProcessBuilder("pdfLatex", "AntiSpamStudy.tex");
			builder.directory(new File("experimentBaseDirectory/AntiSpamStudy/latex/").getAbsoluteFile());
			builder.redirectErrorStream(true);
			builder.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to invoke RScript, which will process the .TeX file.
	 */

	private void HvBoxplotR() {
		try {
			ProcessBuilder builder = new ProcessBuilder("RScript", "HV.Boxplot.R");
			builder.directory(new File("experimentBaseDirectory/AntiSpamStudy/R/").getAbsoluteFile());
			builder.redirectErrorStream(true);
			builder.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
