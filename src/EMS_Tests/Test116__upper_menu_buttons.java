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
* This test tests the new buttons of the top layer of the EMS-web-manager.
* ----------------
* Tests:
* 	 - Login the EMS.
* 	 1. Press the Generate Configuration button.
* 	 2. Press the Generate configuration menu.
* 	 3. Press the Network Topology button.
* 	 4. Press the EMS OVOC button.
* 
* Results:
* 	 1+2. The user should reach the Manage-multiple-users menu when 'Generate Configuration' action is selected.
* 	   3. User should reach the 'Network Topology' menu.
* 	   4. User should reach the 'EMS OVOC' login menu.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test116__upper_menu_buttons {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test116__upper_menu_buttons(browserTypes browser) {
	  
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
  public void Upper_menu_buttons() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Login
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	
    // Step 1 - Press the Generate Configuration button
	testFuncs.myDebugPrinting("Step 1 - Press the Generate Configuration button");
	testFuncs.myClick(driver, By.xpath("//*[@id='subHeader']/a[1]"), 5000);
	testFuncs.searchStr(driver, "Manage Multiple Users - Generate Configuration");	
	String selectAction = new Select(driver.findElement(By.xpath("//*[@id='action']"))).getFirstSelectedOption().getText();
	testFuncs.myDebugPrinting("selectAction - " + selectAction, enumsClass.logModes.MINOR);
	testFuncs.myAssertTrue("Generate configuration option is not selected !! (selectAction - " + selectAction + ")", selectAction.equals("Generate IP Phones Configuration Files"));
		
    // Step 2 - Press the 'Generate configuration' menu
	testFuncs.myDebugPrinting("Press the 'Generate configuration' menu");
	testFuncs.pressHomeButton(driver);
	testFuncs.enterMenu(driver, "Setup_user_configuration", "Manage Multiple Users - User Configuration");
	String selectAction2 = new Select(driver.findElement(By.xpath("//*[@id='action']"))).getFirstSelectedOption().getText();
	testFuncs.myDebugPrinting("selectAction2 - " + selectAction2, enumsClass.logModes.MINOR);
	testFuncs.myAssertTrue("Generate configuration option is not selected !! (selectAction2 - " + selectAction2 + ")", selectAction2.equals("User configuration"));
	   
	// Step 3 - Press the Network Topology button
	testFuncs.myDebugPrinting("Step 3 - Press the Network Topology button");
	testFuncs.pressNetworkTopologyButton(driver);
	
	// Step 3 - Press the Network Topology button
	testFuncs.myDebugPrinting("Step 3 - Press the Network Topology button");
	testFuncs.pressNetworkTopologyButton(driver);
	
	// Step 4 - Press the EMS-OVOC button
	testFuncs.myDebugPrinting("Step 4 - Press the EMS-OVOC button");
	pressEMSButton();
  }
  
  // Press the EMS Button
  private void pressEMSButton() {
		  
	  testFuncs.myWait(3000);		  
	  testFuncs.myClick(driver, By.xpath("/html/body/div[1]/header/a/img"), 7000);
	  for(String winHandle : driver.getWindowHandles()) {
	    	
	        driver.switchTo().window(winHandle);  
	  }
	  testFuncs.searchStr(driver, "USERNAME");
	  testFuncs.searchStr(driver, "PASSWORD");
	  testFuncs.searchStr(driver, "Log In"); 	  
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
