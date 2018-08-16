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
* This test tests the Site configuration menu
* ----------------
* Tests:
* 	 - Enter Site configuration menu.
* 	 1. Add a new Site-CFG key.
* 	 2. Delete the created Site-CFG key.
* 
* Results:
* 	 1. Site-CFG key should be added successfully.
* 	 2. Site-CFG key should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test82__site_configuration {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test82__site_configuration(browserTypes browser) {
	  
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
  public void Site_configuration_values() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	 
	// Set variables
	String Id = testFuncs.getId();
	String siteCfgKeyName  = "user_name" + Id;
	String siteCfgKeyValue = "userValue" + Id;
	String tenant 		   = testVars.getDefTenant();
	String site	  		   = testVars.getDefSite() + " [" + testVars.getDefSite() + "] / " + testVars.getDefTenant();
	
	testFuncs.myDebugPrinting("Enter the Site Configuration menu");
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_SITE_CONFIGURATION, "Site Configuration");

	// Step 1 - Add a new site CFG key
	testFuncs.myDebugPrinting("Step 1 - Add a new site CFG key");
	testFuncs.selectSite(driver, site);
    testFuncs.addNewSiteCfgKey(driver, siteCfgKeyName, siteCfgKeyValue, tenant, site);
    
	// Step 2 - Delete a site CFG key
	testFuncs.myDebugPrinting("Step 2 - Delete a site CFG key");
	testFuncs.selectSite(driver, site);
	testFuncs.deleteSiteCfgKey(driver, siteCfgKeyName, siteCfgKeyValue, tenant, site, testVars.getDefSite());
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
