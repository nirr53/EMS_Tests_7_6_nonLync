package EMS_Tests;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.*;import EMS_Tests.enumsClass.*;

/**
* ----------------
* This test tests the phone configuration files menu
* ----------------
* Tests:
* 	 - Enter the Phone configuration files menu.
* 	 1. Upload a phone configuration.
* 	 2. Download a phone configuration.
* 	 3. Delete a phone configuration.
* 
* Results:
* 	 1. Phone configuration should be uploaded successfully.
*	 2. Phone configuration should be displayed successfully.
* 	 3. Phone configuration should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test30__phone_configuration_files {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test30__phone_configuration_files(browserTypes browser) {
	  
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
  public void Phone_configuration_files_menu() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	String usedFile = testVars.getImportFile("30");
	 
    // Enter the Phone configuration files menu
	testFuncs.myDebugPrinting("Enter the Phone configuration files menu");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONFIGURATION_PHONE_CONF_FILES, "Manage Configuration Files");
	
	// Step 1 - Upload a phone configuration file
  	testFuncs.myDebugPrinting("Step 1 - Upload a phone configuration file");
  	testFuncs.uploadConfigurationFile(driver,  testVars.getSrcFilesPath() + "\\" + usedFile, usedFile);
	
	// Step 2 - Display configuration file
 	testFuncs.myDebugPrinting("Step 2 - Display configuration file");
 	String currUrl  = driver.getCurrentUrl();
 	testFuncs.myDebugPrinting("currUrl - " + currUrl, enumsClass.logModes.MINOR);
    driver.get("https://" + testVars.getIp() + "//configfiles//" + usedFile);
 	testFuncs.myWait(5000);
 	testFuncs.searchStr(driver, "network/lan/vlan/period=30");
    driver.get(currUrl);

	// Step 3 - Delete a phone configuration file
  	testFuncs.myDebugPrinting("Step 3 - Delete a phone configuration file");
	testFuncs.deleteConfigurationFile(driver, usedFile);
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
