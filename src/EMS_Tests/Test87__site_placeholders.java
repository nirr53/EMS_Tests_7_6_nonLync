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
* This test tests the Site placeholder
* ----------------
* Tests:
* 	 - Enter Site configuration menu.
* 	 1. Add a new Site Placeholder
*    2. Edit the Site Tenant Placeholder
*    3. Delete the Site Tenant Placeholder
* 
* Results:
* 	 1. The Site Placeholder should be added successfully
* 	 2. The Site Placeholder should be edited successfully
* 	 3. The Site Placeholder should be deleted successfully
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test87__site_placeholders {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test87__site_placeholders(browserTypes browser) {
	  
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
  public void Site_placeholders() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String Id 	          = testFuncs.getId();
	String sitePhName     = "sitePhName"     + Id;
	String sitePhValue    = "sitePhValue"    + Id;
	String sitePhNewValue = "sitePhNewValue" + Id;
	String sitePHSite	  = testVars.getDefSite() + " [" + testVars.getDefSite() + "] / " + testVars.getDefTenant();
	String sitePHTenant	  = testVars.getDefTenant();
	
	testFuncs.myDebugPrinting("Enter the Sites configuration menu");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "Site_configuration", "Site Configuration");
	 	
	// Step 1 - Add a new Site Placeholder
	testFuncs.myDebugPrinting("Step 1 - Add a new Site Placeholder");
	testFuncs.addSitePH(driver, sitePhName, sitePhValue, sitePHSite, sitePHTenant);
	
	// Step 2 - Edit the created Site Placeholder
	testFuncs.myDebugPrinting("Step 2 - Edit the created Site Placeholder");
	testFuncs.editSitePH(driver, sitePhName, sitePhNewValue);
	
	// Step 3 - Delete the created Site Placeholder
	testFuncs.myDebugPrinting("Step 3 - Delete the created Site Placeholder");
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
