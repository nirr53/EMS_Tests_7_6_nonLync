package EMS_Tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.*;

/**
* ----------------
* This test tests the View Sites menu
* ----------------
* Tests:
* 	 - Enter View Sites menu.
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
public class Test81__view_sites {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test81__view_sites(String browser) {
	  
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
  public void View_Sites() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Setup_System_view_sites", "View Sites");
		
	// Step 1 - Check headers of the menu
	testFuncs.myDebugPrinting("Step 1 - Check headers of the menu");
	testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/h4"				   , "Sites can only be changed from the");
	testFuncs.verifyStrByXpathContains(driver, "//*[@id='sites1']/thead/tr/th[1]", "Name");
	testFuncs.verifyStrByXpathContains(driver, "//*[@id='sites1']/thead/tr/th[2]", "Description");
	testFuncs.verifyStrByXpathContains(driver, "//*[@id='sites1']/thead/tr/th[3]", "Tenant");
	testFuncs.verifyStrByXpathContains(driver, "//*[@id='sites1']/thead/tr/th[4]", "Subnet Mask");
	
    // Step 2 - Check the Change-Tenants-Via-EMS button.
	testFuncs.myDebugPrinting("Step 2 - Check the Change-Tenants-Via-EMS button.");
	testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/h4/a"), 3000);
	ArrayList<?> tabs = new ArrayList<Object> (driver.getWindowHandles());
	driver.switchTo().window((String) tabs.get(1));
	String txt = driver.findElement(By.tagName("body")).getText();
	testFuncs.myAssertTrue("Txt was not detected !! ("  + txt + ")", txt.contains("USERNAME"));
	testFuncs.myAssertTrue("Txt was not detected !! ("  + txt + ")", txt.contains("PASSWORD"));
	testFuncs.myAssertTrue("Txt was not detected !! ("  + txt + ")", txt.contains("Log In"));
	String currUrl = driver.getCurrentUrl();
	testFuncs.myDebugPrinting("currUrl - " + currUrl);
	String emsUrl = "https://" + testVars.getIp() + "/web-ui-ovoc/#!/login";
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
