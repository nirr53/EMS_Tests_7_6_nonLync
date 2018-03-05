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
* This test tests a create of site configuration value and site-PH  when the site has special characters
* ----------------
* Tests:
* 	 - Enter Site configuration menu and select a Site with special characters
* 	 1. Add a configuration value
* 	 2. Delete configuration value
* 	 3. Add a site PH
* 	 4. Delete a site PH
*
* Results:
* 	 1-4. Actions should end successfully. 
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test130__site_special_characters {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test130__site_special_characters(String browser) {
	  
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
  public void Site_special_chars_configuration_key_tests() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String tenant 		   = testVars.getDefTenant();
	String sitePHSite	   = testVars.getSpecialCharsSite(0) + " [" + testVars.getDefSite() + "] / " + testVars.getDefTenant();
	String Id 			   = testFuncs.getId();
	String siteCfgKeyName  = "user_name"     + Id;
	String siteCfgKeyValue = "userValue"     + Id;
	String sitePhName     = "sitePhName"     + Id;
	String sitePhValue    = "sitePhValue"    + Id;
	String sitePHTenant	  = testVars.getDefTenant();	
	
	// Login and enter the View-Sites menu
	testFuncs.myDebugPrinting("Enter the Add new region placeholders menu");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "Site_configuration", "Site Configuration");
	testFuncs.selectSite(driver, sitePHSite);

	// Step 1 - Add a new site CFG key
	testFuncs.myDebugPrinting("Step 1 - Add a new site CFG key");
    testFuncs.addNewSiteCfgKey(driver, siteCfgKeyName, siteCfgKeyValue, tenant, sitePHSite);
    
	// Step 2 - Delete a site CFG key
	testFuncs.myDebugPrinting("Step 2 - Delete a site CFG key");
	testFuncs.selectSite(driver, sitePHSite);
	testFuncs.deleteSiteCfgKey(driver, siteCfgKeyName, siteCfgKeyValue, tenant, sitePHSite);
	
	// Step 3 - Add a new Site Placeholder
	testFuncs.myDebugPrinting("Step 3 - Add a new Site Placeholder");
	testFuncs.addSitePH(driver, sitePhName, sitePhValue, sitePHSite, sitePHTenant);
	
	// Step 4 - Delete the created Site Placeholder
	testFuncs.myDebugPrinting("Step 4 - Delete the created Site Placeholder");
	testFuncs.deleteSitePH(driver, sitePhName, sitePhValue, testVars.getSpecialCharsSite(0) + " [" + testVars.getDefSite() + "]");
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
