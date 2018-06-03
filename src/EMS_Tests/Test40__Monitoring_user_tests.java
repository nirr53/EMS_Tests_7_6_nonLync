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
* This test verify that we cannot add\edit\delete user while we in Monitoring user (system and tenant)
* ----------------
* Tests:
* 	 - Login via a Monitoring (system) user and enter the Users menu
*    1. Try to add a user
*    2. Try to add / delete a device to an existing user (I.e System)
*    3. Try to edit a device to an existing user (I.e System)
*    
*    - Login via a Monitoring (Tenant) user and enter the Users menu
*    4. Try to add a user
*    5. Try to add / delete a device to an existing user (I.e System)
*    6. Try to edit a device to an existing user (I.e System)
* 
* Results:
*    1+4. Add user button should be deactivated.
*    2+5. Add / Delete device button should be deactivated.
*    3+6. Edit user should be active, but the Submit button should be deactivated.
*    
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test40__Monitoring_user_tests {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test40__Monitoring_user_tests(browserTypes browser) {
	  
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
  public void Monitoring_user_and_device_actions() throws Exception {
	

	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String sysUser = "system";		// This is the user default user
	
	// Login the system via a Monitoring user (system)
	testFuncs.myDebugPrinting("Login the system via a Monitoring user (system)");
	testFuncs.login(driver, testVars.getMonitUsername(), testVars.getMonitPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");

	// Step 1 - Try to add a user
  	testFuncs.myDebugPrinting("Step 1 - Try to add a user");
  	String addUserState = driver.findElement(By.xpath("//*[@id='trunkTBL']/div/div[1]/a")).getAttribute("class");
  	testFuncs.myAssertTrue("Add user button is activated !!", addUserState.contains("not-active"));	

	// Step 2 - Try to add / delete a device to an existing user (I.e System)
  	testFuncs.myDebugPrinting("Step 2 - Try to add / delete a device to an existing user (I.e System)");
  	testFuncs.searchUser(driver, sysUser);
  	String addDeviceState = driver.findElement(By.xpath("//*[@id='results']/tbody/tr/td[9]/a[1]")).getAttribute("class");
  	testFuncs.myAssertTrue("Add device button is activated !!", addDeviceState.contains("not-active"));	
 	String deleteDeviceState = driver.findElement(By.xpath("//*[@id='results']/tbody/tr/td[9]/a[3]")).getAttribute("class");
  	testFuncs.myAssertTrue("Delete device button is activated !!", deleteDeviceState.contains("not-active"));	

  	// Step 3 - Try to edit a device to an existing user (I.e System)
   	testFuncs.myDebugPrinting("Step 3 - Try to edit a device to an existing user (I.e System)");
  	testFuncs.searchUser(driver, sysUser);
   	testFuncs.myClick(driver, By.xpath("//*[@id='results']/tbody/tr/td[9]/a[2]"), 7000);
   	testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3/label", "Edit User DO NOT DELETE");
   	testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button[1]"), 7000);
   	testFuncs.searchStr(driver, "Unauthorized");
   	testFuncs.searchStr(driver, "You do not have permission to modify this item");	
   	
   	// Logout and re-login the system via a Monitoring user (tenant)
 	testFuncs.myDebugPrinting("Logout and re-login the system via a Monitoring user (tenant)");
	testFuncs.enterMenu(driver, "General_Informatiom_logout", testVars.getMainPageStr());
 	testFuncs.login(driver, testVars.getMonitTenUsername(), testVars.getMonitTenPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
 	testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");

 	// Step 4 - Try to add a user
   	testFuncs.myDebugPrinting("Step 4 - Try to add a user");
   	addUserState = driver.findElement(By.xpath("//*[@id='trunkTBL']/div/div[1]/a")).getAttribute("class");
   	testFuncs.myAssertTrue("Add user button is activated !!", addUserState.contains("not-active"));	

 	// Step 5 - Try to add / delete a device to an existing user (I.e System)
   	testFuncs.myDebugPrinting("Step 5 - Try to add / delete a device to an existing user (I.e System)");
   	addDeviceState = driver.findElement(By.xpath("//*[@id='results']/tbody/tr[1]/td[9]/a[1]")).getAttribute("class");
   	testFuncs.myAssertTrue("Add device button is activated !! <" + addDeviceState + ">", addDeviceState.contains("not-active"));	
  	deleteDeviceState = driver.findElement(By.xpath("//*[@id='results']/tbody/tr[1]/td[9]/a[3]")).getAttribute("class");
   	testFuncs.myAssertTrue("Delete device button is activated !! <" + deleteDeviceState + ">", deleteDeviceState.contains("not-active"));	

   	// Step 6 - Try to edit a device to an existing user
    testFuncs.myDebugPrinting("Step 6 - Try to edit a device to an existing user");
    testFuncs.myClick(driver, By.xpath("//*[@id='results']/tbody/tr/td[9]/a[2]"), 7000);
   	testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3/label", "Edit User");
    testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button[1]"), 7000);
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
