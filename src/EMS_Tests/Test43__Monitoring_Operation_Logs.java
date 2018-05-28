package EMS_Tests;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import org.openqa.selenium.*;import EMS_Tests.enumsClass.browserTypes;

/**
* ----------------
* This test verify that the Log menus are hidden when you login via Monitoring or Operation users
* ----------------
* Tests:
* 	 1. Login with a  Monitoring user (System), check the Log menu is not displayed and logout.
* 	 2. Login with a  Monitoring user (Tenant), check the Log menu is not displayed and logout.
* 	 3. Login with a  Operation user  (system), check that you can see the Logs menu and logout
* 	 4. Login with an Operation user  (Tenant), check the Log menu is not displayed.
* 
* Results:
*  	 1. Via a Monitoring user (System) the Log menu is not displayed.
*    2. Via a Monitoring user (Tenant) the Log menu is not displayed.
*    3. Via a Operation user  (System) the Log menu is displayed.
*  	 4. Via a Operation user  (Tenant) the Log menu is not displayed.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test43__Monitoring_Operation_Logs {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test43__Monitoring_Operation_Logs(browserTypes browser) {
	  
	  System.out.println("Browser - "  + browser);
	  this.usedBrowser = browser;
  }
  
  //Define each browser as a parameter
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
  public void Monitoring_Operation_Log_menus() throws Exception {
	
	  Log.startTestCase(this.getClass().getName());
	  
	  // Step 1 - Login with a  Monitoring user (system), check that you cannot see the menu and logout
	  testFuncs.myDebugPrinting("Step 1 - Login with a  Monitoring user (system) and check that you cannot see the menu");
	  testFuncs.login(driver, testVars.getMonitUsername(), testVars.getMonitPassword(), testVars.getSysMainStr()          , "https://", this.usedBrowser);
	  testFuncs.enterMenu(driver, "Troubleshoot_system_diagnostics", "Unauthorized");
	  testFuncs.searchStr(driver, "You do not have permission to modify this item");
	  testFuncs.enterMenu(driver, "General_Informatiom_logout", testVars.getMainPageStr());
	  
	  // Step 2 - Login with a  Monitoring user (Tenant), check that you cannot see the menu and logout
	  testFuncs.myDebugPrinting("Step 2 - Login with a  Monitoring user (Tenant) and check that you cannot see the menu");
	  testFuncs.login(driver, testVars.getMonitTenUsername(), testVars.getMonitTenPassword(), testVars.getSysMainStr()    , "https://", this.usedBrowser);
	  String bodyText = driver.findElement(By.tagName("body")).getText();
	  testFuncs.myAssertTrue("Troubleshot system menu is displayed !!\bbodyText - " + bodyText, !bodyText.contains("TROUBLESHOT"));
	  testFuncs.enterMenu(driver, "General_Informatiom_logout", testVars.getMainPageStr());
	  
	  // Step 3 - Login with a  Operation user (system), check that you can see the Logs menu and logout
	  testFuncs.myDebugPrinting("Step 3 - Login with a  Operation user (system), check that you can see the Logs menu and logout");
	  testFuncs.login(driver, testVars.getOperUsername(), testVars.getOperPassword(), testVars.getSysMainStr()             , "https://", this.usedBrowser);
	  testFuncs.enterMenu(driver, "Troubleshoot_system_diagnostics", "System Logs");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='trunkTBL']/div/div[1]/h3"					  , "System Logs");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='trunkTBL']/div/div[2]/form/table/thead/tr/th", "System Logs");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='trunkTBL']/div/div[2]/form/table/tbody/tr[1]/td[1]/span/b", "Web Admin");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='trunkTBL']/div/div[2]/form/table/tbody/tr[2]/td[1]/span/b", "Activity");
	  
	  // Step 4 - Login with a  Operation user (Tenant), check that you cannot see the menu
	  testFuncs.myDebugPrinting("Step 4 - Login with a  Operation user (Tenant) and check that you cannot see the menu");
	  testFuncs.login(driver, testVars.getOperTenUsername(), testVars.getOperTenPassword(), testVars.getSysMainStr()      , "https://", this.usedBrowser);
	  bodyText = driver.findElement(By.tagName("body")).getText();
	  testFuncs.myAssertTrue("Troubleshot system menu is displayed !!\bbodyText - " + bodyText, !bodyText.contains("TROUBLESHOT"));
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
