package EMS_Tests;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import org.openqa.selenium.*;import EMS_Tests.enumsClass.*;

/**
* ----------------
* This test tests the create of several phone templates via an Operation users (system+tenant)
* ----------------
* Tests:
* 	 - Login via an Operation user (system) and enter the Phone Templates menu.
* 	 1. Create a phone template
* 	 2. Edit the created template
* 	 3. Delete the template
* 
* 	 - Logout, re-login via an Operation user (tenant) and enter the Phone Templates menu.
* 	 4. Create a phone template.
* 	 5. Edit the created template.
* 	 6. Delete the template.
* 
* Results:
*   1-6. All the operations should work as 'Administrator' user.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test59__Operation_system_tenant_templates {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test59__Operation_system_tenant_templates(browserTypes browser) {
	  
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
  public void Operation_system_tenant_Templates() throws Exception {
	  
	  Log.startTestCase(this.getClass().getName());
	  
	  // Set variables
	  String Id 			  = testFuncs.getId();
	  String phoneType    	  = "430HD";	
	  Map<String, String> map = new HashMap<String, String>();
	  map.put("isRegionDefault"		     ,  "false");
	  map.put("cloneFromtemplate"        ,  ""); 
	  map.put("isDownloadSharedTemplates",  "false");

	  // Login via Operation user (system) and enter the Phone Templates menu
	  testFuncs.myDebugPrinting("Login via Operation user (system) and enter the Phone Templates menu");
	  testFuncs.login(driver, testVars.getOperSysLoginData(enumsClass.loginData.USERNAME), testVars.getOperSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONF_TEMPLATES, "IP Phones Configuration Templates");
		  
	  // Step 1 - Add a template
	  testFuncs.myDebugPrinting("Step 1 - Add a template");	
	  map.put("cloneFromtemplate", "NirTemplate430");
	  testFuncs.addTemplate(driver, "my" + phoneType + "Template_" + Id, "my" + phoneType + "desc", testVars.getDefTenant(), phoneType, map);

	  // Step 2 - Edit a template
	  testFuncs.myDebugPrinting("Step 2 - Edit a template");
	  map.put("model", phoneType);	
	  testFuncs.editTemplate(driver,  "my" + phoneType + "Template_" + Id, map);	  	

	  // Step 3 - Delete a template		
	  testFuncs.myDebugPrinting("Step 3 - Delete a template");	
	  testFuncs.deleteTemplate(driver, "my" + phoneType + "Template_" + Id);
	  
	  // Logout, re-login via Operation user (tenant) and enter the Phone Templates menu
	  testFuncs.myDebugPrinting("Logout, re-login via Operation user (tenant) and enter the Phone Templates menu");
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_GEN_INFOR_LOGOUT, testVars.getMainPageStr());
	  testFuncs.login(driver, testVars.getOperSysLoginData(enumsClass.loginData.USERNAME), testVars.getOperSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONF_TEMPLATES, "IP Phones Configuration Templates");
		  
	  // Step 4 - Add a template
	  testFuncs.myDebugPrinting("Step 4 - Add a template");	
	  map.put("cloneFromtemplate", "NirTemplate440");
	  testFuncs.addTemplate(driver, "my" + phoneType + "Template_" + Id, "my" + phoneType + "desc", testVars.getDefTenant(), phoneType, map);

	  // Step 5 - Edit a template
	  testFuncs.myDebugPrinting("Step 5 - Edit a template");
	  map.put("model", phoneType);	
	  map.put("tenant", testVars.getDefTenant());	
	  testFuncs.editTemplate(driver,  "my" + phoneType + "Template_" + Id, map);	  	

	  // Step 6 - Delete a template		
	  testFuncs.myDebugPrinting("Step 6 - Delete a template");	
	  testFuncs.deleteTemplate(driver, "my" + phoneType + "Template_" + Id);
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