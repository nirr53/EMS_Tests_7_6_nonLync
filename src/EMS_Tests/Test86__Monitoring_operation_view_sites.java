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
* This test tests the View Sites menu via Monitoring (system+license) and Operation (system+license) users
* ----------------
* Tests:
*    - Login the system via Monitoring user (system)
*    1. Verify that the Site name and Tenant name is displayed properly.
*  
*    - Login the system via Monitoring user (tenant)
*    2. Verify that the Site name and Tenant name is displayed properly.
*    
*    - Login the system via Operation user (system)
*    3. Verify that the Site name and Tenant name is displayed properly.
*    
*    - Login the system via Operation user (tenant)
*    4. Verify that the Site name and Tenant name is displayed properly.
* 
* Results:
* 	 1-4. Headers should be displayed properly.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test86__Monitoring_operation_view_sites {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test86__Monitoring_operation_view_sites(browserTypes browser) {
	  
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
	
	// Step 1 - Login the system via Monitoring user (system) and verify in the View-sites menu that the menu is displayed properly
	testFuncs.myDebugPrinting("Step 1 - Login the system via Monitoring user (system) and verify in the View-sites menu that the menu is displayed properly");
	testFuncs.login(driver, testVars.getMonitUsername(), testVars.getMonitPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_SYSTEM_VIEW_SITES, "View Sites");	
	testFuncs.searchStr(driver, testVars.getDefSite() + " [" + testVars.getDefSite() + "] " + testVars.getDefTenant());
	
	// Step 2 - Logout, re-login the system via Monitoring user (tenant) and verify in the View-sites menu that the menu is displayed properly
	testFuncs.myDebugPrinting("Step 2 - Logout, re-login the system via Monitoring user (tenant) and verify in the View-sites menu that the menu is displayed properly");  
	testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_GEN_INFOR_LOGOUT, testVars.getMainPageStr());
	testFuncs.login(driver, testVars.getMonitTenUsername(), testVars.getMonitTenPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_SYSTEM_VIEW_SITES, "View Sites");
	// Known VI
	testFuncs.searchStr(driver, testVars.getDefSite() + " [" + testVars.getDefSite() + "] " + testVars.getDefTenant());

	// Step 3 - Logout, re-login the system via Operation user (system) and verify in the View-sites menu that the menu is displayed properly
	testFuncs.myDebugPrinting("Step 3 - Logout, re-login the system via Operation user (system) and verify that the menu is displayed properly");  
	testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_GEN_INFOR_LOGOUT, testVars.getMainPageStr());
	testFuncs.login(driver, testVars.getOperUsername(), testVars.getOperPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_SYSTEM_VIEW_SITES, "View Sites");
	testFuncs.searchStr(driver, testVars.getDefSite() + " [" + testVars.getDefSite() + "] " + testVars.getDefTenant());

	// Step 4 - Logout, re-login the system via Operation user (tenant) and verify in the View-sites menu that the menu is displayed properly
	testFuncs.myDebugPrinting("Step 4 - Logout, re-login the system via Operation user (tenant) and verify in the View-sites menu that the menu is displayed properly");  
	testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_GEN_INFOR_LOGOUT, testVars.getMainPageStr());
	testFuncs.login(driver, testVars.getOperTenUsername(), testVars.getOperTenPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_SYSTEM_VIEW_SITES, "View Sites");
	testFuncs.searchStr(driver, testVars.getDefSite() + " [" + testVars.getDefSite() + "] " + testVars.getDefTenant());
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
