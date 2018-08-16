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
* This test tests the phone firmware files menu via an Operation user.
* ----------------
* Tests:
* 	 - Enter the Phone firmware files menu via an Operation user.
* 	 1. Upload a phone firmware.
* 	 2. Edit a phone firmware.
* 	 3. Delete a phone firmware.
* 
* Results:
* 	 1-3. All actions should be performed successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test56__Operation_phone_firmware_files {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test56__Operation_phone_firmware_files(browserTypes browser) {
	  
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
  public void Opeartion_phone_firmware_files() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String Id = testFuncs.getId();
	String firmName     = "myFirmName"  + Id;
	String firmDesc     = "myFirmDesc"  + Id;
	String firmVersion = String.valueOf(testFuncs.getNum(128)) + "." +
						 String.valueOf(testFuncs.getNum(128)) + "." +
						 String.valueOf(testFuncs.getNum(128)) + "." +
						 String.valueOf(testFuncs.getNum(128));
	String firmRegion   = testVars.getNonDefTenant(0);
	String firmFileName = testVars.getImportFile("31");
	 
    // Enter the Phone firmware files menu
	testFuncs.myDebugPrinting("Enter the Phone firmware files menu");
	testFuncs.login(driver, testVars.getOperSysLoginData(enumsClass.loginData.USERNAME), testVars.getOperSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONFIGURATION_PHONE_FIRM_FILES, "Phone firmware files");

	// Step 1 - Add new IP Phone firmware
  	testFuncs.myDebugPrinting("Step 1 - Add new IP Phone firmware");
	testFuncs.addNewFirmware(driver, firmName, firmDesc, firmVersion, firmRegion, firmFileName);
	
	// Step 2 - Edit an existing firmware and check Download
  	testFuncs.myDebugPrinting("Step 2 - Edit an existing firmware and check Download");
	String newFirmDesc   = "edited"  + firmDesc;
	String newFirmRegion = testVars.getNonDefTenant(1);
	String newFirmVersion = String.valueOf(testFuncs.getNum(128)) + "." +
			  	  			String.valueOf(testFuncs.getNum(128)) + "." +
			  	  			String.valueOf(testFuncs.getNum(128)) + "." +
			  	  			String.valueOf(testFuncs.getNum(128));	
  	testFuncs.editFirmware(driver, firmName, firmDesc, newFirmDesc, newFirmVersion, newFirmRegion);  	
		
	// Step 3 - Delete a firmware
  	testFuncs.myDebugPrinting("Step 3 - Delete a firmware");
	testFuncs.deleteFirmware(driver,  firmName, newFirmDesc, newFirmVersion);
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
