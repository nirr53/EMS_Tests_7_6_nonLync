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
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

import EMS_Tests.enumsClass.*;

/**
* ----------------
* This test tests a duplicate create at Tenant configuration menu
* ----------------
* Tests:
* 	 - Login and enter the Tenant configuration menu
* 	 - Create a tenant configuration and tenant PH.
* 	   1. Try to create another tenant configuration with the same name.
* 	   2. Try to create another tenant PH with the same name.
* 	   3. Delete the created tenant configuration and tenant PH.
* 
* Results:
* 	 1+2. The add should fail with appropriate error prompt.
* 	   3. The created tenant configuration and tenant PH should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test135__duplicate_tenant_configuration_ph {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test135__duplicate_tenant_configuration_ph(browserTypes browser) {
	  
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
  public void Duplicate_Tenant_configuration_ph() throws Exception {
	 	
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String Id = testFuncs.getId();
	String cfgKeyName  = "user_name"  + Id;
	String cfgKeyValue = "userValue"  + Id;
	String tenPhName   = "tenPhName"  + Id;
	String tenPhValue  = "tenPhValue" + Id;
	String tenTenant   = testVars.getDefTenant();
	 
	// Login and  Add new Tenant configuration and Tenant PH
	testFuncs.myDebugPrinting("Login and  Add new Tenant configuration and Tenant PH");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "Tenant_configuration", "Tenant Configuration");
	testFuncs.addNewCfgKey(driver, cfgKeyName, cfgKeyValue, tenTenant);
	testFuncs.addTenantPH(driver, tenPhName, tenPhValue, tenTenant);

	// Step 1 - Try to create another tenant configuration with the same name
	testFuncs.myDebugPrinting("Step 1 - Try to create another tenant configuration with the same name");
	addNewCfgKey(driver, cfgKeyName, tenTenant);
	
	// Step 2 - Try to create another tenant PH with the same name
	testFuncs.myDebugPrinting("Step 2 - Try to create another tenant PH with the same name");
	addTenantPH(driver, tenPhName, tenTenant);

	// Step 3 - Delete the Tenant PH and Tenant value
	testFuncs.myDebugPrinting("Step 3 - Delete the Tenant PH and Tenant value");
	testFuncs.enterMenu(driver, "Tenant_configuration", "Tenant Configuration");
	deleteCfgKey(driver, cfgKeyName, cfgKeyValue, testVars.getDefTenant());
	testFuncs.deleteTenantPH(driver, tenPhName, tenPhValue);
  }
  
  /**
  *  Create an existing Tenant-PH
  *  @param driver     - given element
  *  @param tenPhName  - given Tenant-ph name
  *  @param tenTenant  - given Tenant for the tenant-ph
  */
  private void addTenantPH(WebDriver driver, String tenPhName, String tenTenant) {
	  
	  // Fill data
	  testFuncs.myDebugPrinting("Fill data", enumsClass.logModes.NORMAL);  
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[4]/div[2]/div/span[2]/a"), 9000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3", "Add new placeholder");
	  Select tenId = new Select(driver.findElement(By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[4]/select")));
	  tenId.selectByVisibleText(tenTenant);
	  testFuncs.myWait(5000);
	  testFuncs.myDebugPrinting("tenPhName - "  + tenPhName ,enumsClass.logModes.MINOR);  
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='ph_name']") , tenPhName , 2000);
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='ph_value']"), "123", 2000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button[2]"), 5000);
		
	  // Verify that appropriate error prompt is displayed	
	  testFuncs.myDebugPrinting("Verify that appropriate error prompt is displayed", enumsClass.logModes.NORMAL);  	
	  testFuncs.searchStr(driver, "Failed to save new placeholder. This name is exist.");
  }
  
  /**
  *  Delete Tenant configuration key
  *  @param driver      - given driver
  *  @param cfgKeyName  - given configuration tenant name
  *  @param cfgKeyValue - given configuration tenant value
  *  @param currTenant  - given configuration tenant
  */  
  private void deleteCfgKey(WebDriver driver, String cfgKeyName, String cfgKeyValue, String currTenant) throws IOException {
	  	  
	  // Get idx
	  testFuncs.myDebugPrinting("Get idx", enumsClass.logModes.MINOR);
	  BufferedReader r = new BufferedReader(new StringReader(driver.findElement(By.tagName("body")).getText()));
	  String l = null;
	  int i = 1;
	  Boolean countLines = false;
	  while ((l = r.readLine()) != null) {
				
		  testFuncs.myDebugPrinting("i - " + i + " " + l, enumsClass.logModes.DEBUG);
		  if (l.contains(cfgKeyName)) {
					  
			  testFuncs.myDebugPrinting("i - " + i, enumsClass.logModes.MINOR);
			break;
		  } else if (countLines) {
			
			i++;
		  } else if (l.contains("Configuration Key Configuration Value")) {
			countLines = true;
		  }
	  }
   
	  // Delete key
	  testFuncs.myDebugPrinting("Delete key", enumsClass.logModes.MINOR);	  
	  testFuncs.myClick(driver, By.xpath("//*[@id='table_keys']/tbody/tr[" + i + "]/td[3]/div/a/i"), 7000);
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalTitleId']"  , "Delete configuration setting");
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalContentId']", "Are you sure you want to delete the " + cfgKeyName + " from the configuration settings?");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 7000); 
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalTitleId']"  , "Save Configuration ( " + currTenant + " )");
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalContentId']", "Tenant configuration was saved successfully.");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 7000);
			  
	  // Verify delete
	  testFuncs.myDebugPrinting("Verify delete", enumsClass.logModes.MINOR);	  
	  String txt = driver.findElement(By.tagName("body")).getText();
	  testFuncs.myAssertTrue("Delete did not succeeded !!\ntxt - " + txt,  !txt.contains(cfgKeyName));
	  testFuncs.myAssertTrue("Delete did not succeeded !!\ntxt - " + txt,  !txt.contains(cfgKeyValue));
  }
  
  /**
  *  Create an existing Tenant configuration key
  *  @param driver      - given driver
  *  @param cfgKeyName  - given configuration tenant name
  */
  private void addNewCfgKey(WebDriver driver, String cfgKeyName, String currTenant) {
	  
	  // Select tenant
	  testFuncs.myDebugPrinting("Select tenant", enumsClass.logModes.MINOR);	  
	  Select currentTenant = new Select(driver.findElement(By.xpath("//*[@id='tenant_id']")));
	  currentTenant.selectByVisibleText(currTenant);
	  testFuncs.myWait(20000);
	  
	  // Select key, set data and submit
	  testFuncs.myDebugPrinting("Add cfgKeyName - "  + cfgKeyName, enumsClass.logModes.MINOR);	
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='ini_name']"), cfgKeyName, 7000);
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='ini_value']"), "123"	, 7000);  
	  testFuncs.myWait(7000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[3]/div[2]/div[1]/div[3]/a"), 7000);

	  // Verify that appropriate error prompt is displayed
	  testFuncs.myDebugPrinting("Verify that appropriate error prompt is displayed", enumsClass.logModes.MINOR);
	  testFuncs.searchStr(driver, "This setting name is already in use");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[2]"), 7000);
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
