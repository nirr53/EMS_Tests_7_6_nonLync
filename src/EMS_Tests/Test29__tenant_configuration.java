package EMS_Tests;

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
* This test tests the Tenant configuration menu
* ----------------
* Tests:
* 	 - Enter Tenant configuration menu.
* 	 1. Add a new CFG key.
* 	 2. Delete the created CFG key.
*    3. Copy Values from another tenant.
*    4. Delete  configuration values from the tenants
* 
* Results:
* 	 1. CFG key should be added successfully.
* 	 2. CFG key should be deleted successfully.
*    3. CFG key should be copied successfully.
*    4. All values should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test29__tenant_configuration {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test29__tenant_configuration(browserTypes browser) {
	  
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
  public void Tenant_configuration_values() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	 
	// Enter the Add new Tenant configuration menu
	testFuncs.myDebugPrinting("Enter the Add new Tenant configuration menu");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "Tenant_configuration", "Tenant Configuration");
	String Id = testFuncs.getId();
	String cfgKeyName      = "user_name" + Id;
	String cfgKeyValue     = "userValue" + Id;

//	// Step 1 - Add a new CFG key
//	testFuncs.myDebugPrinting("Step 1 - Add a new CFG key");
//	testFuncs.addNewCfgKey(driver, cfgKeyName, cfgKeyValue, testVars.getDefTenant());
//    
//	// Step 2 - Delete a CFG key
//	testFuncs.myDebugPrinting("Step 2 - Delete a CFG key");
//	testFuncs.deleteCfgKey(driver, cfgKeyName, cfgKeyValue, testVars.getDefTenant());
	
	// Step 3 - Copy a CFG key from other tenant
	testFuncs.myDebugPrinting("Step 3 - Copy a CFG key from other tenant");
    copyCFGkey(driver, testVars.getDefTenant(), testVars.getNonDefTenant(0), cfgKeyName, cfgKeyValue);
	
//	// Step 4 - Delete  configuration values from the tenants
//	testFuncs.myDebugPrinting("Step 4 - Delete  configuration values from the tenants");
//    testFuncs.deleteAllConfValues(driver , cfgKeyName, testVars.getDefTenant());
//    testFuncs.selectTenant(driver, testVars.getNonDefTenant(0));
//    testFuncs.deleteAllConfValues(driver , cfgKeyName, testVars.getNonDefTenant(0));
  }
  
  // Copy a configuration value from other tenant
  private void copyCFGkey(WebDriver driver, String tenWeCopyTo, String tenWeCopyFrom, String cfgKeyName, String cfgKeyValue) {
	
	  // Create a new CFG key on other Tenant
	  testFuncs.addNewCfgKey(driver, cfgKeyName, cfgKeyValue, tenWeCopyFrom);
	  testFuncs.myWait(60000);

	  // Select a new tenant
	  testFuncs.myDebugPrinting("Select a new tenant (" + tenWeCopyTo + ")", enumsClass.logModes.MINOR);	  
	  Select currentTenant = new Select(driver.findElement(By.xpath("//*[@id='tenant_id']")));
	  currentTenant.selectByVisibleText(tenWeCopyTo);
	  testFuncs.myWait(10000);
	  
	  // Copy values from other tenant
	  testFuncs.myDebugPrinting("Copy values from other tenant", enumsClass.logModes.NORMAL);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[3]/div[2]/div[1]/div[5]/div[2]/button")    , 5000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[3]/div[2]/div[1]/div[5]/div[2]/ul/li[3]/a"), 7000);
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalTitleId']"  , "Copy Configuration ( " + tenWeCopyTo + " )");
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalContentId']", "Please select configuration setting to copy from.");
	  new Select(driver.findElement(By.xpath("/html/body/div[2]/div/select"))).selectByVisibleText("AutoDetection");
	  testFuncs.myWait(5000);
	  new Select(driver.findElement(By.xpath("/html/body/div[2]/div/select"))).selectByVisibleText(tenWeCopyFrom);
	  testFuncs.myWait(15000);
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 15000);
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalTitleId']"  , "Save Configuration ( " + testVars.getDefTenant() + " )");
	  testFuncs.myWait(10000);
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalContentId']", "Tenant configuration was saved successfully.");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);	  
			  
	  // Verify copy
	  testFuncs.searchStr(driver, cfgKeyName);
	  testFuncs.searchStr(driver, cfgKeyValue);
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
