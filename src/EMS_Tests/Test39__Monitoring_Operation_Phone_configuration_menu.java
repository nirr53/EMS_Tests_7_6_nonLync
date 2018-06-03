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
* This test tests the different modes of Phone-Configuration menu via several users.
* ----------------
* Tests:
* 	 1. Login with a  Monitoring user (system), check that you cannot see the menu and logout.
*    2. Login with a  Monitoring user (Tenant), check that you cannot see the menu and logout.
*    3. Login with an  Operation user (System), check that you can see the menu and logout.
*    4. Login with an  Operation user (Tenant), check that you can see the menu.
* 
* Results:
*    1-2. You cannot see the Phone-configuration menu.
*    3-4. You can see the Phone-configuration menu.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test39__Monitoring_Operation_Phone_configuration_menu {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test39__Monitoring_Operation_Phone_configuration_menu(browserTypes browser) {
	  
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
  public void Monitoring_Operation_Configuration_menu() throws Exception {
	
	  Log.startTestCase(this.getClass().getName());
	  
	  // Step 1 - Login with a  Monitoring user (system), check that you cannot see the menu and logout
	  testFuncs.myDebugPrinting("Step 1 - Login with a  Monitoring user (system) and check that you cannot see the menu");
	  testFuncs.login(driver, testVars.getMonitUsername(), testVars.getMonitPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	  testFuncs.enterMenu(driver, "Setup_Phone_conf_phone_configuration_files", "Unauthorized");
	  testFuncs.searchStr(driver, "You do not have permission to modify this item");
	  testFuncs.enterMenu(driver, "General_Informatiom_logout", testVars.getMainPageStr());
	  
	  // Step 2 - Login with a  Monitoring user (Tenant), check that you cannot see the menu and logout
	  testFuncs.myDebugPrinting("Step 2 - Login with a  Monitoring user (Tenant) and check that you cannot see the menu");
	  testFuncs.login(driver, testVars.getMonitUsername(), testVars.getMonitPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	  testFuncs.enterMenu(driver, "Setup_Phone_conf_phone_configuration_files", "Unauthorized");
	  testFuncs.searchStr(driver, "You do not have permission to modify this item");
	  testFuncs.enterMenu(driver, "General_Informatiom_logout", testVars.getMainPageStr());
	  
	  // Step 3 - Login with an  Operation user (System), check that you can see the menu and logout
	  testFuncs.myDebugPrinting("Step 3 - Login with an  Operation user (System), check that you can see the menu and logout");
	  testFuncs.login(driver, testVars.getOperUsername(), testVars.getOperPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	  testFuncs.enterMenu(driver, "Setup_Phone_conf_phone_configuration_files", "Manage Configuration Files");
	  testFuncs.enterMenu(driver, "General_Informatiom_logout", testVars.getMainPageStr());
	  
	  // Step 4 - Login with an  Operation user (Tenant), check that you can see the menu
	  testFuncs.myDebugPrinting("Step 4 - Login with a  Operation user (Tenant) and check that you cannot see the menu");
	  testFuncs.login(driver, testVars.getOperUsername(), testVars.getOperPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	  testFuncs.enterMenu(driver, "Setup_Phone_conf_phone_configuration_files", "Manage Configuration Files");
	  testFuncs.enterMenu(driver, "General_Informatiom_logout", testVars.getMainPageStr());
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
