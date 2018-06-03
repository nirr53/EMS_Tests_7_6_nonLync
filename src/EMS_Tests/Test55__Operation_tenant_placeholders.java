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
* This test tests the tenant placeholders menu via an Operation users (system +tenant)
* ----------------
* Tests:
* 	 - Enter Tenant placeholders menu via an Operation user (system).
* 	 1. Add a tenant placeholder.
* 	 2. Edit an existing tenant placeholder.
* 	 3. Delete a tenant placeholder.
* 	 4. Copy placeholders from another tenant.
* 
* 	 - Enter Tenant placeholders menu via an Operation user (tenant).
* 	 5. Add a tenant placeholder.
* 	 6. Edit an existing tenant placeholder.
* 	 7. Delete a tenant placeholder.
* 	 8. Copy placeholders from another tenant.
* 
* Results:
* 	 1-8. All actions should work successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test55__Operation_tenant_placeholders {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test55__Operation_tenant_placeholders(browserTypes browser) {
	  
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
  public void Operation_tenant_placeholders_menu() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String Id 	         = testFuncs.getId();
	String tenPhName     = "tenPhName"     + Id;
	String tenPhValue    = "tenPhValue"    + Id;
	String tenNewPhValue = "tenNewPhValue" + Id;
	String tenTenant     = testVars.getDefTenant();
	String tenNewTenant  = testVars.getNonDefTenant(0);
	
	// Login as Operation user (system) and enter the Tenant configuration menu
	testFuncs.myDebugPrinting("Login as Operation user (system) and enter the Tenant configuration menu");
	testFuncs.login(driver, testVars.getOperUsername(), testVars.getOperPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "Tenant_configuration", "Tenant Configuration");
	
	// Set Tenant
	Select currTen = new Select(driver.findElement(By.xpath("//*[@id='tenant_id']")));
	currTen.selectByVisibleText(testVars.getDefTenant());
	testFuncs.myWait(2000);
	 	
	// Step 1 - Add a new Tenant Placeholder
	testFuncs.myDebugPrinting("Step 1 - Add a new Tenant Placeholder");
	testFuncs.addTenantPH(driver, tenPhName, tenPhValue, tenTenant);
	
	// Step 2 - Edit the created Tenant Placeholder
	testFuncs.myDebugPrinting("Step 2 - Edit the created Tenant Placeholder");
	testFuncs.editTenantPH(driver, tenPhName, tenNewPhValue);
	
	// Step 3 - Delete the created Tenant Placeholder
	testFuncs.myDebugPrinting("Step 3 - Delete the created Tenant Placeholder");
	testFuncs.deleteTenantPH(driver, tenPhName, tenNewPhValue);
    
	// Step 4 - Copy Tenant placeholder from another tenant
	testFuncs.myDebugPrinting("Step 4 - Copy Tenant placeholders from another tenant");
	testFuncs.addTenantPH(driver, tenPhName, tenPhValue, tenTenant);
    testFuncs.copyPlaceholder(driver, tenNewTenant, tenPhName, tenPhValue, tenTenant);
	testFuncs.deleteTenantPH(driver, tenPhName, tenPhValue);
	currTen = new Select(driver.findElement(By.xpath("//*[@id='tenant_id']")));
	currTen.selectByVisibleText(testVars.getDefTenant());
	testFuncs.myWait(2000);
	testFuncs.deleteTenantPH(driver, tenPhName, tenPhValue);
	
	// Logout, re-login as Operation user (tenant) and enter the Tenant configuration menu
	testFuncs.myDebugPrinting("Logout, re-login as Operation user (tenant) and enter the Tenant configuration menu");
	testFuncs.enterMenu(driver, "General_Informatiom_logout", testVars.getMainPageStr());
	testFuncs.login(driver, testVars.getOperUsername(), testVars.getOperPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "Tenant_configuration", "Tenant Configuration");
	 	
	// Step 5 - Add a new Tenant Placeholder
	testFuncs.myDebugPrinting("Step 5 - Add a new Tenant Placeholder");
	testFuncs.addTenantPH(driver, tenPhName, tenPhValue, tenTenant);
	
	// Step 6 - Edit the created Tenant Placeholder
	testFuncs.myDebugPrinting("Step 6 - Edit the created Tenant Placeholder");
	testFuncs.editTenantPH(driver, tenPhName, tenNewPhValue);
	
	// Step 7 - Delete the created Tenant Placeholder
	testFuncs.myDebugPrinting("Step 7 - Delete the created Tenant Placeholder");
	testFuncs.deleteTenantPH(driver, tenPhName, tenNewPhValue);
    
	// Step 8 - Copy Tenant placeholder from another tenant
	testFuncs.myDebugPrinting("Step 8 - Copy Tenant placeholders from another tenant");
	testFuncs.addTenantPH(driver, tenPhName, tenPhValue, tenTenant);
    testFuncs.copyPlaceholder(driver, tenNewTenant, tenPhName, tenPhValue, tenTenant);
	testFuncs.deleteTenantPH(driver, tenPhName, tenNewPhValue);
	Select currTen2 = new Select(driver.findElement(By.xpath("//*[@id='tenant_id']")));
	currTen2.selectByVisibleText(testVars.getDefTenant());
	testFuncs.myWait(2000);
	testFuncs.deleteTenantPH(driver, tenPhName, tenPhValue);
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
