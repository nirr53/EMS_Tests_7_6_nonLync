package EMS_Tests;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.*;

/**
* ----------------
* This test tests the delete of device status when delete a user.
* ----------------
* Tests:
*  	 - Login the system via an Administrator user.
*    - Create two users via a POST query.
*    1. Delete a user via Manage users-changes menu.
*    2. Delete a user via Manage multiple-users-changes menu.
*  
* Results:
*    1+2. In both cases, the device status should be erased from the device-status menu after delete.
*  
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test133__delete_device_status_tests {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test133__delete_device_status_tests(String browser) {
	  
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
  public void Delete_device_status_tests() throws Exception {
	  
	  Log.startTestCase(this.getClass().getName());
	  
	  // Set variables + login
	  Map<String, String> map = new HashMap<String, String>();
	  String prefix    = "deldevstts" + testFuncs.getId();
	  String userName1 = prefix + "_1";
	  String userName2 = prefix + "_2";
	  String usersNumber   	= "2";
	  testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser); 

	  // Create users with a POST query
	  testFuncs.myDebugPrinting("Create users with a POST query");
	  testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	  testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()       ,
					 										   testVars.getPort()     ,
					 									       "2"				      ,
					 									       prefix            ,
					 										   testVars.getDomain()   ,
					 										   "registered"           ,
					 										   "420HD"                ,
					 										   testVars.getDefTenant(),
					 										   "location");
	  testFuncs.verifyPostUsersCreate(driver,  prefix,  prefix, true, Integer.valueOf(usersNumber));

	  // Step 1 - Delete a user via Manage user page
	  testFuncs.myDebugPrinting("Step 1 - Delete a user via Manage user page");
	  testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	  testFuncs.searchUser(driver, userName1); 
	  deleteUserManMenu(userName1 + "@" + testVars.getDomain());
	  
	  // Verify that device-status was also deleted
	  testFuncs.myDebugPrinting("Verify that device-status was also deleted", testVars.logerVars.NORMAL);
	  verifyDeleteDevStatus(userName1);
	     
	  // Step 2 - Delete a user via Manage-multiple-users menu
	  testFuncs.myDebugPrinting("Step 2 - Delete a user via Manage-multiple-users menu");		
	  testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");  
	  testFuncs.selectMultipleUsers(driver, prefix, "1");
	  map.put("usersPrefix"	  , prefix);  
	  map.put("usersNumber"	  , "1"); 
	  map.put("startIdx"   	  , String.valueOf("1"));	  
	  map.put("srcUsername"	  , "Finished");	  
	  map.put("action"	 	  , "Delete Users");	  
	  map.put("skipVerifyDelete", "true");	  
	  testFuncs.setMultipleUsersAction(driver, map);
	  testFuncs.searchStr(driver, userName2 + "@" + testVars.getDomain() + " Finished");
	  
	  // Verify that device-status was also deleted
	  testFuncs.myDebugPrinting("Verify that device-status was also deleted", testVars.logerVars.NORMAL);
	  verifyDeleteDevStatus(userName2);
  }
  
  // Verify delete of device-status
  private void verifyDeleteDevStatus(String userName) {
	  
	  testFuncs.enterMenu(driver, "Monitor_device_status", "Devices Status");
	  testFuncs.myDebugPrinting("Verify that the device was also created", testVars.logerVars.MINOR);			    
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input"), "user:" + userName.trim(), 5000);			    
	  driver.findElement(By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input")).sendKeys(Keys.ENTER);	     
	  testFuncs.myWait(10000);
	  String txt = driver.findElement(By.tagName("body")).getText();			    
	  testFuncs.myAssertTrue("Device status were found !!\ntxt - " + txt, txt.contains("There are no devices that fit this search criteria")); 
  }

  // Delete users via Manage-Users menu
  private void deleteUserManMenu(String userName) {
	  
	  testFuncs.myClick(driver, By.xpath("//*[@id='results']/tbody/tr/td[9]/a[3]"), 2000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete the User " + userName);
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000);
	  driver.findElement(By.xpath("//*[@id='searchtext']")).clear();
	  driver.findElement(By.xpath("//*[@id='searchtext']")).sendKeys(userName);
	  driver.findElement(By.xpath("//*[@id='searchtext']")).sendKeys(Keys.ENTER);
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
