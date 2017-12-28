package EMS_Tests;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import org.openqa.selenium.*;

/**
* ----------------
* This test tests the default phone firmware files menu
* ----------------
* Tests:
* 	 - Login via an Operation user (Tenant) 
* 	 1. Verify that the Phone firmware files menu is not displayed
* 
* 	 - Login via an Operation user (System) and enter the Phone firmware files menu.
* 	 2. Try to delete a default firmware.
* 	 3. Try to edit a default firmware.
*    4. Create the default firmware again for future testing

* Results:
*    1.   The menu should not be displayed
*    2-3. The actions should be possible and end successfully.
*    4.   The default firmware should be created successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test57__Operation_default_phone_firmware_files {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test57__Operation_default_phone_firmware_files(String browser) {
	  
	  System.out.println("Browser - "  + browser);
	  this.usedBrowser = browser;
  }
  
  // Define each browser as a parameter
  @SuppressWarnings("rawtypes")
  @Parameters(name="{0}")
  public static Collection data() {
	  
	GlobalVars testVars2  = new GlobalVars();
    return Arrays.asList(testVars2.getBrowsers());
  }
  
  @BeforeClass
  public static void setting_SystemProperties() {
	  
	  System.out.println("System Properties seting Key value.");
  }  
  
  @Before
  public void setUp() throws Exception {
	  	
	testVars  = new GlobalVars();
    testFuncs = new GlobalFuncs(); 
    System.setProperty("webdriver.chrome.driver", testVars.getchromeDrvPath());
	System.setProperty("webdriver.ie.driver"    , testVars.getIeDrvPath());
	testFuncs.myDebugPrinting("Enter setUp(); usedbrowser - " + this.usedBrowser);
	driver = testFuncs.defineUsedBrowser(this.usedBrowser);
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void Operation_phone_firmware_files_actions() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	 
    // Login via an Operation user (Tenant) and verify that the Phone firmware files menu
	testFuncs.myDebugPrinting("Login via an Operation user (Tenant) and enter the Phone firmware files menu");
	testFuncs.login(driver, testVars.getOperTenUsername(), testVars.getOperTenPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	
	// Step 1 - Verify that the Phone firmware files menu is not displayed
	testFuncs.myDebugPrinting("Step 1 - Verify that the Phone firmware files menu is not displayed");
	String txt = driver.findElement(By.tagName("body")).getText();
	testFuncs.myAssertTrue("Phone firmware files menu is displayed !!\ntxt - " + txt, !txt.contains("Phone Firmware Files"));
	testFuncs.enterMenu(driver, "General_Informatiom_logout", testVars.getMainPageStr());

	// Login via an Operation user (System) and enter the Phone firmware files menu
	testFuncs.login(driver, testVars.getOperUsername(), testVars.getOperPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "Setup_Phone_conf_phone_firmware_files", "Phone firmware files");

	// Step 2 - Try to edit a default firmware
  	testFuncs.myDebugPrinting("Step 2 - Try to edit a default firmware");
	String newFirmName    = "430HD";
	String newFirmVersion = testFuncs.getId();
	String firmFileName   = testVars.getImportFile("31");
	String newFirmDesc    = newFirmName + " - default firmware";
  	testFuncs.editFirmware(driver, newFirmName, newFirmDesc, newFirmDesc, newFirmVersion, "");  	
		
	// Step 3 - Try to delete a default firmware
  	testFuncs.myDebugPrinting("Step 3 - Try to delete a default firmware");
	testFuncs.deleteFirmware(driver,  newFirmName, newFirmDesc, newFirmVersion);
	
	// Step 4 - Create the default firmware again for future testing
  	testFuncs.myDebugPrinting("Step 4 - Create the default firmware again for future testing");
	testFuncs.addNewFirmware(driver, newFirmName, newFirmDesc, "", "", firmFileName);
  }
  
  @After
  public void tearDown() throws Exception {
	  
    driver.quit();
    System.clearProperty("webdriver.chrome.driver");
	System.clearProperty("webdriver.ie.driver");
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
    	
    	testFuncs.myFail(verificationErrorString);
    }
  }
}
