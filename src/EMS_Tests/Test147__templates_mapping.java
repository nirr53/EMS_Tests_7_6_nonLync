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
import org.openqa.selenium.support.ui.Select;

import EMS_Tests.enumsClass.*;

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
	addMapping(driver, usedTemplate, newDataMap);
	
	// Step 3 - Verify Mapping
	testFuncs.myDebugPrinting("Step 3 - Verify Mapping");
	verifyMapping(driver, usedModel, usedTenant, "Nir_445HD.cfg");
	
	// Step 3 - Add another mapping
	testFuncs.myDebugPrinting("Step 2 - Add mapping");
	newDataMap.put("isDefault",  "true"); 
	newDataMap.put("modelType" , usedModel); 
	newDataMap.put("tenantType", usedTenant); 
	addMapping(driver, usedTemplate, newDataMap);
  }
  
  /**
  *  Add a mapping value by given parameters
  *  @param driver       - given driver
  *  @param usedTemplate - given used template
  *  @param newDataMap   - array of data for default mapping
  */
  private void addMapping(WebDriver driver, String usedTemplate, Map<String, String> newDataMap) {
	
	// Set a Template
	testFuncs.myDebugPrinting("Set a Template <" + usedTemplate + ">", enumsClass.logModes.MINOR);
	new Select(driver.findElement(By.xpath("//*[@id='templates-def']"))).selectByVisibleText(usedTemplate);
	testFuncs.myWait(5000);
	
	// Set extra data
	testFuncs.myDebugPrinting("Set extra data", enumsClass.logModes.MINOR);
	if (newDataMap.containsKey("isDefault") && newDataMap.get("isDefault").contains("true")) {
			
		// Mapping is default - Check the checkbox if needed
		testFuncs.myDebugPrinting("Mapping is default - Check the checkbox if needed", enumsClass.logModes.MINOR);		
		WebElement cbIsDef = driver.findElement(By.xpath("//*[@id='is_default']"));
		if (cbIsDef.getAttribute("value").contains("false")) {
			
			cbIsDef.click();
			testFuncs.myWait(2000);
		}
		
		// Set model
		testFuncs.myDebugPrinting("Set model", enumsClass.logModes.MINOR);		
		new Select(driver.findElement(By.xpath("//*[@id='def']/div/div/div[2]/table/tbody/tr/td[2]/div/table/tbody[1]/tr/td[3]/select"))).selectByVisibleText(newDataMap.get("modelType"));
		testFuncs.myWait(5000);
		
		// Set Tenant
		testFuncs.myDebugPrinting("Set Tenant", enumsClass.logModes.MINOR);		
		new Select(driver.findElement(By.xpath("//*[@id='def']/div/div/div[2]/table/tbody/tr/td[2]/div/table/tbody[1]/tr/td[5]/select"))).selectByVisibleText(newDataMap.get("tenantType"));
		testFuncs.myWait(5000);	
	}
	
	// Confirm
	testFuncs.myClick(driver, By.xpath("//*[@id='def']/div/div/div[2]/table/tbody/tr/td[2]/div/table/tbody[1]/tr/td[6]/button"), 5000);
	testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Update Template");
	testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Successful to update the template of tenant and type");		
	testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
  }
  
  /**
  *  Verify a mapping value by given parameters
  *  @param driver       - given driver
  *  @param usedTemplate - given used template
  *  @param usedTenant   - given used tenant
  *  @param	confName	 - wanted configuration file name for search
  */
  public void verifyMapping(WebDriver driver, String usedModel, String usedTenant, String confName) {
	  
	  // Verify headers of Test-Mapping section		
	  testFuncs.myDebugPrinting("Verify headers of Test-Mapping section", enumsClass.logModes.MINOR);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[2]/ul/li[3]/a"), 5000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='test']/div/div/div/div[1]/h3", "Choose TENANT and MODEL and click test for the TEMPLATE");		
	  testFuncs.verifyStrByXpath(driver, "//*[@id='test']/div/div/div/div[2]/table/thead/tr/th[1]", "Model");	
	  testFuncs.verifyStrByXpath(driver, "//*[@id='test']/div/div/div/div[2]/table/thead/tr/th[2]", "Tenant");
	  
	  // Select the tested model and tested tenant, and test the mapping
	  testFuncs.myDebugPrinting("Select the tested model and tested tenant, and test the mapping", enumsClass.logModes.MINOR);		  
	  testFuncs.mySelect(driver, By.xpath("//*[@id='test']/div/div/div/div[2]/table/tbody/tr/td[1]/select"), enumsClass.selectTypes.GIVEN_TEXT, usedModel , 4000);
	  testFuncs.mySelect(driver, By.xpath("//*[@id='test']/div/div/div/div[2]/table/tbody/tr/td[2]/select"), enumsClass.selectTypes.GIVEN_TEXT, usedTenant, 4000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='test']/div/div/div/div[2]/table/tbody/tr/td[3]/buttton"), 5000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Test Tenant URL");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", confName);
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
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
