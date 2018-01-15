package EMS_Tests;

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
* This test tests the Timeout mechanism
* -----------------
* Tests:
*    Login the system
*    Wait for 10 minutes without do any action.
*    1. After the 10 minutes, try to enter for one of the menus.
* 
* Results:
*    1. You should be logged of the system when you try to enter the wanted menu.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test132__timeout {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;

  // Default constructor for print the name of the used browser 
  public Test132__timeout(String browser) {
	  
	  Log.info("Browser - "  + browser);
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
  public void Timeout() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	  
    // Login via HTTPS
    testFuncs.myDebugPrinting("Login via HTTPS");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);  
  
	// Wait for 10 minutes
    testFuncs.myDebugPrinting("Wait for 10 minutes");
	for (int i = 0; i < 900; ++i) {
		
		testFuncs.myWait(1000);
	    testFuncs.myDebugPrinting("Wait for " + i + " seconds !!", testVars.logerVars.MINOR);
	}
	
	// Step 1 - try to enter one of the system menus and verify that you logged of the system
    testFuncs.myDebugPrinting("Step 1 - try to enter one of the system menus and verify that you logged of the system");
	
	String bodyText = driver.findElement(By.tagName("body")).getText();
	testFuncs.myDebugPrinting("bodyText - \n" + bodyText, testVars.logerVars.MAJOR);
	if (!bodyText.contains(testVars.getMainPageStr())) {
		
		testFuncs.myClick(driver, By.xpath("//*[@id='navbar-collapse']/ul[1]/li[2]/a"), 7000); 
		testFuncs.searchStr(driver, testVars.getMainPageStr());

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