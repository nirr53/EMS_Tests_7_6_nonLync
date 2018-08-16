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
import EMS_Tests.enumsClass.*;

/**
* ----------------
* This test tests the Template mapping of Setup wizard menu
* ----------------
* Tests:
* 	 - Login and enter the Setup Wizard menu 
* 	 - Enter the Template mapping menu
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
public class Test162__setup_wizard_template_mapping {
	
  private browserTypes  usedBrowser;
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test162__setup_wizard_template_mapping(browserTypes browser) {
	  
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
	String usedTemplate  = "importtemplate";
	String usedModels[]  = {"445HD", "450HD"};
	String usedTenants[] = {testVars.getNonDefTenant(0), testVars.getNonDefTenant(1)};
	String usedTenant    = testVars.getDefTenant();

	// Login the system and enter Setup-Wizard menu
	testFuncs.myDebugPrinting("Login the system and enter Setup-Wizard menu");
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_SETUP_WIZARD, "System Properties");

	// Enter the User-configuration menu
	testFuncs.myDebugPrinting("Enter the User-configuration menu");
	testFuncs.myClick(driver, By.xpath("//*[@id='sfb']")								, 5000);
	testFuncs.myClick(driver, By.xpath("//*[@id='smartwizard']/nav[2]/div[2]/button[2]"), 5000);
    List<WebElement>radioButton = driver.findElements(By.xpath("//*[@id='nozerotouch']"));
	radioButton.get(0).click();
	testFuncs.myClick(driver, By.xpath("//*[@id='smartwizard']/nav[2]/div[2]/button[2]"), 5000);
	testFuncs.mySelect(driver,
					   By.xpath("//*[@id='step-2']/div/div[2]/div[1]/select"),
					   enumsClass.selectTypes.GIVEN_TEXT,
					   usedTenant,
					   4000);
	testFuncs.myClick(driver, By.xpath("//*[@id='smartwizard']/nav[2]/div[2]/button[2]"), 5000);
	testFuncs.myClick(driver, By.xpath("//*[@id='smartwizard']/nav[1]/div[2]/button[2]"), 5000);
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-4']/h2", "Zero Touch Templates Mapping");
	
	// Step 1 - Add and verify mapping
	testFuncs.myDebugPrinting("Step 1 - Add and verify mapping");
	Map<String, String> newDataMap = new HashMap<String, String>();
	newDataMap.put("isDefault"    ,  "true"); 
	newDataMap.put("modelType"    , usedModels[0]); 
	newDataMap.put("tenantType"   , usedTenants[0]);
	newDataMap.put("confMsgHeader", "Enable zero touch"); 
	newDataMap.put("confMsgBody"  , "Successful to enable new zero touch default template of tenant and type"); 
	testFuncs.addMapping(driver, usedTemplate, newDataMap);
	testFuncs.verifyMappingWizard(driver, usedModels[0], usedTenants[0], usedTenants[0] + "_" + usedModels[0] + ".cfg");

//	
//    // Step 2 - Edit tenant configuration
//	testFuncs.myDebugPrinting("Step 2 - Edit tenant configuration");
//	String newTenConfValue = "newTenantConfValue" + Id;;
//	editConfigration(tenConfName, tenConfValue, newTenConfValue, usedTenant);
//	
//    // Step 3 - Delete tenant configuration
//	testFuncs.myDebugPrinting("Step 3 - Delete tenant configuration");
//	deleteConfigration(tenConfName, newTenConfValue, usedTenant);
//	
//	// Step 4 - Add a Tenant configuration value from Features Tab
//	testFuncs.myDebugPrinting("Step 4 - Add a Tenant configuration value from Features Tab");
//	Map<String, String> newDataMap = new HashMap<String, String>();
//	newDataMap.put("valueName" , "dayLightSaving"); 
//	newDataMap.put("valueKey"  , tenantValueKey); 
//	newDataMap.put("usedTenant", usedTenant); 
//	featuresConfigration(newDataMap);
//	
//	// Step 5 - Delete all Tenant configuration values using Features Tab
//	testFuncs.myDebugPrinting("Step 4 - Add a Tenant configuration value from Features Tab");
//	newDataMap.put("valueName"	   ,  "deleteAllValues"); 
//	newDataMap.put("valueForVerify",  tenantValueKey); 
//	featuresConfigration(newDataMap);	
  }

  @After
  public void tearDown() throws Exception {
	  
//    driver.quit();
    System.clearProperty("webdriver.chrome.driver");
	System.clearProperty("webdriver.ie.driver");
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
    	
      testFuncs.myFail(verificationErrorString);
    }
  }
}
