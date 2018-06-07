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
import org.openqa.selenium.*;import EMS_Tests.enumsClass.*;

/**
* ----------------
* This test tests the Device placeholders menu for monitoring users (system + tenant)
* ----------------
* Tests:
* 	 - Login via Administrator, create a user with a POST query and add a device-PH to it
* 	 - Log off, re-login via a Monitoring user (system) and enter the device-PH menu
* 	 1. Try to add a device-PH
* 	 2. Try to edit an existing device-PH
* 	 3. Try to delete an existing device-PH
* 
* 	 - Log off, re-login via a Monitoring user (tenant) and enter the device-PH menu
* 	 4. Try to add a device-PH
* 	 5. Try to edit an existing device-PH
* 	 6. Try to delete an existing device-PH
*
* 	 7. Log off, re-login as an Administrator and delete the created device placeholder and created user
* 
* Results:
* 	 1-6. Add, edit and delete actions should be disabled while we we login via a Monitoring user.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test47__Monitoring_device_placeholders {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test47__Monitoring_device_placeholders(browserTypes browser) {
	  
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
  public void Monitoring_Device_placeholders_menu() throws Exception {
	  
	  Log.startTestCase(this.getClass().getName());
	  
	  // Set variables
	  String srcUserName      =   "dvPHUsrTst" + testFuncs.getId();
	  Map<String, String> map = new HashMap<String, String>();
	  map.put("startIdx"   ,  String.valueOf(1));
	  map.put("usersNumber",  "1"); 
	  
	  // Login via Administrator, create a user with a POST query and add a device-PH to it
	  testFuncs.myDebugPrinting("Login via Administrator, create a user with a POST query and add a device-PH to it");
	  testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
	  testFuncs.createUserViaPost(testVars.getCrUserBatName(),
			  					  testVars.getIp()           ,
			  					  testVars.getPort()         ,
			  					  "1"		        		 ,
			  					  srcUserName  		         ,
			  					  testVars.getDomain()       ,
			  					  "registered"               ,
			  					  testVars.getDefPhoneModel(),
			  					  testVars.getDefTenant()    ,
				 				  "myLocation");
	  testFuncs.verifyPostUserCreate(driver, srcUserName + "@" + testVars.getDomain(), srcUserName, true);
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONFIGURATION_PHONE_DEVICE_PHS, "Manage Devices Placeholders");
	  String phName  = "DayLightActivate";
	  String phValue = "18";
	  testFuncs.addDevicePlaceholder(driver, srcUserName, phName, phValue);
	  
	  // Log off , re-login via a Monitoring user (system) and enter the device-PH menu
	  testFuncs.myDebugPrinting("Log off , re-login via a Monitoring user (system) and enter the device-PH menu");
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_GEN_INFOR_LOGOUT, testVars.getMainPageStr());
	  testFuncs.login(driver, testVars.getMonitUsername(), testVars.getMonitPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONFIGURATION_PHONE_DEVICE_PHS, "Manage Devices Placeholders");

	  // Step 1 - Try to add a device-PH
	  testFuncs.myDebugPrinting("Step 1 - Try to add a device-PH");
	  String addButton = driver.findElement(By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[2]/div[2]/a")).getAttribute("class");
	  testFuncs.myAssertTrue("Add device-PH button is not deactivated !! (" + addButton + ")", addButton.contains("not-active"));
	  
	  // Step 2 - Try to edit an existing device-PH
	  testFuncs.myDebugPrinting("Step 2 - Try to edit an existing device-PH");
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONFIGURATION_PHONE_DEVICE_PHS, "Manage Devices Placeholders");    
	  testFuncs.myClick(driver, By.xpath("//*[@id='placeholders_body']/tr/td[7]/button")				   , 6000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/form/div/div[3]/button[2]"), 6000);
	  testFuncs.searchStr(driver, "Unauthorized");
	  testFuncs.searchStr(driver, "You do not have permission to modify this item");
	  
	  // Step 3 - Try to delete an existing device-PH
	  testFuncs.myDebugPrinting("Step 3 - Try to delete an existing device-PH");
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONFIGURATION_PHONE_DEVICE_PHS, "Manage Devices Placeholders");  
	  String deleteButton = driver.findElement(By.xpath("//*[@id='placeholders_body']/tr[1]/td[8]/a")).getAttribute("class");
	  testFuncs.myAssertTrue("Delete device-PH button is not deactivated !! (" + deleteButton + ")", deleteButton.contains("not-active"));

	  // Log off , re-login via a Monitoring user (tenant) and enter the device-PH menu
	  testFuncs.myDebugPrinting("Log off , re-login via a Monitoring user (tenant) and enter the device-PH menu");
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_GEN_INFOR_LOGOUT, testVars.getMainPageStr());
	  testFuncs.login(driver, testVars.getMonitTenUsername(), testVars.getMonitTenPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONFIGURATION_PHONE_DEVICE_PHS, "Manage Devices Placeholders");

	  // Step 4 - Try to add a device-PH
	  testFuncs.myDebugPrinting("Step 4 - Try to add a device-PH");
	  addButton = driver.findElement(By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[2]/div[2]/a")).getAttribute("class");
	  testFuncs.myAssertTrue("Add device-PH button is not deactivated !! (" + addButton + ")", addButton.contains("not-active"));
	  
	  // Step 5 - Try to edit an existing device-PH
	  testFuncs.myDebugPrinting("Step 5 - Try to edit an existing device-PH");
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONFIGURATION_PHONE_DEVICE_PHS, "Manage Devices Placeholders");    
	  testFuncs.myClick(driver, By.xpath("//*[@id='placeholders_body']/tr/td[7]/button")				   , 6000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/form/div/div[3]/button[2]"), 6000);
	  testFuncs.searchStr(driver, "Unauthorized");
	  testFuncs.searchStr(driver, "You do not have permission to modify this item");
	  
	  // Step 6 - Try to delete an existing device-PH
	  testFuncs.myDebugPrinting("Step 6 - Try to delete an existing device-PH");
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONFIGURATION_PHONE_DEVICE_PHS, "Manage Devices Placeholders");  
	  deleteButton = driver.findElement(By.xpath("//*[@id='placeholders_body']/tr[1]/td[8]/a")).getAttribute("class");
	  testFuncs.myAssertTrue("Delete device-PH button is not deactivated !! (" + deleteButton + ")", deleteButton.contains("not-active"));
	  
	  // Step 7 - Log off, re-login as an Administrator and delete the created device placeholder and created user
	  testFuncs.myDebugPrinting("Step 7 - Log off, re-login as an Administrator and delete the created device placeholder and created user");
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_GEN_INFOR_LOGOUT, testVars.getMainPageStr());
	  testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONFIGURATION_PHONE_DEVICE_PHS, "Manage Devices Placeholders");  
	  deleteDevicePlaceholder(driver, srcUserName, phName, phValue);
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");
	  testFuncs.selectMultipleUsers(driver, srcUserName, "1");
	  map.put("action"	 ,  "Delete Users");
	  map.put("srcUsername",  "Finished");
	  map.put("usersPrefix"	  , srcUserName);
	  map.put("skipVerifyDelete", "true");
	  srcUserName = srcUserName.toLowerCase();
	  testFuncs.setMultipleUsersAction(driver, map);  
  }
  
  /**
   *  Delete an existing device placeholder
   *  @param driver     - given driver
   *  @param userName   - pre-create registered user
   *  @param phName     - existing placeholder name for edit
   *  @param phNewValue - new value for the placeholder
   */
   private void deleteDevicePlaceholder(WebDriver driver, String userName, String phName, String phNewValue) {
 	  
 	  // Delete the device Placeholder
	  testFuncs.myDebugPrinting("Delete the device Placeholder", enumsClass.logModes.MINOR);
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/div[2]/form/div/input"), userName, 2000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/div[2]/form/div/span/button"), 5000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='placeholders_body']/tr/td[8]/a"), 5000);  
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete item: " + phName);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete this value?");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);  
 	
 	  // Verify delete
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/div[2]/form/div/input"), userName, 2000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/div[2]/form/div/span/button"), 5000);
	  testFuncs.searchStr(driver, "There are no placeholders at present");
 	  String bodyText = driver.findElement(By.tagName("body")).getText();
 	  testFuncs.myAssertTrue("PH name is still detecetd !!\nbodyText - "  + bodyText, !bodyText.contains(phName));
 	  testFuncs.myAssertTrue("PH value is still detected !!\nbodyText - " + bodyText, !bodyText.contains(phNewValue));  
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
