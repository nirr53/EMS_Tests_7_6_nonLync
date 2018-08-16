package EMS_Tests;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.*;
import EMS_Tests.enumsClass.*;

/**
* ----------------
* This test tests a try to create duplicate Template placeholders menu
* ----------------
* Tests:
* 	 - Add a Template placeholder.
* 	 1. Add another Template placeholder with the same name.
* 	 2. Delete the template and Template placeholder.
* 
* Results:
* 	1. The add should fail with appropriate error prompt.
* 	2. Template placeholder should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test134__duplicate_template_placeholders {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test134__duplicate_template_placeholders(browserTypes browser) {
	  
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
  public void Duplicate_Template_placeholders() throws Exception {
	  

	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String tempName          = "Audiocodes_405";
	String Id 			     = testFuncs.getId();
	String tempPhName        = "myPHolderName"  	  + Id;
	String tempPhValue       = "myPHolderValue" 	  + Id;
	String tempPhDescription = "myPHolderDescription" + Id;
	 
    // Add a new Template placeholder
	testFuncs.myDebugPrinting("Add a new Template placeholder");
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONF_TEMPLATES_PLACEHOLDERS, "Template Placeholders");	
    testFuncs.addTemplatePlaceholder(driver, tempName, tempPhName, tempPhValue, tempPhDescription);

    // Step 1 - Add another Template placeholder with the same name
	testFuncs.myDebugPrinting("Step 1 - Add another Template placeholder with the same name");
    testFuncs.addTemplatePlaceholder(driver, tempName, tempPhName);
    
	// Step 2 - Delete a Template placeholder
	testFuncs.myDebugPrinting("Step 2 - Delete a Template placeholder");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONF_TEMPLATES_PLACEHOLDERS, "Template Placeholders");
    testFuncs.deleteTemplatePlaceholder(driver, tempName, tempPhName);
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
