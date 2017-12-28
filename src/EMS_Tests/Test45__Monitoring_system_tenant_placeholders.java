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

/**
* ----------------
* This test tests the region placeholders menu in Monitoring user (system + tenant)
* ----------------
* Tests:
* 	 - Login via an Administrator user, create a tenant-PH and logout
* 	 1. Login via a Monitoring user (system) and try to add a new Tenant Placeholder
* 	 2. Try to copy tenant-PH from another Tenant
* 	 3. Try to edit a Tenant-PH
* 	 4. Try to delete a Tenant-PH
* 	 5. Verify that Features menu is disabled
*    6. Verify that Actions menu is disabled
*    7. Verify that Add-Configuration-key button is disabled
* 	 8. Logout, relogin as an Administrator and delete the Tenant-PH
* 
* Results:
*    2-7. All the operation should be not active while we in Monitoring user.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test45__Monitoring_system_tenant_placeholders {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test45__Monitoring_system_tenant_placeholders(String browser) {
	  
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
  public void Monitoring_system_tenant_placeholders_menu() throws Exception {
	
	Log.startTestCase(this.getClass().getName());
	
	// Set vars
	String Id 	         = testFuncs.getId();
	String tenPhName     = "tenMonitPhName"  + Id;
	String tenPhValue    = "tenMonitPhValue" + Id;
	String tenTenant     = testVars.getDefTenant();
	
	// Login via an Administrator user, create a tenant-PH and logout
	testFuncs.myDebugPrinting("Login via an Administrator user, create a tenant-PH and logout");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "Tenant_configuration", "Tenant Configuration");
	testFuncs.addTenantPH(driver, tenPhName, tenPhValue, tenTenant);
	testFuncs.enterMenu(driver, "General_Informatiom_logout", testVars.getMainPageStr());
	
	// Step 1 - Login via a Monitoring user (system) and try to add a new Tenant Placeholder
	testFuncs.myDebugPrinting("Step 1 - Login via a Monitoring user (system) and try to add a new Tenant Placeholder");
	testFuncs.login(driver, testVars.getMonitUsername(), testVars.getMonitPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "Tenant_configuration", "Tenant Configuration");
	addTenantPHMonitoring(driver);

	// Step 2 - Try to copy tenant-PH from another Tenant
	testFuncs.myDebugPrinting("Step 2 - Try to copy tenant-PH from another Tenant");
	testFuncs.enterMenu(driver, "Tenant_configuration", "Tenant Configuration");
	copyTenantPHMonitoring(driver);
	
	// Step 3 - Try to edit a Tenant-PH
	testFuncs.myDebugPrinting("Step 3 - Try to edit a Tenant-PH");
	testFuncs.enterMenu(driver, "Tenant_configuration", "Tenant Configuration");
	Select tenId = new Select(driver.findElement(By.xpath("//*[@id='tenant_id']")));
	tenId.selectByVisibleText(tenTenant);
	testFuncs.mySendKeys(driver, By.xpath("//*[@id='tenants1-filtering']"), tenPhName , 6000);	
	testFuncs.editTenantPH(driver, tenPhName, tenPhValue);
	
	// Step 4 - Try to delete a Tenant-PH
	testFuncs.myDebugPrinting("Step 4 - Try to delete a Tenant-PH");
	testFuncs.enterMenu(driver, "Tenant_configuration", "Tenant Configuration");
	testFuncs.deleteTenantPH(driver, tenPhName, tenPhValue);
	
	// Step 5 - Verify that Actions menu is disabled
	testFuncs.myDebugPrinting("Step 5 - Verify that Actions menu is disabled");
	String actions = driver.findElement(By.xpath("//*[@id='contentwrapper']/section/div/div[3]/div[2]/div[1]/div[5]/div[1]/button")).getAttribute("class");
	testFuncs.myAssertTrue("Actions menu is enbled !!", actions.contains("not-active"));
	 	
	// Step 6 - Verify that Add-Configuration-key button is disabled
	testFuncs.myDebugPrinting("Step 6 - Verify that Add-Configuration-key button is disabled");
	String addCfgKey = driver.findElement(By.xpath("//*[@id='contentwrapper']/section/div/div[3]/div[2]/div[1]/div[3]/a")).getAttribute("class");
	testFuncs.myAssertTrue("Add configuration-key button is enbled !!", addCfgKey.contains("not-active"));
	
	// Step 7 - Verify that Add Site-PH is disabled
	testFuncs.myDebugPrinting("Step 7 - Verify that Add Site-PH is disabled");	
	String addSitePHKey = driver.findElement(By.xpath("//*[@id='contentwrapper']/section/div/div[4]/div[2]/div/span[2]/a")).getAttribute("class");
	testFuncs.myAssertTrue("Add Site-PH button is active !!", addSitePHKey.contains("not-active"));

	// Step 8 - Logout, relogin as an Administrator and delete the Tenant-PH
	testFuncs.myDebugPrinting("Step 8 - Logout, relogin as an Administrator and delete the Tenant-PH");
	testFuncs.enterMenu(driver, "General_Informatiom_logout", testVars.getMainPageStr());
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "Tenant_configuration", "Tenant Configuration");
	tenId = new Select(driver.findElement(By.xpath("//*[@id='tenant_id']")));
	tenId.selectByVisibleText(tenTenant);
	testFuncs.deleteTenantPH(driver, tenPhName, "Forced");
  }
  
  @Test
  public void Monitoring_tenant_tenant_placeholders_menu() throws Exception {
	
	Log.startTestCase(this.getClass().getName());
	String Id 	         = testFuncs.getId();
	String tenPhName     = "tenMonitPhName"  + Id;
	String tenPhValue    = "tenMonitPhValue" + Id;
	String tenTenant     = testVars.getDefTenant();
	
	// Login via an Administrator user, create a tenant-PH and logout
	testFuncs.myDebugPrinting("Login via an Administrator user, create a tenant-PH and logout");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "Tenant_configuration", "Tenant Configuration");
	testFuncs.addTenantPH(driver, tenPhName, tenPhValue, tenTenant);
	testFuncs.enterMenu(driver, "General_Informatiom_logout", testVars.getMainPageStr());
	
	// Step 1 - Login via a Monitoring user (tenant) and try to add a new Tenant Placeholder
	testFuncs.myDebugPrinting("Step 1 - Login via a Monitoring user (tenant) and try to add a new Tenant Placeholder");
	testFuncs.login(driver, testVars.getMonitTenUsername(), testVars.getMonitTenPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "Tenant_configuration", "Tenant Configuration");
	addTenantPHMonitoring(driver);

	// Step 2 - Try to copy tenant-PH from another Tenant
	testFuncs.myDebugPrinting("Step 2 - Try to copy tenant-PH from another Tenant");
	testFuncs.enterMenu(driver, "Tenant_configuration", "Tenant Configuration");
	copyTenantPHMonitoring(driver);
	
	// Step 3 - Try to edit a Tenant-PH
	testFuncs.myDebugPrinting("Step 3 - Try to edit a Tenant-PH");
	testFuncs.enterMenu(driver, "Tenant_configuration", "Tenant Configuration");
	Select tenId = new Select(driver.findElement(By.xpath("//*[@id='tenant_id']")));
	tenId.selectByVisibleText(tenTenant);
	testFuncs.mySendKeys(driver, By.xpath("//*[@id='tenants1-filtering']"), tenPhName , 6000);	
	testFuncs.editTenantPH(driver, tenPhName, tenPhValue);
	
	// Step 4 - Try to delete a Tenant-PH
	testFuncs.myDebugPrinting("Step 4 - Try to delete a Tenant-PH");
	testFuncs.enterMenu(driver, "Tenant_configuration", "Tenant Configuration");
	testFuncs.deleteTenantPH(driver, tenPhName, tenPhValue);
	
	// Step 5 - Verify that Actions menu is disabled
	testFuncs.myDebugPrinting("Step 5 - Verify that Actions menu is disabled");
	String actions = driver.findElement(By.xpath("//*[@id='contentwrapper']/section/div/div[3]/div[2]/div[1]/div[5]/div[1]/button")).getAttribute("class");
	testFuncs.myAssertTrue("Actions menu is enbled !!", actions.contains("not-active"));
	 	
	// Step 6 - Verify that Add-Configuration-key button is disabled
	testFuncs.myDebugPrinting("Step 6 - Verify that Add-Configuration-key button is disabled");
	String addCfgKey = driver.findElement(By.xpath("//*[@id='contentwrapper']/section/div/div[3]/div[2]/div[1]/div[3]/a")).getAttribute("class");
	testFuncs.myAssertTrue("Add configuration-key button is enbled !!", addCfgKey.contains("not-active"));
	
	// Step 7 - Verify that Add Site-PH is disabled
	testFuncs.myDebugPrinting("Step 7 - Verify that Add Site-PH is disabled");	
	String addSitePHKey = driver.findElement(By.xpath("//*[@id='contentwrapper']/section/div/div[4]/div[2]/div/span[2]/a")).getAttribute("class");
	testFuncs.myAssertTrue("Add Site-PH button is active !!", addSitePHKey.contains("not-active"));

	// Step 8 - Logout, relogin as an Administrator and delete the Tenant-PH
	testFuncs.myDebugPrinting("Step 8 - Logout, relogin as an Administrator and delete the Tenant-PH");
	testFuncs.enterMenu(driver, "General_Informatiom_logout", testVars.getMainPageStr());
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "Tenant_configuration", "Tenant Configuration");
	tenId = new Select(driver.findElement(By.xpath("//*[@id='tenant_id']")));
	tenId.selectByVisibleText(tenTenant);
	testFuncs.deleteTenantPH(driver, tenPhName, "Forced");
  }

  // Try to copy a Tenant-Ph via a Monitoring user
  private void copyTenantPHMonitoring(WebDriver driver) {
	  
	// Try to copy a Tenant-Ph via a Monitoring user
	testFuncs.myDebugPrinting("Try to copy a Tenant-Ph via a Monitoring user", testVars.logerVars.NORMAL);
	String copyButton = driver.findElement(By.xpath("//*[@id='contentwrapper']/section/div/div[4]/div[2]/div/span[1]/a")).getAttribute("class");
	testFuncs.myAssertTrue("Add button is not deactivated !!\nacopyButton - " + copyButton, copyButton.contains("not-active"));
  }

  // Try to add a Tenant-Ph via a Monitoring user
  private void addTenantPHMonitoring(WebDriver driver) {
	  
	  // Try to add a Tenant-Ph via a Monitoring user
	  testFuncs.myDebugPrinting("Try to add a Tenant-Ph via a Monitoring user", testVars.logerVars.NORMAL);
	  String addButton = driver.findElement(By.xpath("//*[@id='contentwrapper']/section/div/div[4]/div[2]/div/span[2]/a")).getAttribute("class");
	  testFuncs.myAssertTrue("Add button is not deactivated !!\naddButton - " + addButton, addButton.contains("not-active"));
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
