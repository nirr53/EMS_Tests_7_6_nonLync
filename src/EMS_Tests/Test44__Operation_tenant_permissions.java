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
* This test tests the different modes of View-Tenants menu via several users.
* ----------------
* Tests:
*    1. Login with an Operation user (system), check the all tenants are displayed
*    2. Login with an Operation user (Tenant), check that only the tenant which associate with the user is displayed
* 
* Results:
*    1. All Tenants should be displayed.
*    2. Only the tenant which associate with the user is displayed
*   
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test44__Operation_tenant_permissions {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test44__Operation_tenant_permissions(browserTypes browser) {
	  
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
  public void Monitoring_Operation_Region_permissions() throws Exception {
	
	  Log.startTestCase(this.getClass().getName());
	  
	  // Set variables
	  String bodyText;
	  String tenant1 = testVars.getDefTenant(); 	// The default Tenant of the system
	  
	  // Step 1 - Login with a  Operation user (System), check the all tenants are displayed and logout
	  testFuncs.myDebugPrinting("Step 1 - Login with a  Operation user (System), check the all tenants are displayed and logout");
	  testFuncs.login(driver, testVars.getOperSysLoginData(enumsClass.loginData.USERNAME), testVars.getOperSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "https://", this.usedBrowser);
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_SYSTEM_VIEW_TENANTS, "Tenant List");
	  bodyText = driver.findElement(By.tagName("body")).getText();
	  testFuncs.myAssertTrue("Tenant <" + tenant1 + "> was not found !!\nbodyText - " + bodyText, bodyText.contains(tenant1));
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_GEN_INFOR_LOGOUT, testVars.getMainPageStr());

	  // Step 2 - Login with a  Operation user (Tenant), check that only the tenant which associate with the user is displayed
	  testFuncs.myDebugPrinting("Step 2 - Login with a  Operation user (Tenant), check that only the tenant which associate with the user is displayed");
	  testFuncs.login(driver, testVars.getOperTenLoginData(enumsClass.loginData.USERNAME), testVars.getOperTenLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "https://", this.usedBrowser);
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_SYSTEM_VIEW_TENANTS, "Tenant List");
	  bodyText = driver.findElement(By.tagName("body")).getText();
	  testFuncs.myAssertTrue("Tenant <" + tenant1 + "> was not found !!\nbodyText - " + bodyText,  bodyText.contains(tenant1));
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
