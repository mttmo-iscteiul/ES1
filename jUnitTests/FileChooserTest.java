package antiSpamFilter;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * This class was developed to create a jUnit test for the FileChooser class.
 * @author ES1-2017-IC1-69
 *
 */

public class FileChooserTest {
	
	/**
	 * This method tests if the constructor was well executed.
	 */
	
	@Test
	public void getFilePath() {
		FileChooser f = new FileChooser("log", "Escolha o rules");
		assertNotNull(f.getFilePath());
	}

}
