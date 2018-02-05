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
* This test tests a duplicate create at Site configuration menu
* ----------------
* Tests:
* 	 - Login and enter the Site configuration menu
* 	 - Create a site configuration and site PH.
* 	   1. Try to create another site configuration with the same name.
* 	   2. Try to create another site PH with the same name.
* 	   3. Delete the created site configuration and site PH.
* 
* Results:
* 	 1+2. The add should fail with appropriate error prompt.
* 	   3. The created site configuration and site PH should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test136__duplicate_site_configuration_ph {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test136__duplicate_site_configuration_ph(String browser) {
	  
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
  public void Duplicate_Site_configuration_ph() throws Exception {
	 	
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String Id 			   = testFuncs.getId();
	String siteCfgKeyName  = "user_name"   + Id;
	String siteCfgKeyValue = "userValue"   + Id;
	String sitePhName      = "sitePhName"  + Id;
	String sitePhValue     = "sitePhValue" + Id;
	String tenant 		   = testVars.getDefTenant();
	String site	  		   = testVars.getDefSite() + " [" + testVars.getDefSite() + "] / " + testVars.getDefTenant();
	 
	// Login and  Add new Site configuration and Site PH
	testFuncs.myDebugPrinting("Login and  Add new Site configuration and Site PH");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "Site_configuration", "Site Configuration");
	testFuncs.selectSite(driver, site);
    testFuncs.addNewSiteCfgKey(driver, siteCfgKeyName, siteCfgKeyValue, tenant, site);
	testFuncs.addSitePH(driver, sitePhName, sitePhValue, site, tenant);

	// Step 1 - Try to create another site configuration with the same name
	testFuncs.myDebugPrinting("Step 1 - Try to create another site configuration with the same name");
    testFuncs.addNewSiteCfgKey(driver, siteCfgKeyName, site);
	
	// Step 2 - Try to create another site PH with the same name
	testFuncs.myDebugPrinting("Step 2 - Try to create another site PH with the same name");
	testFuncs.addSitePH(driver, sitePhName, site, tenant);
    
	// Step 3 - Delete the Site PH and Site value
	testFuncs.myDebugPrinting("Step 3 - Delete the Site PH and Site value");
	testFuncs.enterMenu(driver, "Site_configuration", "Site Configuration");   
	testFuncs.selectSite(driver, site);
	testFuncs.deleteSiteCfgKey(driver, siteCfgKeyName, siteCfgKeyValue, tenant, site);    
	testFuncs.deleteSitePH(driver, sitePhName, sitePhValue, testVars.getDefSite() + " [" + testVars.getDefSite() + "]");
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
