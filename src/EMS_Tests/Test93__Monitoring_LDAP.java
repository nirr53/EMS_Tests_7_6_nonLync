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
* This test tests the LDAP menu via Monitoring users(system+tenant)
* ----------------
* Tests:
* 	 -  Login via Monitoring-user (system), an enter the LDAP menu
*    1. Verify that you cannot edit the LDAP properties
*    2. Test the Test-LDAP button
*    
*    -  Logout, re-login via Monitoring-user (tenant), an enter the LDAP menu
*    3. Verify that you cannot edit the LDAP properties
*    4. Test the Test-LDAP button
* 
* Results:
*	 1+3. LDAP data cannot be edited via Monitoring-user.
*    2+4. LDAP button should respond via Monitoring-user.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test93__Monitoring_LDAP {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test93__Monitoring_LDAP(browserTypes browser) {
	  
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
  public void Monitoring_LDAP() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());	
	
	// Set variables
	String ldapAddress  = String.valueOf(testFuncs.getNum(128)) + "." +
			 			  String.valueOf(testFuncs.getNum(128)) + "." +
			 			  String.valueOf(testFuncs.getNum(128)) + "." +
			 			  String.valueOf(testFuncs.getNum(128));
	String Id			= testFuncs.getId();
	String ldapUsername = "username" + Id;
	String ldapPassword = "1q2w3e$R";
	 
	// Login via Monitoring-user (system), an enter the LDAP menu
	testFuncs.myDebugPrinting("Login via Monitoring-user (system), an enter the LDAP menu");
	testFuncs.login(driver, testVars.getMonitUsername(), testVars.getMonitPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "Setup_Phone_conf_system_settings_ldap", "LDAP Configuration");

	// Step 1 - Verify that you cannot edit the LDAP properties
	testFuncs.myDebugPrinting("Step 1 - Verify that you cannot edit the LDAP properties");
	testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/a[1]"), 3000);
   	testFuncs.searchStr(driver, "Unauthorized");
   	testFuncs.searchStr(driver, "You do not have permission to modify this item");
	
	// Step 2 - test the Test-LDAP button
	testFuncs.myDebugPrinting("Step 2 - test the Test-LDAP button");
	testFuncs.enterMenu(driver, "Setup_Phone_conf_system_settings_ldap", "LDAP Configuration");
	testFuncs.mySendKeys(driver, By.xpath("//*[@id='LDAP_SERVER_ADDRESS']"), ldapAddress , 2000);
	testFuncs.mySendKeys(driver, By.xpath("//*[@id='LDAP_USER_NAME']")     , ldapUsername, 2000);
	testFuncs.mySendKeys(driver, By.xpath("//*[@id='LDAP_PASSWORD']")      , ldapPassword, 2000);
	testFuncs.mySendKeys(driver, By.xpath("//*[@id='LDAP_BASE']")		   , Id		 	 , 2000);
	testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/table[2]/tbody/tr[2]/td[3]/a"), 3000);
	testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Please enter name for the LDAP test");
	testFuncs.mySendKeys(driver, By.xpath("/html/body/div[2]/div/input[1]"), "Test" + Id, 2000);
	testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]")				, 3000);
	
	// Nir 29\6\17 bug of 500-error (VI 145598)
   	Alert alert = driver.switchTo().alert();
    alert.accept(); 
    testFuncs.myWait(3000);
    
	// Login via Monitoring-user (tenant), an enter the LDAP menu
	testFuncs.myDebugPrinting("Login via Monitoring-user (tenant), an enter the LDAP menu");
	testFuncs.login(driver, testVars.getMonitTenUsername(), testVars.getMonitTenPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "Setup_Phone_conf_system_settings_ldap", "LDAP Configuration");

	// Step 3 - Verify that you cannot edit the LDAP properties
	testFuncs.myDebugPrinting("Step 3 - Verify that you cannot edit the LDAP properties");
	testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/a[1]"), 3000);
   	testFuncs.searchStr(driver, "Unauthorized");
   	testFuncs.searchStr(driver, "You do not have permission to modify this item");
	
	// Step 4 - test the Test-LDAP button
	testFuncs.myDebugPrinting("Step 4 - test the Test-LDAP button");
	testFuncs.enterMenu(driver, "Setup_Phone_conf_system_settings_ldap", "LDAP Configuration");
	testFuncs.mySendKeys(driver, By.xpath("//*[@id='LDAP_SERVER_ADDRESS']"), ldapAddress , 2000);
	testFuncs.mySendKeys(driver, By.xpath("//*[@id='LDAP_USER_NAME']")     , ldapUsername, 2000);
	testFuncs.mySendKeys(driver, By.xpath("//*[@id='LDAP_PASSWORD']")      , ldapPassword, 2000);
	testFuncs.mySendKeys(driver, By.xpath("//*[@id='LDAP_BASE']")		   , Id		 	 , 2000);
	testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/table[2]/tbody/tr[2]/td[3]/a"), 3000);
	testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Please enter name for the LDAP test");
	testFuncs.mySendKeys(driver, By.xpath("/html/body/div[2]/div/input[1]"), "Test" + Id, 2000);
	testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]")				, 3000);
	
	// Nir 29\6\17 bug of 500-error (VI 145598)
   	alert = driver.switchTo().alert();
    alert.accept(); 
    testFuncs.myWait(3000);	
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
