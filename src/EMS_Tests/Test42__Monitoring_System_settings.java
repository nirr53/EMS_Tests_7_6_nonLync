package EMS_Tests;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import EMS_Tests.enumsClass.*;

/**
* ----------------
* This test verify that you cannot edit the System-Settings values via Monitoring user of any kind.
* ----------------
* Tests:
* 	 1. Login via an Monitoring user (tenant) and the enter System settings menu
* 	    - Try to edit data and check default placeholder values.
*	 2. Login via an Monitoring user (system) and the enter System settings menu
* 	    - Try to edit data and check default placeholder values.
* 
* Results:
* 	 1+2.   The edit should failed.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test42__Monitoring_System_settings {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test42__Monitoring_System_settings(browserTypes browser) {
	  
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
  public void Monitoring_System_Settings_menu() throws Exception {
	
	  Log.startTestCase(this.getClass().getName());
	  
	  // Set variables
	  String[] langs = {"English", "Finnish", "French", "German", "Hebrew", "Italian", "Japanese", "Korean"};
	  String usedLang = langs[testFuncs.getNum(langs.length -1)];
	  testFuncs.myDebugPrinting("usedLang - " + usedLang);
	  
	  // Login via an Monitoring user (tenant) and the enter System settings menu
	  testFuncs.myDebugPrinting("Login via an Monitoring user (tenant) and the enter System settings menu");
	  testFuncs.login(driver, testVars.getMonitTenLoginData(enumsClass.loginData.USERNAME), testVars.getMonitTenLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "https://", this.usedBrowser);
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONF_SYSTEM_SETTINGS, "System Settings");

	  // Step 1 - Try to edit one of the values
	  testFuncs.myDebugPrinting("Step 1 - Try to edit one of the values");
	  Select sysLangs = new Select(driver.findElement(By.xpath("//*[@id='ipplanguage']")));
	  sysLangs.selectByVisibleText(usedLang);
	  testFuncs.myWait(5000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button"), 2000);
	  testFuncs.searchStr(driver, "Unauthorized");
	  testFuncs.searchStr(driver, "You do not have permission to modify this item");
	  
	  // Logout and re-login via an Monitoring user (system) and the enter System settings menu
	  testFuncs.myDebugPrinting("Logout and re-login via an Monitoring user (system) and the enter System settings menu");
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_GEN_INFOR_LOGOUT, testVars.getMainPageStr());
	  testFuncs.login(driver, testVars.getMonitSysLoginData(enumsClass.loginData.USERNAME), testVars.getMonitSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "https://", this.usedBrowser);
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONF_SYSTEM_SETTINGS, "System Settings");

	  // Step 2 - Try to edit one of the values
	  testFuncs.myDebugPrinting("Step 2 - Try to edit one of the values");
	  sysLangs = new Select(driver.findElement(By.xpath("//*[@id='ipplanguage']")));
	  sysLangs.selectByVisibleText(usedLang);
	  testFuncs.myWait(5000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button"), 2000);
	  testFuncs.searchStr(driver, "Unauthorized");
	  testFuncs.searchStr(driver, "You do not have permission to modify this item");
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
