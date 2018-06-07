package EMS_Tests;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

import EMS_Tests.enumsClass.*;

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
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test136__duplicate_site_configuration_ph(browserTypes browser) {
	  
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
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_SITE_CONFIGURATION, "Site Configuration");
	testFuncs.selectSite(driver, site);
    testFuncs.addNewSiteCfgKey(driver, siteCfgKeyName, siteCfgKeyValue, tenant, site);
	testFuncs.addSitePH(driver, sitePhName, sitePhValue, site, tenant);

	// Step 1 - Try to create another site configuration with the same name
	testFuncs.myDebugPrinting("Step 1 - Try to create another site configuration with the same name");
    addNewSiteCfgKey(driver, siteCfgKeyName, site);
	
	// Step 2 - Try to create another site PH with the same name
	testFuncs.myDebugPrinting("Step 2 - Try to create another site PH with the same name");
	addSitePH(driver, sitePhName, site, tenant);
    
	// Step 3 - Delete the Site PH and Site value
	testFuncs.myDebugPrinting("Step 3 - Delete the Site PH and Site value");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_SITE_CONFIGURATION, "Site Configuration");   
	testFuncs.selectSite(driver, site);
	testFuncs.deleteSiteCfgKey(driver, siteCfgKeyName, siteCfgKeyValue, tenant, site, testVars.getDefSite());    
	testFuncs.deleteSitePH(driver, sitePhName, sitePhValue, testVars.getDefSite() + " [" + testVars.getDefSite() + "]");
  }
  
  /**
  *  Create an existing Site-PH with given variables
  *  @param driver      - given element
  *  @param sitePhName  - given Site-PH name
  *  @param sitePHSite  - given Site-PH site
  *  @param siteTenant  - given Site-PH tenant
  */
  private void addSitePH(WebDriver driver, String sitePhName, String sitePHSite, String siteTenant) {
	  
	  // Add an existing Site-PH	  
	  testFuncs.myDebugPrinting("Add an existing Site-PH", enumsClass.logModes.NORMAL);  
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[4]/div[2]/div/span[2]/a"), 5000);	
	  testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3", "Add new placeholder");		
	  Select siteId = new Select(driver.findElement(By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[4]/select")));
	  siteId.selectByVisibleText(sitePHSite);	
	  testFuncs.myWait(5000);
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='ph_name']") , sitePhName , 2000);
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='ph_value']"), "1234", 2000);	
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button[2]"), 5000);
	  testFuncs.searchStr(driver, "Failed to save new placeholder. This name is exist.");	
  }
  
  /**
  *  Add an existing site CFG key according to given data
  *  @param driver       - given element
  *  @param cfgKeyName   - given configuration name
  *  @param currSite     - given configuration site
  *  @throws IOException 
  */
  private void addNewSiteCfgKey(WebDriver driver, String cfgKeyName, String currSite) {  
	  
	  // Select site
	  testFuncs.myDebugPrinting("Select site", enumsClass.logModes.MINOR);	  
	  testFuncs.selectSite(driver, currSite); 
	  
	  // Select key, set data and submit
	  testFuncs.myDebugPrinting("Select key, set data and submit", enumsClass.logModes.MINOR);	  
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='ini_name']") , cfgKeyName  , 2000);  
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='ini_value']"), "1234", 2000);  	  
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[3]/div[2]/div[1]/div[3]/a"), 3000);	
	  
	  // Verify that appropriate error prompt is displayed
	  testFuncs.myDebugPrinting("Verify that appropriate error prompt is displayed", enumsClass.logModes.MINOR);
	  testFuncs.searchStr(driver, "This setting name is already in use.");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[2]"), 3000); 
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
