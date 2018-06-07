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
* 	 - Login and enter the Template mapping menu 
* 	 1. Check headers
*    2. Add and verify mapping
*    3. Update the created mapping
*    4. Disable the created mapping
* 
* Results:
* 	 1. All headers should be detected.
*    2. Mapping should be created and detected successfully.
*    3. Mapping should be updated and detected successfully.
*    4. Mapping should be deleted and detected successfully.
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
  public void Templates_mapping_tests() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String usedTemplate  = "importtemplate";
	String usedModels[]  = {"445HD", "450HD"};
	String usedTenants[] = {testVars.getNonDefTenant(0), testVars.getNonDefTenant(1)};

	// Login the system and enter the Template mapping menu
	testFuncs.myDebugPrinting("Login the system and enter the Template mapping menu");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_TEMPLATES_MAPPING, "Zero Touch Templates Mapping");

	// Step 1 - Check headers
	testFuncs.myDebugPrinting("Step 1 - Check headers");
	checkheaders();
	
	// Step 2 - Add and verify mapping
	testFuncs.myDebugPrinting("Step 2 - Add and verify mapping");
	Map<String, String> newDataMap = new HashMap<String, String>();
	newDataMap.put("isDefault"    ,  "true"); 
	newDataMap.put("modelType"    , usedModels[0]); 
	newDataMap.put("tenantType"   , usedTenants[0]);
	newDataMap.put("confMsgHeader", "Enable zero touch"); 
	newDataMap.put("confMsgBody"  , "Successful to enable new zero touch default template of tenant and type"); 
	addMapping(driver, usedTemplate, newDataMap);
	verifyMapping(driver, usedModels[0], usedTenants[0], usedTenants[0] + "_" + usedModels[0] + ".cfg");
	
	// Step 3 - Update the created mapping
	testFuncs.myDebugPrinting("Step 3 - Update the created mapping");
	testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[2]/ul/li[1]/a"), 3000);
	newDataMap.put("isDefault"    , "true"); 
	newDataMap.put("modelType"    , usedModels[1]); 
	newDataMap.put("tenantType"   , usedTenants[1]);	
	newDataMap.put("confMsgHeader", "Update Template"); 
	newDataMap.put("confMsgBody"  , "Successful to update the template of tenant and type"); 
	addMapping(driver, usedTemplate, newDataMap);
	verifyMapping(driver, usedModels[1], usedTenants[1], usedTenants[1] + "_" + usedModels[1] + ".cfg");
	
	// Step 4 - Disable the created mapping
	testFuncs.myDebugPrinting("Step 4 - Disable the created mapping");
	testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[2]/ul/li[2]/a"), 3000);
	disableMapping(driver, usedModels[1], usedTenants[1]);
  }
  
  /**
  *  Disable a mapping by given parameters
  *  @param driver     - given driver
  *  @param usedModel  - given used Model
  *  @param usedTenant - given used Tenant
  */
  private void disableMapping(WebDriver driver, String usedModel, String usedTenant) {
	  
	  int numOfMappings = driver.findElement(By.tagName("body")).getText().split("405", -1).length - 1;
	  testFuncs.myDebugPrinting("numOfMappings - " + numOfMappings, enumsClass.logModes.MINOR);
	  for (int i = 1 ; i <= numOfMappings; ++i) {
		    
		  Select modelSelect = new Select(driver.findElement(By.xpath("//*[@id='zero']/div/div/div[2]/table/tbody/tr[" + i + "]/td[2]/div/select")));
		  String currModel = modelSelect.getFirstSelectedOption().getText();
		  Select tenantSelect = new Select(driver.findElement(By.xpath("//*[@id='zero']/div/div/div[2]/table/tbody/tr[" + i + "]/td[4]/div/select")));
		  String currTenant = tenantSelect.getFirstSelectedOption().getText();
		  testFuncs.myDebugPrinting("currModel - "  + currModel , enumsClass.logModes.MINOR);
		  testFuncs.myDebugPrinting("currTenant - " + currTenant, enumsClass.logModes.MINOR);

		  // Seek for a matching row
		  if (usedModel.equals(currModel) && usedTenant.equals(currTenant)) {
			  
			  testFuncs.myDebugPrinting("Match was detected !!", enumsClass.logModes.MINOR);
			  if (driver.findElement(By.xpath("//*[@id='zero']/div/div/div[2]/table/tbody/tr[" + i + "]/td[1]/div/input")).isSelected()) {
				  
				  // Disable the mapping
				  testFuncs.myDebugPrinting("Disable the mapping", enumsClass.logModes.MINOR);
				  testFuncs.myClick(driver, By.xpath("//*[@id='zero']/div/div/div[2]/table/tbody/tr[" + i + "]/td[1]/div/input")  , 4000);
				  testFuncs.myClick(driver, By.xpath("//*[@id='zero']/div/div/div[2]/table/tbody/tr[" + i + "]/td[6]/div/buttton"), 4000);
				  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Disable zero touch");		
				  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to disable the zero touch default template of tenant " + usedTenant + " and type " + usedModel);		
				  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000);  
				  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Disable zero touch");		
				  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Successful to disable the zero touch default template of tenant " + usedTenant + " and type " + usedModel);		
				  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000); 
				  
				  // Verify the disable
				  testFuncs.myDebugPrinting("Verify the disable", enumsClass.logModes.MINOR);	
				  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[2]/ul/li[2]/a"), 3000);
				  testFuncs.myAssertTrue("", !driver.findElement(By.xpath("//*[@id='zero']/div/div/div[2]/table/tbody/tr[" + i + "]/td[1]/div/input")).isSelected());
			  }	  
		  }		  
	  }
  }
  
  // Check page headers
  private void checkheaders() {
	    
	  // Check help section
	  testFuncs.myDebugPrinting("Check help section", enumsClass.logModes.MINOR);
	  testFuncs.searchStr(driver, "After creating the TENANTs we need to map the TEMPLATE for each device.");
	  testFuncs.searchStr(driver, "The TEMPALTE will be chosen according to the {MODEL + TENANT}.");
	  testFuncs.searchStr(driver, "With this mapping a new device that registered to the IPP Manager,");
	  testFuncs.searchStr(driver, "will get the TEMPLATE according to its {MODEL + TENANT}. ");
	  testFuncs.searchStr(driver, "This is a part of the Zero Touch process.");
		
	  // Setup Template section
	  testFuncs.myDebugPrinting("Check Setup Template section", enumsClass.logModes.MINOR);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3"			 	  , "Zero Touch Templates Mapping");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[2]/ul/li[1]/a", "Setup Template");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[2]/ul/li[2]/a", "Templates Mapping");		
	  testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[2]/ul/li[3]/a", "Test");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='def']/div/div/div[1]/h3"								 	  , "Default template per model and tenant");
		
	  // Templates Mapping section
	  testFuncs.myDebugPrinting("Check Templates Mapping section", enumsClass.logModes.MINOR);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[2]/ul/li[2]/a"), 3000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='zero']/div/div/div[1]/h3"							  , "Map TEMPLATE to {MODEL + TENANT}");			
	  testFuncs.searchStr(driver, "Map TEMPLATE to {MODEL + TENANT}");

	  // Test section
	  testFuncs.myDebugPrinting("Check Test section", enumsClass.logModes.MINOR);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[2]/ul/li[3]/a"), 3000);
	  testFuncs.searchStr(driver, "Choose TENANT and MODEL and click test for the TEMPLATE");	
	  testFuncs.verifyStrByXpath(driver, "//*[@id='test']/div/div/div/div[2]/table/thead/tr/th[1]", "Model");	
	  testFuncs.verifyStrByXpath(driver, "//*[@id='test']/div/div/div/div[2]/table/thead/tr/th[2]", "Tenant");	
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[2]/ul/li[1]/a"), 3000);
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
	testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , newDataMap.get("confMsgHeader"));
	testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", newDataMap.get("confMsgBody"));		
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
