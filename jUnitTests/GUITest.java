package antiSpamFilter;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class was developed to create a jUnit test for the GUI class
 * @author ES1-2017-IC1-69
 *
 */

public class GUITest {
	
	/**
	 * This method tests if the constructor was well executed.
	 */
	@Test
	public void test_GUI_Constructor() {
		System.out.println("Now Testing Method:GUI Branch:0");
		GUI instance = new GUI();
		assertNotNull(instance);
		
	}
}

