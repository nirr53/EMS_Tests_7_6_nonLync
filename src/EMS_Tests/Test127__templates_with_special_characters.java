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
import EMS_Tests.enumsClass.*;

/**
* ----------------
* This test tests a create of Template with different special character
* ----------------
* Tests:
* 	 - Enter Manage multiple users changes menu.
* 	 1.  Try to create a Template with . character
* 	 2.  Try to create a Template with \ character
* 	 3.  Try to create a Template with | character
* 	 4.  Try to create a Template with * character
* 	 5.  Try to create a Template with ? character
* 	 6.  Try to create a Template with < character
*  	 7.  Try to create a Template with > character
*  	 8.  Try to create a Template with : character
*  	 9.  Try to create a Template with $ character
*  	 10. Try to create a Template with / character
*  
* Results:
* 	 1-10. All the Templates should not be created.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test127__templates_with_special_characters {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test127__templates_with_special_characters(browserTypes browser) {
	  
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
  public void Speicel_characters_templates() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String Id 			  = testFuncs.getId();
	String type			  = "430HD";
	String prefixName     = "sChrsTmpNme_" + Id + "_";
	String prefixDesc     = "sChrsTmpDsc_" + Id + "_";	
	String suffixes[]     = {".", "\"", "|", "*", "?", "<", ":", ">", "$", "//"};
    Map<String, String> map = new HashMap<String, String>();
    map.put("isRegionDefault"		   ,  "false");
    map.put("isDownloadSharedTemplates",  "false");
    map.put("speicalChrsName",  "true");
    
    // Login and enter the Phone Templates menu
	testFuncs.myDebugPrinting("Login and enter the Phone Templates menu");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);    
  	
    // Step 1-10 - Create a user using POST query with different special characters
	int len = suffixes.length;
	for (int i = 0; i < len; ++i) {
		
		testFuncs.myDebugPrinting("Step " + i + ":", enumsClass.logModes.NORMAL);
		String tempName = prefixName + suffixes[i];
		String tempDesc = prefixDesc + suffixes[i];
		testFuncs.myDebugPrinting("tempName - " + tempName, enumsClass.logModes.MINOR);
		testFuncs.myDebugPrinting("tempDesc - " + tempDesc, enumsClass.logModes.MINOR);
		testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONF_TEMPLATES, "IP Phones Configuration Templates");
  		map.put("cloneFromtemplate", "Audiocodes_" + type + "_LYNC");
  		testFuncs.addTemplate(driver, tempName, tempDesc, testVars.getDefTenant(), type, map);
  		testFuncs.myWait(3000);
	}
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
