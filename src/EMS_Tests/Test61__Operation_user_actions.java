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
* This test tests the users actions via an Operation User(system + tenant)
* ----------------
* Tests:
* 	 - Login via an Operation user (system) and enter the Users menu.
* 	 1. Create a user.
*    2. Add a device to the user.
* 	 3. Delete the device.
*    4. Delete the user.
*    
*    - Login via an Operation user (tenant) and enter the Users menu.
* 	 5. Create a user.
*    6. Add a device to the user.
* 	 7. Delete the device.
*    8. Delete the user.
* 
* Results:
*   1-8. All the operations should work as 'Administrator' user.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test61__Operation_user_actions {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test61__Operation_user_actions(browserTypes browser) {
	  
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
  public void Operation_user_actions() throws InterruptedException {
	  
	  Log.startTestCase(this.getClass().getName());
	  
	  // Set variables
	  String Id         = testFuncs.getId();
	  String userName   = "user"   + Id;
	  String deviceName = "device" + Id;
	  String usedTemp   = "NirTemplate430";
	  String usedFirm   = "430HD";
	  
	  // Step 1- Login via an Operation user (system) and create a user.
	  testFuncs.myDebugPrinting("Step 1- Login via an Operation user (system) and create a user.");
	  testFuncs.login(driver, testVars.getOperUsername(), testVars.getOperPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	  testFuncs.addUser(driver, userName, "1q2w3e$r", userName, testVars.getDefTenant());
	  
	  // Step 2 - Add a device to the created user
	  testFuncs.myDebugPrinting("Step 2 - Add a device to the created user");
	  testFuncs.addDevice(driver, userName, deviceName, usedTemp, testFuncs.getMacAddress(), usedFirm);
	  
	  // Step 3 - Delete device
	  testFuncs.myDebugPrinting("Step 3 - Delete device");
	  deleteDevice(driver, userName, deviceName);
	  
	  // Step 4 - Delete user
	  testFuncs.myDebugPrinting("Step 4 - Delete user");
	  deleteUser(driver, userName);  
	  
	  // Step 5 - Logout and re-login via an Operation user (system) and create a user
	  testFuncs.myDebugPrinting("Step 5 - Logout and re-login via an Operation user (system) and create a user");
	  testFuncs.enterMenu(driver, "General_Informatiom_logout", testVars.getMainPageStr());
	  testFuncs.login(driver, testVars.getOperTenUsername(), testVars.getOperTenPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	  testFuncs.addUser(driver, userName, "1q2w3e$r", userName, testVars.getDefTenant());
	  
	  // Step 6 - Add a device to the created user
	  testFuncs.myDebugPrinting("Step 6 - Add a device to the created user");
	  testFuncs.addDevice(driver, userName, deviceName, usedTemp, testFuncs.getMacAddress(), usedFirm);
	  
	  // Step 7 - Delete device
	  testFuncs.myDebugPrinting("Step 7 - Delete device");
	  deleteDevice(driver, userName, deviceName);
	  
	  // Step 8 - Delete user
	  testFuncs.myDebugPrinting("Step 8 - Delete user");
	  deleteUser(driver, userName);
  }
  
  /**
  *  Delete a device from Users menu by given username
  *  @param driver     - A given driver
  *  @param username   - user name
  *  @param deviceName - device name
  */
  private void deleteDevice(WebDriver driver, String userName, String deviceName) {
  
	  testFuncs.myClick(driver, By.xpath("//*[@id='tr" + userName + "device']/td[2]/table/tbody/tr[1]/td/div/table/tbody/tr[5]/td/a[2]"), 3000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete Device");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete device?");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 7000);
	  testFuncs.myAssertTrue("Device still exist!", !driver.findElement(By.tagName("body")).getText().contains(deviceName));  
  }
  
  /**
  *  Delete a user from Users menu by given username
  *  @param driver       - A given driver
  *  @param deviceName   - user name
  */
  private void deleteUser(WebDriver driver, String userName) {
	  
		testFuncs.searchUser(driver, userName); 
		testFuncs.myClick(driver, By.xpath("//*[@id='results']/tbody/tr/td[9]/a[3]"), 2000);
	    testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete the User " + userName);
		testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000);
	    driver.findElement(By.xpath("//*[@id='searchtext']")).clear();
	    driver.findElement(By.xpath("//*[@id='searchtext']")).sendKeys(userName);
	    testFuncs.myClick(driver, By.xpath("//*[@id='searchusersform']/div/div/div/div/span/a/button/span"), 7000); 
		testFuncs.searchStr(driver, "No users found");
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
