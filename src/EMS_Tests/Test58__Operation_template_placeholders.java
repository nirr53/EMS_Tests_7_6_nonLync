package EMS_Tests;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.*;import EMS_Tests.enumsClass.*;

/**
* ----------------
* This test tests the Template placeholders menu via an Operation users (system+tenant).
* ----------------
* Tests:
* 	 - Enter Template placeholders menu via an Operation user (system).
* 	 1. Add a Template placeholder.
* 	 2. Edit an existing Template placeholder.
* 	 3. Delete a Template placeholder.
*    4. Copy a Template placeholder.
*    
* 	 - Enter Template placeholders menu via an Operation user (tenant).
* 	 5. Add a Template placeholder.
* 	 6. Edit an existing Template placeholder.
* 	 7. Delete a Template placeholder.
*    8. Copy a Template placeholder.
* 
* Results:
* 	Via Operation user (system)
* 	1. Template placeholder should be added successfully.
* 	2. Template placeholder should be edited successfully.
* 	3. Template placeholder should be deleted successfully.
*   4. Template placeholder should be copied successfully.
*   
*   Via Operation user (tenant)
*   5. Template placeholder should be added successfully.
* 	6. Template placeholder should be edited successfully.
* 	7. Template placeholder should be deleted successfully.
*   8. Template placeholder should be copied successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test58__Operation_template_placeholders {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test58__Operation_template_placeholders(browserTypes browser) {
	  
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
  public void Operation_template_placeholders_menu() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String tempName          = "NirTemplate420";
	String tempWeCopyTo      = "NirTemplate430";
	String Id 			     = testFuncs.getId();
	String tempPhName        = "myPHolderName"        + Id;
	String tempPhValue       = "myPHolderValue"       + Id;
	String tempPhDescription = "myPHolderDescription" + Id;
	 
    // Login via Operation user (system) and enter the Add new template placeholder menu
	testFuncs.myDebugPrinting("Login via Operation user (system) and enter the Add new template placeholder menu");
	testFuncs.login(driver, testVars.getOperUsername(), testVars.getOperPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONF_TEMPLATES_PLACEHOLDERS, "Template Placeholders");
	
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
    
    // Logout and re-login via Operation user (tenant) and enter the Add new template placeholder menu
	testFuncs.myDebugPrinting("Logout and re-login via Operation user (tenant) and enter the Add new template placeholder menu");
	testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_GEN_INFOR_LOGOUT, testVars.getMainPageStr());
	testFuncs.login(driver, testVars.getOperUsername(), testVars.getOperPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONF_TEMPLATES_PLACEHOLDERS, "Template Placeholders");
	
	// Step 5 - Add a new Template placeholder
	testFuncs.myDebugPrinting("Step 5 - Add a new Template placeholder");
    testFuncs.addTemplatePlaceholder(driver, tempName, tempPhName, tempPhValue, tempPhDescription);
       
	// Step 6 - Edit an existing Template placeholder
	testFuncs.myDebugPrinting("Step 6 - Edit an existing Template placeholder");
	tempPhValue       = "new" + tempPhValue;
	tempPhDescription = "new" + tempPhDescription;
    testFuncs.editTemplatePlaceholder(driver, tempName, tempPhName, tempPhValue, tempPhDescription);
    
	// Step 7 - Delete a Template placeholder
	testFuncs.myDebugPrinting("Step 7 - Delete a Template placeholder");
    testFuncs.deleteTemplatePlaceholder(driver, tempName, tempPhName);
    
	// Step 8 - Copy a Template placeholder
	testFuncs.myDebugPrinting("Step 8 - Copy a Template placeholder");
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
