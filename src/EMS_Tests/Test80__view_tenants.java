package EMS_Tests;

import java.util.ArrayList;
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
* This test tests the View Tenants menu
* ----------------
* Tests:
* 	 - Enter View Tenants menu.
* 	 1. Check the menu headers.
* 	 2. Check the Change-Tenants-Via-EMS button.
* 
* Results:
* 	 1. Headers should be displayed properly.
* 	 2. The button should leads to the IPP-EMS
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test80__view_tenants {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test80__view_tenants(browserTypes browser) {
	  
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
  public void View_Tenants() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Setup_System_view_tenants", "Tenant List");
		
	// Step 1 - Check headers of the menu
	testFuncs.myDebugPrinting("Step 1 - Check headers of the menu");
	testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div[2]/div/div[2]/div/h4"				   , "Tenants can only be changed from the");
	testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div[2]/div/div[2]/table/thead/tr/th[2]/b", "Name");
	testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div[2]/div/div[2]/table/thead/tr/th[3]/b", "Description");	
	
    // Step 2 - Check the Change-Tenants-Via-EMS button.
	testFuncs.myDebugPrinting("Step 2 - Check the Change-Tenants-Via-EMS button.");
	testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div[2]/div/div[2]/div/h4/a"), 10000);
	ArrayList<?> tabs = new ArrayList<Object> (driver.getWindowHandles());
	driver.switchTo().window((String) tabs.get(1));
	String txt = driver.findElement(By.tagName("body")).getText();
	testFuncs.myAssertTrue("Txt was not detected !! ("  + txt + ")", txt.contains("USERNAME"));
	testFuncs.myAssertTrue("Txt was not detected !! ("  + txt + ")", txt.contains("PASSWORD"));
	testFuncs.myAssertTrue("Txt was not detected !! ("  + txt + ")", txt.contains("Log In"));	
	String emsUrl = "https://" + testVars.getIp() + "/web-ui-ovoc/#!/login";	
	String currUrl = driver.getCurrentUrl();
	testFuncs.myDebugPrinting("emsUrl - "   + emsUrl, enumsClass.logModes.MINOR);
	testFuncs.myDebugPrinting("currUrl - "  + currUrl, enumsClass.logModes.MINOR);
	testFuncs.myAssertTrue("emsUrl was not detected !! ("  + emsUrl + ")", emsUrl.contains(currUrl));
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
