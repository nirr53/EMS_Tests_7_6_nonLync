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
* This test verify that you cannot edit the System-Settings values via Operation user (Tenant) but can at Operation user (System).
* ----------------
* Tests:
* 	 1. Login via an Operation user (tenant) and the enter System settings menu
* 	    - Try to edit data and check default placeholder values.
* 	 2. Login via an Operation user (system) and the enter System settings menu
* 	    - Try to fill data and check default placeholder values.
*    3. Reset system language to English
* 
* Results:
* 	 1.   The edit should failed.
*    2+3. The edit should success.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test52__Operation_System_Settings {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test52__Operation_System_Settings(browserTypes browser) {
	  
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
  public void Operation_System_settings() throws Exception {
	  
	  Log.startTestCase(this.getClass().getName());
	  
	  // Set variables
	  String[] langs = {"English", "Finnish", "French", "German", "Hebrew", "Italian", "Japanese", "Korean"};
	  String usedLang = langs[testFuncs.getNum(langs.length -1)];
	  testFuncs.myDebugPrinting("usedLang - " + usedLang);
		
	  // Login via an Operation user (tenant) and the enter System settings menu
	  testFuncs.myDebugPrinting("Login via an Operation user (tenant) and the enter System settings menu");
	  testFuncs.login(driver, testVars.getOperTenUsername(), testVars.getOperTenPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONF_SYSTEM_SETTINGS, "System Settings");
	  
	  // Step 1 - Try to edit one of the values
	  testFuncs.myDebugPrinting("Step 1 - Try to edit one of the values");
	  Select sysLangs = new Select(driver.findElement(By.xpath("//*[@id='ipplanguage']")));
	  sysLangs.selectByVisibleText(usedLang);
	  testFuncs.myWait(5000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button"), 2000);
	  testFuncs.searchStr(driver, "Unauthorized");
	  testFuncs.searchStr(driver, "You do not have permission to modify this item");
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_GEN_INFOR_LOGOUT, testVars.getMainPageStr());  
	  
	  // Login via an Operation user (system) and the enter System settings menu
	  testFuncs.myDebugPrinting("Login via an Operation user (system) and the enter System settings menu");
	  testFuncs.login(driver, testVars.getOperUsername(), testVars.getOperPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONF_SYSTEM_SETTINGS, "System Settings");

	  // Step 2 - Try to edit one of the values
	  testFuncs.myDebugPrinting("Step 2 - Try to edit one of the values");
	  sysLangs = new Select(driver.findElement(By.xpath("//*[@id='ipplanguage']")));
	  sysLangs.selectByVisibleText(usedLang);
	  testFuncs.myWait(5000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button"), 2000); 
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Save general settings");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Server successfully updated.");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 2000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div/a[4]"), 3000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='placeholders_body']/tr[7]/td[3]" , usedLang);

	  //  Step 3 - Reset system language to English
	  testFuncs.myDebugPrinting(" Step 3 - Reset system language to English");
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONF_SYSTEM_SETTINGS, "System Settings");
	  sysLangs = new Select(driver.findElement(By.xpath("//*[@id='ipplanguage']")));
	  sysLangs.selectByVisibleText("English");
	  testFuncs.myWait(5000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button"), 5000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Save general settings");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Server successfully updated.");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 2000);
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
