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
	testFuncs.addNewCfgKey(driver, cfgKeyName, tenTenant);
	
	// Step 2 - Try to create another tenant PH with the same name
	testFuncs.myDebugPrinting("Step 2 - Try to create another tenant PH with the same name");
	testFuncs.addTenantPH(driver, tenPhName, tenTenant);

	// Step 3 - Delete the Tenant PH and Tenant value
	testFuncs.myDebugPrinting("Step 3 - Delete the Tenant PH and Tenant value");
	testFuncs.enterMenu(driver, "Tenant_configuration", "Tenant Configuration");
	testFuncs.deleteCfgKey(driver, cfgKeyName, cfgKeyValue, testVars.getDefTenant());
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
