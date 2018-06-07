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
* This test tests the Alarms page via Operation (system+tenant)
* ----------------
* Tests:
* 	 - Enter the alarms page via Operation user (system).
* 	 1. Check the Export button.
* 	 2. Check the link to the Alarms page from the Dashboard button
* 
* 	 - Enter the alarms page via Operation user (tenant).
* 	 3. Check the Export button.
* 	 4. Check the link to the Alarms page from the Dashboard button
* 
* Results:
* 	 1. Reload mechanism should work successfully.
* 	 2. Link should display the Alerts menu.
*  	 3. Reload mechanism should work successfully.
* 	 4. Link should display the Alerts menu.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test84__Operation_alarms {
	
  private browserTypes  usedBrowser;
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test84__Operation_alarms(browserTypes browser) {
	  
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
  public void Operation_Alarms() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Login via Operation-user (system), enter the Alarms menu
	testFuncs.myDebugPrinting("Login via Operation-user (system), enter the Alarms menu");
	testFuncs.login(driver, testVars.getOperUsername(), testVars.getOperPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_DASHBOARD_ALARMS, "Export");
	
	// Step 1 - Test the Export button
	if (!this.usedBrowser.toString().equals(enumsClass.browserTypes.IE)) {
		
		testFuncs.myDebugPrinting("Step 1 - Test the Export button");
		testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), testVars.getAlarmsExport());
		testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/div/div[1]/h3/div/a[1]"), 5000);
		testFuncs.verifyStrByXpathContains(driver, "//*[@id='jqistate_state0']/div[2]", "Are your sure you want to export the alarms to CSV?");
		testFuncs.myClick(driver, By.xpath("//*[@id='update']")					, 2000);
		testFuncs.myClick(driver, By.xpath("//*[@id='jqi_state0_buttonExport']"), 10000);
		testFuncs.myAssertTrue("File was not downloaded successfully !!", testFuncs.findFilesByGivenPrefix(testVars.getDownloadsPath(), testVars.getAlarmsExport()));	
		testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), testVars.getAlarmsExport());
	}
		
    // Step 2 - Test the Alarms link from the main-page
	testFuncs.myDebugPrinting("Step 2 - Test the Alarms link from the main-page");	
    testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
	testFuncs.myClick(driver, By.xpath("//*[@id='navbar-collapse']/ul[3]/li[2]/a/i"), 3000);
	testFuncs.myClick(driver, By.xpath("/html/body/div[2]/span[2]/a/button"), 3000);
	testFuncs.searchStr(driver, "Alarms");
	
	// Logout, re-login via Operation-user (tenant), and enter the Alarms menu
	testFuncs.myDebugPrinting("Logout, re-login via Operation-user (tenant), and enter the Alarms menu");
	testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_MONITOR_GEN_INFOR_LOGOUT, testVars.getMainPageStr());
	testFuncs.login(driver, testVars.getOperTenUsername(), testVars.getOperTenPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_DASHBOARD_ALARMS, "Export");
	
	// Step 3 - Test the Export button
	if (!this.usedBrowser.toString().equals(enumsClass.browserTypes.IE)) {
		
		testFuncs.myDebugPrinting("Step 3 - Test the Export button");
		testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), testVars.getAlarmsExport());
		testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/div/div[1]/h3/div/a[1]"), 5000);
		testFuncs.verifyStrByXpathContains(driver, "//*[@id='jqistate_state0']/div[2]", "Are your sure you want to export the alarms to CSV?");
		testFuncs.myClick(driver, By.xpath("//*[@id='update']")					, 2000);
		testFuncs.myClick(driver, By.xpath("//*[@id='jqi_state0_buttonExport']"), 10000);
		testFuncs.myAssertTrue("File was not downloaded successfully !!", testFuncs.findFilesByGivenPrefix(testVars.getDownloadsPath(), testVars.getAlarmsExport()));	
		testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), testVars.getAlarmsExport());
	}
		
    // Step 4 - Test the Alarms link from the main-page
	testFuncs.myDebugPrinting("Step 4 - Test the Alarms link from the main-page");	
    testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
	testFuncs.myClick(driver, By.xpath("//*[@id='navbar-collapse']/ul[3]/li[2]/a/i"), 3000);
	testFuncs.myClick(driver, By.xpath("/html/body/div[2]/span[2]/a/button"), 3000);
	testFuncs.searchStr(driver, "Alarms");
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
