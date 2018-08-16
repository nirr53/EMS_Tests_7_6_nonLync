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
* This test tests the System license menu during Monitoring (system+tenant) and Operation (system+tenant) users.
* ----------------
* Tests:
*    - Login the system via Monitoring user (system)
*    1. Verify that the system-license menu is displayed properly.
*  
*    - Login the system via Monitoring user (tenant)
*    2. Verify that the system-license menu is NOT displayed.
*    
*    - Login the system via Operation user (system)
*    3. Verify that the system-license menu is displayed properly.
*    
*    - Login the system via Operation user (tenant)
*    4. Verify that the system-license menu is NOT displayed.
*    
*  Results:
*    1-4. License menu should be displayed only to system users (Monitoring and Operation).
*  
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test85__Monitoring_Opeartion_license {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test85__Monitoring_Opeartion_license(browserTypes browser) {
	  
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
  public void Monitoring_Operation_license() throws Exception {
	  
	  Log.startTestCase(this.getClass().getName());
	  
	  // Step 1 - Login the system via Monitoring user (system) and verify that the system-license menu is displayed properly
	  testFuncs.myDebugPrinting("Step 1 - Login the system via Monitoring user (system) and verify that the system-license menu is displayed properly");
	  testFuncs.login(driver, testVars.getMonitSysLoginData(enumsClass.loginData.USERNAME), testVars.getMonitSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "https://", this.usedBrowser);
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_SYSTEM_LICENSE, "License Properties");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div/table/thead/tr/th[2]", "Property");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div/table/thead/tr/th[3]", "Value");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div/table/thead/tr/th[4]", "Description"); 
	  
	  // Step 2 - Logout, re-login the system via Monitoring user (tenant) and verify that the system-license menu is not displayed
	  testFuncs.myDebugPrinting("Step 2 - Logout, re-login the system via Monitoring user (tenant) and verify that the system-license menu is not displayed");  
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_GEN_INFOR_LOGOUT, testVars.getMainPageStr());
	  testFuncs.login(driver, testVars.getMonitTenLoginData(enumsClass.loginData.USERNAME), testVars.getMonitTenLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "https://", this.usedBrowser);  
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_SYSTEM_SECTION, "System");
	  testFuncs.myAssertTrue("License menu is detected !!", !driver.findElement(By.tagName("body")).getText().contains("License"));
	
	  // Step 3 - Logout, re-login the system via Operation user (system) and verify that the system-license is displayed properly
	  testFuncs.myDebugPrinting("Step 3 - Logout, re-login the system via Operation user (system) and verify that the system-license is displayed properly");  
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_GEN_INFOR_LOGOUT, testVars.getMainPageStr());
	  testFuncs.login(driver, testVars.getOperSysLoginData(enumsClass.loginData.USERNAME), testVars.getOperSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "https://", this.usedBrowser);  
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_SYSTEM_LICENSE, "License Properties");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div/table/thead/tr/th[2]", "Property");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div/table/thead/tr/th[3]", "Value");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div/table/thead/tr/th[4]", "Description"); 

	  // Step 4 - Logout, re-login the system via Operation user (tenant) and verify that the system-license menu is not displayed
	  testFuncs.myDebugPrinting("Step 4 - Logout, re-login the system via Operation user (tenant) and verify that the system-license menu is not displayed");  
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_GEN_INFOR_LOGOUT, testVars.getMainPageStr());
	  testFuncs.login(driver, testVars.getOperTenLoginData(enumsClass.loginData.USERNAME), testVars.getOperTenLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "https://", this.usedBrowser);  
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_SYSTEM_SECTION, "System");
	  testFuncs.myAssertTrue("License menu is detected !!", !driver.findElement(By.tagName("body")).getText().contains("License"));
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
