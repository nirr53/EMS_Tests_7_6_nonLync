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
* This test tests the login mechanism
* ----------------
* Tests:
* 	 1. Test the 'Log off' button.
* 	 2. Test the displayed version number
*    3. Test the 'Help' button
*    4. Test the Audiocodes product button
*    5. Test the upper links of main menu
*    6. Test the 'Home' button

* 
* Results:
*    1. 'Log off' should disconnect you from the system.
*    2. The current version number should be displayed.
*    3. Pressing the Home button should return you to the main menu.
*    4. Pressing the AC products button should open you a menu of Audicodes products.
*    5. Pressing the links, will lead you to System-Settings \ Alarms menus.
*    6. Pop-up with Help message should appear.
*    
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test3__home_screen_buttons {
	
  private WebDriver 	driver;
  private browserTypes  usedBrowser;
  private StringBuffer  verificationErrors = new StringBuffer();
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test3__home_screen_buttons(browserTypes browser) {
	  
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
  public void Home_screen_buttons() throws Exception {
	
	Log.startTestCase(this.getClass().getName());
	  
    // Step 3.1 - press the Log off button
	testFuncs.myDebugPrinting("Step 3.1 - press the Log off button");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_GEN_INFOR_LOGOUT, testVars.getMainPageStr());

    // Step 3.2 - Check version number
	testFuncs.myDebugPrinting("Step 3.2 - Check version number");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	String txt = driver.findElement(By.tagName("body")).getText();
	testFuncs.myAssertTrue("Version <" + testVars.getVersion() + "> was not detected !! (" + txt + ")", txt.contains(testVars.getVersion()));
	
	// Step 3.3 - Home button
	testFuncs.myDebugPrinting("Step 3.3 - Home button");
	testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_DASHBOARD_ALARMS, "Export");
	testFuncs.pressHomeButton(driver);
	
	// Step 3.4 - Audiocodes products button
	testFuncs.myDebugPrinting("Step 3.4 - Audiocodes products button");
	pressAudcProductsButton(driver);
	testFuncs.pressHomeButton(driver);
	
	// Step 3.5 - Main menu upper links
	testFuncs.myDebugPrinting("Step 3.5 - Main menu upper links");
	pressMainMenuUpperLinks(driver, "//*[@id='contentwrapper']/section/div[3]/div/div/div[2]/div[1]/div[1]/a", "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3", "System Settings");
	testFuncs.pressHomeButton(driver);
	pressMainMenuUpperLinks(driver, "//*[@id='contentwrapper']/section/div[3]/div/div/div[2]/div[1]/div[3]/a", "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3", "System Settings");
	testFuncs.pressHomeButton(driver);
	pressMainMenuUpperLinks(driver, "//*[@id='totalspan']/a", "//*[@id='trunkTBL']/div/div[1]/h3", "Alarms");
  
	// Step 3.6 - Help Button
	testFuncs.myDebugPrinting("Step 3.6 - Help button");
	pressHelpButton(driver);
  }

  // Check links
  private void pressMainMenuUpperLinks(WebDriver driver, String xpath, String headerXpath, String header) {
	  
	  testFuncs.myClick(driver, By.xpath(xpath), 5000);   
	  testFuncs.verifyStrByXpath(driver, headerXpath, header);
  }

  // Press the Audiocodes product button
  private void pressAudcProductsButton(WebDriver driver2) {
	
	  String winHandleBefore = driver.getWindowHandle();
	  testFuncs.myClick(driver, By.xpath("//*[@id='GuideBanner']/a/img"), 2000);    
	  for(String winHandle : driver.getWindowHandles()) {
	    	
	        driver.switchTo().window(winHandle);  
	  }
	  
	  // Verify login to the Products page
//	  testFuncs.searchStr(driver, "Management Products & Solutions");   
	  driver.close();
	  driver.switchTo().window(winHandleBefore);
  }

  // Press the Help button
  private void pressHelpButton(WebDriver driver) {
	  
	  // Test the Help button at right side of page
	  testFuncs.myDebugPrinting("Step 3.3.1 - Test the Help button at right side of page", enumsClass.logModes.NORMAL);
	  testFuncs.myClick(driver, By.xpath("//*[@id='navbar-collapse']/ul[3]/li[4]/a"), 8000);
	  for(String winHandle : driver.getWindowHandles()) {
	    	
	        driver.switchTo().window(winHandle);  
	  }
	  
	  // Verify Help headers
	  testFuncs.myDebugPrinting("Verify Help headers", enumsClass.logModes.MINOR);
	  testFuncs.searchStr(driver, "Welcome to AudioCodes Technical Document Library");  
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
