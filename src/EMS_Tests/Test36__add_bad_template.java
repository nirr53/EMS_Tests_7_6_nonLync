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
* This test try to create templates in several modes.
* ----------------
* Tests:
* 	 - Enter the templates menu, create a templates.
* 	 1. Try to create a template without name or description.
* 	 2. Try to create two template with the same name or very big cfg file.
* 
* Results:
*	 1. Every template needs a name and a description. 
*	 2. Two similar Templates are impossible.
*
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test36__add_bad_template {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test36__add_bad_template(browserTypes browser) {
	  
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
  public void Add_bad_template() throws Exception {
	  
	 Log.startTestCase(this.getClass().getName());
	 
	 // Set variables
	 String Id       = testFuncs.getId();
	 String tempName = "tempName" + Id;
	 String tempDesc = "tempDesc" + Id;
	 Map<String, String> map = new HashMap<String, String>();
	 map.put("isRegionDefault"		    ,  "false");
	 map.put("cloneFromtemplate"        ,  ""); 
	 map.put("isDownloadSharedTemplates",  "false");
	 map.put("falseCreateName"          ,  tempName); 
	 map.put("falseCreateDesc"          ,  tempDesc); 
	  
	// Login and enter the Phone Templates menu
	testFuncs.myDebugPrinting("Login and enter the Phone Templates menu");
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONF_TEMPLATES, "IP Phones Configuration Templates");	
	
	// Step 1 - Try to create a template without name or description
  	testFuncs.myDebugPrinting("Step 1 - Try to create a template without name or description");
	testFuncs.addTemplate(driver, "", "", testVars.getDefTenant(), "420HD", map);

	// Step 2 - Try to create two template with the same name or very big cfg file
	testFuncs.myDebugPrinting("Step 2 - Try to create two template with the same name or very big cfg file");
	map.put("secondCreate", "1");
	testFuncs.addTemplate(driver, tempName, tempDesc, testVars.getDefTenant(), "420HD", map);
	
	// Step 3 - Delete the created template from step 1
	testFuncs.myDebugPrinting("Step 3 - Delete the created template from step 1");
	testFuncs.deleteTemplate(driver, tempName);
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
