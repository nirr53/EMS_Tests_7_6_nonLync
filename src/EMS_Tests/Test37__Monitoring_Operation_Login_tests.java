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
* This test tests the login mechanism for Operation and Monitoring users
* ----------------
* Tests:
* 	 1.1 Try to login with a  valid   username and an invalid password to Monitoring user
* 	 1.2 Try to login with an invalid username and a  valid   password to Monitoring user
* 	 1.3 Try to login with an invalid username and an invalid password to Monitoring user
* 	 1.4 Try to login with an valid   username and a  valid   password to Monitoring user
* 	 2.1 Try to login with a  valid   username and an invalid password to Operation user
* 	 2.2 Try to login with an invalid username and a  valid   password to Operation user
* 	 2.3 Try to login with an invalid username and an invalid password to Operation user
* 	 2.4 Try to login with an valid   username and a  valid   password to Operation user
* 
* Results:
* 	 1-2. In all tests only a login with valid username and valid password should succeed.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test37__Monitoring_Operation_Login_tests {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test37__Monitoring_Operation_Login_tests(browserTypes browser) {
	  
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
  public void Monitoring_Operation_login() throws Exception {
	  
	  Log.startTestCase(this.getClass().getName());
	
//	  // Set invalid username and password
//	  String monInvUsername = testVars.getMonitSysLoginData(enumsClass.loginData.USERNAME).substring(1) ;
//	  String monInvPassword = testVars.getMonitSysLoginData(enumsClass.loginData.PASSWORD).substring(1);
//	  String opInvUsername  = testVars.getOperSysLoginData(enumsClass.loginData.USERNAME).substring(1);
//	  String opInvPassword  = testVars.getOperSysLoginData(enumsClass.loginData.PASSWORD).substring(1);
//    
//	  // Step 1.1 - invalid login - valid username + invalid password to Monitoring User
//	  testFuncs.myDebugPrinting("Step 1.1 - invalid login - valid username + invalid password to Monitoring User");
//	  testFuncs.login(driver, testVars.getMonitSysLoginData(enumsClass.loginData.USERNAME), monInvPassword, testVars.getFailLogStr(), "https://", this.usedBrowser);
//	
//	  // Step 1.2 - invalid login - invalid username + valid password to Monitoring User
//	  testFuncs.myDebugPrinting("Step 1.2 - invalid login - invalid username + valid password to Monitoring User");
//	  testFuncs.login(driver, monInvUsername, testVars.getMonitSysLoginData(enumsClass.loginData.PASSWORD), "Invalid user", "https://", this.usedBrowser);
//	
//	  // Step 1.3 - invalid login - invalid username + invalid password to Monitoring User
//	  testFuncs.myDebugPrinting("Step 1.3 - invalid login - invalid username + invalid password to Monitoring User");
//	  testFuncs.login(driver, monInvUsername, monInvPassword, "Invalid user", "https://", this.usedBrowser);
//	
//	  // Step 1.4 - valid login - valid username + valid password to Monitoring User
//	  testFuncs.myDebugPrinting("Step 1.4 - valid login - valid username + valid password to Monitoring User");
//	  testFuncs.login(driver, testVars.getMonitSysLoginData(enumsClass.loginData.USERNAME), testVars.getMonitSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "https://", this.usedBrowser);
//	  
//	  // Step 2.1 - invalid login - valid username + invalid password to Operation User
//	  testFuncs.myDebugPrinting("Step 2.1 - invalid login - valid username + invalid password to Operation User");
//	  testFuncs.login(driver, testVars.getOperSysLoginData(enumsClass.loginData.USERNAME), opInvPassword, testVars.getFailLogStr(), "https://", this.usedBrowser);
//	
//	  // Step 2.2 - invalid login - invalid username + valid password to Operation User
//	  testFuncs.myDebugPrinting("Step 2.2 - invalid login - invalid username + valid password to Operation User");
//	  testFuncs.login(driver, opInvUsername, testVars.getOperSysLoginData(enumsClass.loginData.USERNAME), "Invalid user", "https://", this.usedBrowser);
//	
//	  // Step 2.3 - invalid login - invalid username + invalid password to Operation User
//	  testFuncs.myDebugPrinting("Step 2.3 - invalid login - invalid username + invalid password to Operation User");
//	  testFuncs.login(driver, opInvUsername, opInvPassword, "Invalid user", "https://", this.usedBrowser);
//	
//	  // Step 2.4 - valid login - valid username + valid password to Operation User
//	  testFuncs.myDebugPrinting("Step 2.4 - valid login - valid username + valid password to Operation User");
//	  testFuncs.login(driver, testVars.getOperSysLoginData(enumsClass.loginData.USERNAME), testVars.getOperSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "https://", this.usedBrowser);  
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
