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
* This test tests a login mechanism for all users
* -----------------
* Tests:
*    1. Try to login the system via Admin user
*    2. Try to login the system via Operation user (system)
*    3. Try to login the system via Operation user (tenant)
*    4. Try to login the system via Monitoring user (system)
*    5. Try to login the system via Monitoring user (tenant)
* 
* Results:
*    1-5. All login actions should succeed
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test0__all_users {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;

  // Default constructor for print the name of the used browser 
  public Test0__all_users(browserTypes browser) {
	  
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
  public void Login_all_users() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	  
    // Step 1 - login system via Administrator user
    testFuncs.myDebugPrinting("Step 1 - login system via Administrator user");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "General_Informatiom_logout", testVars.getMainPageStr());
  
	// Step 2 - login system via Operation (system) user
    testFuncs.myDebugPrinting("Step 2 - login system via Operation (system) user");
    testFuncs.login(driver, testVars.getOperUsername(), testVars.getOperPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);  
    testFuncs.enterMenu(driver, "General_Informatiom_logout", testVars.getMainPageStr());

	// Step 3 - login system via Operation (tenant) user
    testFuncs.myDebugPrinting("Step 3 - login system via Operation (tenant) user");
    testFuncs.login(driver, testVars.getOperUsername(), testVars.getOperPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);  
    testFuncs.enterMenu(driver, "General_Informatiom_logout", testVars.getMainPageStr());
 
	// Step 4 - login system via Monitoring (system) user
	testFuncs.myDebugPrinting("Step 4 - login system via Monitoring (system) user");
	testFuncs.login(driver, testVars.getMonitUsername(), testVars.getMonitPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
    testFuncs.enterMenu(driver, "General_Informatiom_logout", testVars.getMainPageStr());

	// Step 5 - login system via Monitoring (tenant) user
	testFuncs.myDebugPrinting("Step 5 - login system via Monitoring (tenant) user");
	testFuncs.login(driver, testVars.getMonitTenUsername(), testVars.getMonitTenPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
    testFuncs.enterMenu(driver, "General_Informatiom_logout", testVars.getMainPageStr());
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