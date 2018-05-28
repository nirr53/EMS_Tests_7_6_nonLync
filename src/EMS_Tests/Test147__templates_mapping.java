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
import org.openqa.selenium.*;
import EMS_Tests.enumsClass.browserTypes;

/**
* ----------------
* This test tests the Template mapping menu
* ----------------
* Tests:
* 	 - Login and enter the Template mapping menu a registered user with registered device 
* 	 1. 
* 
* Results:
* 	 1. As described.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test147__templates_mapping {
	
  private browserTypes  usedBrowser;
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test147__templates_mapping(browserTypes browser) {
	  
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
  public void Tenant_site_configuration_during_create_tests() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String usedTemplate = "NirTemplate445";
	String usedModel 	= "445HD";
	String usedTenant = testVars.getDefTenant();

	// Login the system and enter the Template mapping menu
	testFuncs.myDebugPrinting("Login the system and enter the Template mapping menu");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Templates_mapping", "Zero Touch Templates Mapping");

	// Step 1 - Check headers
	testFuncs.myDebugPrinting("Step 1 - Check headers");
	testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3"			 	  , "Zero Touch Templates Mapping");
	testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[2]/ul/li[1]/a", "Setup Template");
	testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[2]/ul/li[2]/a", "Templates Mapping");
	testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[2]/ul/li[3]/a", "Test");
	testFuncs.verifyStrByXpath(driver, "//*[@id='def']/div/div/div[1]/h3"								 	  , "Default template per model and tenant");

	// Step 2 - Add mapping
	testFuncs.myDebugPrinting("Step 2 - Add mapping");
	Map<String, String> newDataMap = new HashMap<String, String>();
	newDataMap.put("isDefault",  "true"); 
	newDataMap.put("modelType" , usedModel); 
	newDataMap.put("tenantType", usedTenant); 
	testFuncs.addMapping(driver, usedTemplate, newDataMap);
	
	// Step 3 - Verify Mapping
	testFuncs.myDebugPrinting("Step 3 - Verify Mapping");
	testFuncs.verifyMapping(driver, usedModel, usedTenant);
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
