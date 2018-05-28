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
* This test tests the Template placeholders menu
* ----------------
* Tests:
* 	 - Enter Template placeholders menu.
* 	 1. Add a Template placeholder.
* 	 2. Edit an existing Template placeholder.
* 	 3. Delete a Template placeholder.
*    4. Copy a Template placeholder.
* 
* Results:
* 	1. Template placeholder should be added successfully.
* 	2. Template placeholder should be edited successfully.
* 	3. Template placeholder should be deleted successfully.
*   4. Template placeholder should be copied successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test28__template_placeholders {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test28__template_placeholders(browserTypes browser) {
	  
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
  public void Template_placeholders_menu() throws Exception {
	  

	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String tempName          = "Audiocodes_405";
	String tempWeCopyTo      = "Audiocodes_420HD";
	String Id 			     = testFuncs.getId();
	String tempPhName        = "myPHolderName"  	  + Id;
	String tempPhValue       = "myPHolderValue" 	  + Id;
	String tempPhDescription = "myPHolderDescription" + Id;
	 
    // Enter the Add new template placeholder menu
	testFuncs.myDebugPrinting("Enter the Add new template placeholder menu");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "Setup_Phone_conf_templates_placeholders", "Template Placeholders");
	
	// Step 1 - Add a new Template placeholder
	testFuncs.myDebugPrinting("Step 1 - Add a new Template placeholder");
    testFuncs.addTemplatePlaceholder(driver, tempName, tempPhName, tempPhValue, tempPhDescription);
       
	// Step 2 - Edit an existing Template placeholder
	testFuncs.myDebugPrinting("Step 2 - Edit an existing Template placeholder");
	tempPhValue       = "new" + tempPhValue;
	tempPhDescription = "new" + tempPhDescription;
    testFuncs.editTemplatePlaceholder(driver, tempName, tempPhName, tempPhValue, tempPhDescription);
    
	// Step 3 - Delete a Template placeholder
	testFuncs.myDebugPrinting("Step 3 - Delete a Template placeholder");
    testFuncs.deleteTemplatePlaceholder(driver, tempName, tempPhName);
    
	// Step 4 - Copy a Template placeholder
	testFuncs.myDebugPrinting("Step 4 - Copy a Template placeholder");
    testFuncs.addTemplatePlaceholder(driver,    tempName, tempPhName, tempPhValue, tempPhDescription);
	testFuncs.copyTemplatePlaceholder(driver,   tempName, tempWeCopyTo, tempPhName);
    testFuncs.deleteTemplatePlaceholder(driver, tempName, tempPhName);
    testFuncs.deleteTemplatePlaceholder(driver, tempWeCopyTo, tempPhName);
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
