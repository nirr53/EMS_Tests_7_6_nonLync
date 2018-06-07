package EMS_Tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
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
* This test tests the phone firmware files menu via a Monitoring users (system +tenant).
* ----------------
* Tests:
* 	 - Login via a Monitoring user (system) and enter the Phone firmware files menu.
* 	 1. Verify that you cannot upload a Phone firmware.
* 	 2. Verify that you cannot edit an existing Phone firmware.
* 	 3. Verify that you cannot delete a phone firmware.
* 
* 	 - Login via a Monitoring user (tenant)
* 	 4.  Verify that the Phone firmware files menu is not displayed
* 
* Results:
* 	1-3. Via a Monitoring user you cannot upload, edit or delete any firmware.
*     4. Phone firmware files menu should not be displayed.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test41__Monitoring_phone_firmware_files {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test41__Monitoring_phone_firmware_files(browserTypes browser) {
	  
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
  public void Phone_firmware_files() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	 
    // Login via a Monitoring user (system) and enter the Phone firmware files menu
	testFuncs.myDebugPrinting("Login via a Monitoring (system) user and enter the Phone firmware files menu");
	testFuncs.login(driver, testVars.getMonitUsername(), testVars.getMonitPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONFIGURATION_PHONE_FIRM_FILES, "Phone firmware files");
	 
	// Step 1 - Verify that you cannot upload a Phone firmware
	testFuncs.myDebugPrinting("Step 1 - Verify that you cannot upload a Phone firmware");
	String classTtxt = driver.findElement(By.xpath("//*[@id='tbTemps']/tbody/tr[1]/td/a")).getAttribute("class");
	testFuncs.myAssertTrue("Add-Firmware button is active !!", classTtxt.contains("not-active"));

	// Step 2 - Verify that you cannot edit a Phone firmware
  	testFuncs.myDebugPrinting("Step 2 - Verify that you cannot edit a Phone firmware");
  	int i = getIdx(driver, testVars.getDefPhoneModel());
  	testFuncs.myClick(driver, By.xpath("//*[@id='tbTemps']/tbody/tr[2]/td/table/tbody/tr[" + i + "]/td[7]/a"), 3000);
  	testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/div[2]/div[3]/button[1]"), 3000);
    testFuncs.searchStr(driver, "Unauthorized");
	testFuncs.searchStr(driver, "You do not have permission to modify this item");

	// Step 3 - Verify that you cannot delete a Phone firmware
  	testFuncs.myDebugPrinting("Step 3 - Verify that you cannot delete a Phone firmware");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONFIGURATION_PHONE_FIRM_FILES, "Phone firmware files");
  	int i2 = getIdx(driver, testVars.getDefPhoneModel());
	classTtxt = driver.findElement(By.xpath("//*[@id='tbTemps']/tbody/tr[2]/td/table/tbody/tr[" + i2 + "]/td[8]/a")).getAttribute("class");
	testFuncs.myAssertTrue("Delete-Firmware button is active !!", classTtxt.contains("not-active"));	
	
    // Step 4 - Logout, re-login via a Monitoring user (tenant) and verify that the Phone firmware files menu is not displayed
	testFuncs.myDebugPrinting("Step 4 - Logout, re-login via a Monitoring user (tenant) and enter the Phone firmware files menu");
	testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_GEN_INFOR_LOGOUT, testVars.getMainPageStr());
	testFuncs.login(driver, testVars.getMonitTenUsername(), testVars.getMonitTenPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONF_SECTION, "Phones Configuration");
	String txt = driver.findElement(By.tagName("body")).getText();
	testFuncs.myAssertTrue("Delete-Firmware button is active !!", !txt.contains("Phone firmware files"));	
  }
  
  // Get idx accorfing to given Firmware name
  int getIdx(WebDriver driver, String firmName) throws IOException {
	  
	  // Get idx
	  BufferedReader r = new BufferedReader(new StringReader(driver.findElement(By.tagName("body")).getText()));
	  String l = null;
	  int i = 1;
	  while ((l = r.readLine()) != null) {
		  
		  testFuncs.myDebugPrinting("i - " + i, enumsClass.logModes.DEBUG);
		  testFuncs.myDebugPrinting(l		  , enumsClass.logModes.DEBUG);  
		  if (l.contains(firmName)) {
			  
			  testFuncs.myDebugPrinting("i - " + i, enumsClass.logModes.MINOR);
			  break;
		  }
		  if (l.contains("Edit" )) {
			  
			  ++i;
		  }
	  } 
	  return i;	  
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
