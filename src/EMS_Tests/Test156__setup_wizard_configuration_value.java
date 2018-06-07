package EMS_Tests;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
* This test tests the Setup wizard menu
* ----------------
* Tests:
* 	 - Login and enter the Setup Wizard menu 
* 	 - Enter the User-configuration menu
* 	 1. Add Tenant configuration value
* 	 2. Edit Tenant configuration value
* 	 3. Delete Tenant configuration value
* 	 4. Add a Tenant configuration value from features TAB
* 	 5. Delete all values via the Features tab
* 
* Results:
* 	 1-5. As excepted.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test156__setup_wizard_configuration_value {
	
  private browserTypes  usedBrowser;
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test156__setup_wizard_configuration_value(browserTypes browser) {
	  
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
  public void Setup_wizrd_configuration_value() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String usedTenant     = testVars.getDefTenant();
	String Id 	   	      = testFuncs.getId();
	String tenConfName    = "tenantConfName"  + Id;
	String tenConfValue   = "tenantConfValue" + Id;
	String tenantValueKey = "system/daylight_saving/activate";

	// Login the system and enter Setup-Wizard menu
	testFuncs.myDebugPrinting("Login the system and enter Setup-Wizard menu");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_SETUP_WIZARD, "System Properties");

	// Enter the User-configuration menu
	testFuncs.myDebugPrinting("Enter the User-configuration menu");
	testFuncs.myClick(driver, By.xpath("//*[@id='sfb']")								, 4000);
	testFuncs.myClick(driver, By.xpath("//*[@id='smartwizard']/nav[2]/div[2]/button[2]"), 4000);
    List<WebElement>radioButton = driver.findElements(By.xpath("//*[@id='nozerotouch']"));
	radioButton.get(0).click();
	testFuncs.myClick(driver, By.xpath("//*[@id='smartwizard']/nav[2]/div[2]/button[2]"), 4000);
	new Select(driver.findElement(By.xpath("//*[@id='step-2']/div/div[2]/div[1]/select"))).selectByVisibleText(usedTenant);
	testFuncs.myWait(4000);
	testFuncs.myClick(driver, By.xpath("//*[@id='smartwizard']/nav[2]/div[2]/button[2]"), 4000);
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-3']/h2"							  	 , "Tenant Configuration");
	
    // Step 1 - Add tenant configuration
	testFuncs.myDebugPrinting("Step 1 - Add tenant configuration");
	addTenantConfigration(tenConfName, tenConfValue, usedTenant);
	
    // Step 2 - Edit tenant configuration
	testFuncs.myDebugPrinting("Step 2 - Edit tenant configuration");
	String newTenConfValue = "newTenantConfValue" + Id;;
	editConfigration(tenConfName, tenConfValue, newTenConfValue, usedTenant);
	
    // Step 3 - Delete tenant configuration
	testFuncs.myDebugPrinting("Step 3 - Delete tenant configuration");
	deleteConfigration(tenConfName, newTenConfValue, usedTenant);
	
	// Step 4 - Add a Tenant configuration value from Features Tab
	testFuncs.myDebugPrinting("Step 4 - Add a Tenant configuration value from Features Tab");
	Map<String, String> newDataMap = new HashMap<String, String>();
	newDataMap.put("valueName" , "dayLightSaving"); 
	newDataMap.put("valueKey"  , tenantValueKey); 
	newDataMap.put("usedTenant", usedTenant); 
	featuresConfigration(newDataMap);
	
	// Step 5 - Delete all Tenant configuration values using Features Tab
	testFuncs.myDebugPrinting("Step 4 - Add a Tenant configuration value from Features Tab");
	newDataMap.put("valueName"	   ,  "deleteAllValues"); 
	newDataMap.put("valueForVerify",  tenantValueKey); 
	featuresConfigration(newDataMap);	
  }

  // Add a Tenant configuration value from features TAB
  private void featuresConfigration(Map<String, String> newDataMap) {

	  // Add a Tenant configuration value from features TAB
	  testFuncs.myDebugPrinting("Add a Tenant configuration value from features TAB", enumsClass.logModes.NORMAL);
	  String valueName  = newDataMap.get("valueName");
	  String usedTenant = newDataMap.get("usedTenant");
	  switch (valueName) {
	  
	  		case "dayLightSaving":
	  			
	  			// Add value
	  			testFuncs.myDebugPrinting("Add dayLight saving value", enumsClass.logModes.NORMAL);
	  			testFuncs.myClick(driver, By.xpath("//*[@id='step-3']/div/div[2]/div/div[1]/div[5]/div[1]/button"), 4000);
	  			testFuncs.myClick(driver, By.xpath("//*[@id='daylight']")										  , 4000);			 
	  			testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Daylight Savings Time");			
	  			testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);
	  			testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Save Configuration ( " + usedTenant + " )");
	  			testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Tenant configuration was saved successfully.");		
	  			testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);

	  			// Verify add
	  			testFuncs.myDebugPrinting("Verify add", enumsClass.logModes.MINOR);
	  			// Nir 4\6\18 - bug here, menu return to AutoDetection tenant	
	  			testFuncs.myClick(driver, By.xpath("//*[@id='smartwizard']/nav[1]/div[2]/button[1]"), 5000);
	  			new Select(driver.findElement(By.xpath("//*[@id='step-2']/div/div[2]/div[1]/select"))).selectByVisibleText(testVars.getNonDefTenant(1));
	  			testFuncs.myWait(4000);  
	  			new Select(driver.findElement(By.xpath("//*[@id='step-2']/div/div[2]/div[1]/select"))).selectByVisibleText(usedTenant);	  
	  			testFuncs.myWait(4000);		  
	  			testFuncs.myClick(driver, By.xpath("//*[@id='smartwizard']/nav[2]/div[2]/button[2]"), 4000);
	  			testFuncs.verifyStrByXpath(driver, "//*[@id='step-3']/h2", "Tenant Configuration");		  
	  			testFuncs.searchStr(driver, newDataMap.get("valueKey"));
	  			break;
	  			
	  		case "deleteAllValues":
	  			
	  			// Delete all configuration values
	  			testFuncs.myDebugPrinting("Delete all configuration values", enumsClass.logModes.NORMAL);
	  			testFuncs.myClick(driver, By.xpath("//*[@id='step-3']/div/div[2]/div/div[1]/div[5]/div[2]/button")         , 4000);
	  			testFuncs.myClick(driver, By.xpath("//*[@id='step-3']/div/div[2]/div/div[1]/div[5]/div[2]/ul/li[1]/a/span"), 4000);
	  			testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete configuration settings");
	  			testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete all configuration settings and save empty content?");		
	  			testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);
	  			testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Save Configuration ( " + usedTenant + " )");
	  			testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Tenant configuration was saved successfully.");		
	  			testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);

	  			// Verify add
	  			testFuncs.myDebugPrinting("Verify add", enumsClass.logModes.MINOR);
	  			// Nir 4\6\18 - bug here, menu return to AutoDetection tenant	
	  			testFuncs.myClick(driver, By.xpath("//*[@id='smartwizard']/nav[1]/div[2]/button[1]"), 5000);
	  			new Select(driver.findElement(By.xpath("//*[@id='step-2']/div/div[2]/div[1]/select"))).selectByVisibleText(testVars.getNonDefTenant(1));
	  			testFuncs.myWait(4000);  
	  			new Select(driver.findElement(By.xpath("//*[@id='step-2']/div/div[2]/div[1]/select"))).selectByVisibleText(usedTenant);	  
	  			testFuncs.myWait(4000);		  
	  			testFuncs.myClick(driver, By.xpath("//*[@id='smartwizard']/nav[2]/div[2]/button[2]"), 4000);
	  			testFuncs.verifyStrByXpath(driver, "//*[@id='step-3']/h2", "Tenant Configuration");		  
	  			String valueForVerify = newDataMap.get("valueForVerify");
	  			testFuncs.myAssertTrue("Tenant values still detected !! (" + valueForVerify + ")", !driver.findElement(By.tagName("body")).getText().contains(valueForVerify));
	  			break;
	  }
  }

  // Delete Tenant configuration value
  private void deleteConfigration(String tenConfName, String newTenConfValue, String usedTenant) {
	
	  // Delete Tenant configuration value
	  testFuncs.myDebugPrinting("Delete Tenant configuration value", enumsClass.logModes.NORMAL);
	  
	  // Get index of the deleted value
	  testFuncs.myDebugPrinting("Get index of the deleted value", enumsClass.logModes.MINOR);
	  for (int i = 1;; ++i) {
		  
		  String tempName = driver.findElement(By.xpath("//*[@id='table_keys']/tbody/tr[" + i + "]/td[1]")).getText();
		  testFuncs.myDebugPrinting("tempName - " + tempName, enumsClass.logModes.MINOR);
		  if (tempName.contains(tenConfName)) {
			  
			  testFuncs.myDebugPrinting("match was detected !! <" + i + ">", enumsClass.logModes.MINOR);
			  testFuncs.myClick(driver, By.xpath("//*[@id='table_keys']/tbody/tr[" + i + "]/td[3]/div/a"), 4000);
			  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete configuration setting");
			  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete the " + tempName + " from the configuration settings?");		
			  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);	  
			  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Save Configuration ( " + usedTenant + " )");
			  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Tenant configuration was saved successfully.");		
			  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);
			  
			  // Verify delete
			  testFuncs.myDebugPrinting("Verify delete", enumsClass.logModes.MINOR);
			  // Nir 4\6\18 - bug here, menu return to AutoDetection tenant
			  testFuncs.myClick(driver, By.xpath("//*[@id='smartwizard']/nav[1]/div[2]/button[1]"), 5000);
			  new Select(driver.findElement(By.xpath("//*[@id='step-2']/div/div[2]/div[1]/select"))).selectByVisibleText(testVars.getNonDefTenant(1));
			  testFuncs.myWait(4000);
			  new Select(driver.findElement(By.xpath("//*[@id='step-2']/div/div[2]/div[1]/select"))).selectByVisibleText(usedTenant);
			  testFuncs.myWait(4000);
			  testFuncs.myClick(driver, By.xpath("//*[@id='smartwizard']/nav[2]/div[2]/button[2]"), 4000);
			  testFuncs.verifyStrByXpath(driver, "//*[@id='step-3']/h2", "Tenant Configuration");
			  testFuncs.myAssertTrue("Value still detected !!", !driver.findElement(By.tagName("body")).getText().contains(tempName));	  
			  break;
		  }
	  }
  }

  // Edit an existing Tenant configuration value
  private void editConfigration(String tenConfName, String tenConfValue, String newTenConfValue, String usedTenant) {
	  
	  // Edit an existing Tenant configuration value
	  testFuncs.myDebugPrinting("Edit an existing Tenant configuration value", enumsClass.logModes.NORMAL);			
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='ini_name']") , tenConfName , 4000);
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='ini_value']"), newTenConfValue, 4000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='step-3']/div/div[2]/div/div[1]/div[3]/a/span"), 7000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Add Setting");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "This setting name is already in use.\nAre you sure you want to replace " + tenConfValue + " to " + newTenConfValue);		
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Save Configuration ( " + usedTenant + " )");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Tenant configuration was saved successfully.");		
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);
	  
	  // Verify edit
	  // Nir 4\6\18 - bug here, menu return to AutoDetection tenant
	  testFuncs.myDebugPrinting("Verify edit", enumsClass.logModes.MINOR);			
	  testFuncs.myClick(driver, By.xpath("//*[@id='smartwizard']/nav[1]/div[2]/button[1]"), 5000);
	  new Select(driver.findElement(By.xpath("//*[@id='step-2']/div/div[2]/div[1]/select"))).selectByVisibleText(testVars.getNonDefTenant(1));
	  testFuncs.myWait(4000);
	  new Select(driver.findElement(By.xpath("//*[@id='step-2']/div/div[2]/div[1]/select"))).selectByVisibleText(usedTenant);
	  testFuncs.myWait(4000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='smartwizard']/nav[2]/div[2]/button[2]"), 4000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='step-3']/h2", "Tenant Configuration");
	  testFuncs.searchStr(driver, tenConfName + " " + newTenConfValue);
  }

  // Add new Tenant configuration
  private void addTenantConfigration(String tenConfName, String tenConfValue, String usedTenant) {
	  
	  // Add new Tenant configuration key
	  testFuncs.myDebugPrinting("Add new Tenant configuration", enumsClass.logModes.NORMAL);			
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='ini_name']") , tenConfName , 4000);
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='ini_value']"), tenConfValue, 4000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='step-3']/div/div[2]/div/div[1]/div[3]/a/span"), 7000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Save Configuration ( " + usedTenant + " )");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Tenant configuration was saved successfully.");		
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);
	  
	  // Verify create
	  // Nir 4\6\18 - bug here, menu return to AutoDetection tenant
	  testFuncs.myDebugPrinting("Verify create", enumsClass.logModes.MINOR);			
	  testFuncs.myClick(driver, By.xpath("//*[@id='smartwizard']/nav[1]/div[2]/button[1]"), 5000);
	  new Select(driver.findElement(By.xpath("//*[@id='step-2']/div/div[2]/div[1]/select"))).selectByVisibleText(testVars.getNonDefTenant(1));
	  testFuncs.myWait(4000);
	  new Select(driver.findElement(By.xpath("//*[@id='step-2']/div/div[2]/div[1]/select"))).selectByVisibleText(usedTenant);
	  testFuncs.myWait(4000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='smartwizard']/nav[2]/div[2]/button[2]"), 4000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='step-3']/h2", "Tenant Configuration");
	  testFuncs.searchStr(driver, tenConfName + " " + tenConfValue);
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
