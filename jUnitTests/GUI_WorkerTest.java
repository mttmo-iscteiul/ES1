package antiSpamFilter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Test;

/**
 * This class was developed to create a jUnit test for the GUI_Worker class.
 * @author ES1-2017-IC1-69
 *
 */

public class GUI_WorkerTest {
	
	/**
	 * This method tests if the constructor was well executed.
	 */

	@Test
	public void test_Constructor() {
		GUI_Worker instance = new GUI_Worker();
		assertNotNull(instance);

	}

	/**
	 * Testing case for input: String opt = "AutomaticConfiguration"
	 */
	@Test
	public void test_writeFile_1() {
		System.out.println("Now Testing Method:writeFile Branch:0");
		GUI_Worker instance = new GUI_Worker();
		instance.writeFile("AutomaticCOnfiguration");
		assertNotNull(new File("AntiSpamConfigurationForBalancedProfessionalAndLeisureMailbox/rules.cf"));
		
	}

	/**
	 * Testing case for input: String opt = "ManualConfiguration"
	 */
	@Test
	public void test_writeFile_2() {
		System.out.println("Now Testing Method:writeFile Branch:2");
		GUI_Worker instance = new GUI_Worker();
		instance.writeFile("ManualConfiguration");
		assertNotNull(new File(instance.getRulesFile()));
		
	}
	
	/**
	 * Testing case for input: double x = [335], int opt = 0
	 */
	@Test
	public void test_updateMapByVector_1() {
		System.out.println("Now Testing Method:updateMapByVector Branch:0");
		GUI_Worker instance = new GUI_Worker();
		instance.updateMapByVector(new double[335], 0);
		assertEquals(335, instance.getRulesSize());
		
	}

	/**
	 * Testing case for input: double x = [335], int opt = 1
	 */
	@Test
	public void test_updateMapByVector_2() {
		System.out.println("Now Testing Method:updateMapByVector Branch:2");
		GUI_Worker instance = new GUI_Worker();
		instance.updateMapByVector(new double[335], 1);
		assertEquals(335, instance.getManualConfig().size());
		
	}

	/**
	 * Testing if the correct value is outputed. Expected value = 3.0 
	 */
	@Test
	public void test_updateMapSingleValue() {
		System.out.println("Now Testing Method:updateMapSingleValue Branch:0");
		GUI_Worker instance = new GUI_Worker();
		instance.updateMapSingleValue("rule", 3.0);
		double a =  instance.getManualConfig().get("rule");
		assertEquals(3.0 ,a, 1);
		
	}

	/**
	 * Testing case for input: int tab = 0
	 */
	@Test
	public void test_calculateFN_1() {
		System.out.println("Now Testing Method:calculateFN Branch:0");
		GUI_Worker instance = new GUI_Worker();
		assertNotNull(instance.calculateFN(0));
		
	}

	/**
	 * Testing case for input: int tab = 1
	 */
	@Test
	public void test_calculateFN_2() {
		System.out.println("Now Testing Method:calculateFN Branch:1");
		GUI_Worker instance = new GUI_Worker();
		assertNotNull(instance.calculateFN(1));
		
	}

	/**
	 * Testing case for input != (tab == 0) and (tab == 1)
	 */
	@Test
	public void test_calculateFN_3() {
		System.out.println("Now Testing Method:calculateFN Branch:2");
		GUI_Worker instance = new GUI_Worker();
		assertNotNull(instance.calculateFN(3));
	
	}

	/**
	 * Testing case for input: int tab = 0
	 */
	@Test
	public void test_calculateFP_1() {
		System.out.println("Now Testing Method:calculateFP Branch:0");
		GUI_Worker instance = new GUI_Worker();
		assertNotNull(instance.calculateFP(0));

	}

	/**
	 * Testing case for input: int tab = 1
	 */
	@Test
	public void test_calculateFP_2() {
		System.out.println("Now Testing Method:calculateFP Branch:1");
		GUI_Worker instance = new GUI_Worker();
		assertNotNull(instance.calculateFP(1));
		
	}

	/**
	 * Testing case for input != (tab == 0) and (tab == 1)
	 */
	@Test
	public void test_calculateFP_3() {
		System.out.println("Now Testing Method:calculateFP Branch:2");
		GUI_Worker instance = new GUI_Worker();
		assertNotNull(instance.calculateFP(3));
		
	}

	/**
	 * Testing if function is executed with no errors
	 */
	@Test
	public void test_setNewConfiguration() {
		System.out.println("Now Testing Method:setNewConfiguration Branch:0");
		GUI_Worker instance = new GUI_Worker();
		instance.setNewConfiguration();
		return;
				
	}

	/**
	 * Testing if expected value is given
	 */
	@Test
	public void test_getRulesFile() {
		System.out.println("Now Testing Method:getRulesFile Branch:0");
		GUI_Worker instance = new GUI_Worker();
		assertNotNull(instance.getRulesFile());
		
	}

	/**
	 * Testing if expected value is given
	 */
	@Test
	public void test_getHamFile() {
		System.out.println("Now Testing Method:getHamFile Branch:0");
		GUI_Worker instance = new GUI_Worker();
		assertNotNull(instance.getHamFile());
		
	}

	/**
	 * Testing if expected value is given
	 */
	@Test
	public void test_getSpamFile() {
		System.out.println("Now Testing Method:getSpamFile Branch:0");
		GUI_Worker instance = new GUI_Worker();
		assertNotNull(instance.getSpamFile());
		
	}

	/**
	 * Testing if expected value is given
	 */
	@Test
	public void test_getRules() {
		System.out.println("Now Testing Method:getRules Branch:0");
		GUI_Worker instance = new GUI_Worker();
		assertNotNull(instance.getRules());
		
	}

	/**
	 * Testing if expected value is given
	 */
	@Test
	public void test_getManualConfig() {
		System.out.println("Now Testing Method:getManualConfig Branch:0");
		GUI_Worker instance = new GUI_Worker();
		assertNotNull(instance.getManualConfig());
		
	}

	/**
	 * Testing if expected value is given
	 */
	@Test
	public void test_getRulesSize() {
		System.out.println("Now Testing Method:getRulesSize Branch:0");
		GUI_Worker instance = new GUI_Worker();
		assertNotNull(instance.getRulesSize());
		
	}

}

