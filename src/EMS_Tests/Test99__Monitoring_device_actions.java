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
* This test tests the actions of Device-Status menu via Monitoring-user (system+tenant)
* ----------------
* Tests:
* 	 - Login via an Administrator user and create a registered user using a POST query.
* 	 - Logout, re-login via Monitoring user (system) and enter the Device-Status menu.
* 	 - Search the user and try the following actions via 'Selected row actions' button.
* 	 1. Change Tenant via Device-status menu.
* 	 2. Update firmware via Device-status menu.
* 	 3. Reset phone via Device-status menu.
* 	 4. Generate phone configuration via Device-status menu.
* 	 5. Update phone configuration via Device-status menu.
* 	 6. Send message via Device-status menu.
*    7. Check the Telnet via Device-status menu.
*    8. Delete device via Device-status menu.
*    9. Logout, re-login as Administrator user and delete the created user.
*    
* 	 - Login via an Administrator user and create a registered user using a POST query.
* 	 - Logout, re-login via Monitoring user (tenant) and enter the Device-Status menu.
* 	 - Search the user and try the following actions via 'Selected row actions' button.
* 	 1. Change Tenant via Device-status menu.
* 	 2. Update firmware via Device-status menu.
* 	 3. Reset phone via Device-status menu.
* 	 4. Generate phone configuration via Device-status menu.
* 	 5. Update phone configuration via Device-status menu.
* 	 6. Send message via Device-status menu.
*    7. Check the Telnet via Device-status menu.
*    8. Delete device via Device-status menu.
*    9. Logout, re-login as Administrator user and delete the created user.
* 
* Results:
*    At both cases:
* 	 	1-8. All the actions should be deactivated via Monitoring user.
*    	  9. Delete user should end successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test99__Monitoring_device_actions {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test99__Monitoring_device_actions(String browser) {
	  
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
  public void Monitoring_System_Device_status_actions() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set vars
	String userName     = "deviceactions" + testFuncs.getId();
	Map<String, String> map = new HashMap<String, String>();
    map.put("usersNumber",  "1"); 
    map.put("startIdx"   ,  String.valueOf(1));
	
	// Login via an Administrator user and create a registered user using a POST query
	testFuncs.myDebugPrinting("Login via Administrator and create a user using POST query");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp(),
				 				testVars.getPort()    						 ,
				 				"1"				   							 ,
				 				userName		   						     ,
				 				testVars.getDomain()  						 ,
				 				"registered"          						 ,
				 				testVars.getDefPhoneModel()              	 ,
				 				testVars.getDefTenant()               		 ,
				 				"myLocation");
	testFuncs.verifyPostUserCreate(driver, userName, userName, true);
	
	// Logout, re-login via Monitoring user (system) and enter the Device-Status menu
	testFuncs.myDebugPrinting("Logout, re-login via Monitoring user (system) and enter the Device-Status menu");
	testFuncs.enterMenu(driver, "Monitoring_General_Informatiom_logout", testVars.getMainPageStr());
	testFuncs.login(driver, testVars.getMonitUsername(), testVars.getMonitPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver , "Monitor_device_status", "Devices Status");   
	
	// Step 1 - Change Tenant via Device-status menu
	testFuncs.myDebugPrinting("Step 1 - Change Tenant via Device-status menu");
	searchAndSelectDevice(driver, userName);
	changeDeviceTenant(userName, testVars.getNonDefTenant(0));
	
	// Step 2 - Update firmware via Device-status menu
	testFuncs.myDebugPrinting("Step 2 - Update firmware via Device-status menu");
	searchAndSelectDevice(driver, userName);
	updateDeviceFirmware(userName, testVars.getDefPhoneModel());
	
	// Step 3 - Reset phone via Device-status menu
	testFuncs.myDebugPrinting("Step 3 - Reset phone via Device-status menu");
	searchAndSelectDevice(driver, userName);
	resetDevice(userName);
	
	// Step 4 - Generate phone configuration via Device-status menu
	testFuncs.myDebugPrinting("Step 4 - Generate phone configuration via Device-status menu");
	searchAndSelectDevice(driver, userName);
	generateDevice(userName);
	
	// Step 5 - Update phone configuration via Device-status menu
	testFuncs.myDebugPrinting("Step 5 - UpdateGenerate phone configuration via Device-status menu");
	searchAndSelectDevice(driver, userName);
	updateDevice(userName);
	
	// Step 6 - Send message via Device-status menu
	testFuncs.myDebugPrinting("Step 6 - Send message via Device-status menu");
	searchAndSelectDevice(driver, userName);
	sendMessage(userName);
	
	// Step 7 - Check the Telnet via Device-status menu
	testFuncs.myDebugPrinting("Step 7 - Check the Telnet via Device-status menu");
	searchAndSelectDevice(driver, userName);
	checktelnet(userName, "watchdog_call_src_info.txt");
	
	// Step 8 - Delete device
	testFuncs.myDebugPrinting("Step 8 - Delete device");
	searchAndSelectDevice(driver, userName);
	deleteDevice(userName);
	
    // Step 9 - Logout, re-login as Administrator user and delete the created user
  	testFuncs.myDebugPrinting("Step 9 - Logout, re-login as Administrator user and delete the created user");
	testFuncs.enterMenu(driver, "Monitoring_General_Informatiom_logout", testVars.getMainPageStr());
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, userName, "1");
    map.put("usersPrefix"	  , userName);
    map.put("usersNumber"	  , "1"); 
    map.put("startIdx"   	  , "1");
    map.put("srcUsername"	  , "Finished");
    map.put("action"	 	  , "Delete Users");
    map.put("skipVerifyDelete", "true");
    testFuncs.setMultipleUsersAction(driver, map);
    testFuncs.searchStr(driver, userName + "@" + testVars.getDomain() + " Finished");
  } 
  
  @Test
  public void Monitoring_Tenant_Device_status_actions() throws Exception {
	 
	String Id           = testFuncs.getId();
	String userName     = "deviceactions" + Id;
	Map<String, String> map = new HashMap<String, String>();
    map.put("usersNumber",  "1"); 
    map.put("startIdx"   ,  String.valueOf(1));
	Log.startTestCase(this.getClass().getName());
	
	// Login via an Administrator user and create a registered user using a POST query
	testFuncs.myDebugPrinting("Login via Administrator and create a user using POST query");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp(),
				 				testVars.getPort()    						 ,
				 				"1"				   							 ,
				 				userName		   						     ,
				 				testVars.getDomain()  						 ,
				 				"registered"          						 ,
				 				testVars.getDefPhoneModel()              	 ,
				 				testVars.getDefTenant()               		 ,
				 				"myLocation");
	testFuncs.verifyPostUserCreate(driver, userName, userName, true);
	
	// Logout, re-login via Monitoring user (tenant) and enter the Device-Status menu
	testFuncs.myDebugPrinting("Logout, re-login via Monitoring user (tenant) and enter the Device-Status menu");
	testFuncs.enterMenu(driver, "Monitoring_General_Informatiom_logout", testVars.getMainPageStr());
	testFuncs.login(driver, testVars.getMonitTenUsername(), testVars.getMonitTenPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver , "Monitor_device_status", "Devices Status");   
	
	// Step 1 - Change Tenant via Device-status menu
	testFuncs.myDebugPrinting("Step 1 - Change Tenant via Device-status menu");
	searchAndSelectDevice(driver, userName);
	changeDeviceTenant(userName, testVars.getNonDefTenant(0));
	
	// Step 2 - Update firmware via Device-status menu
	testFuncs.myDebugPrinting("Step 2 - Update firmware via Device-status menu");
	searchAndSelectDevice(driver, userName);
	updateDeviceFirmware(userName, testVars.getDefPhoneModel());
	
	// Step 3 - Reset phone via Device-status menu
	testFuncs.myDebugPrinting("Step 3 - Reset phone via Device-status menu");
	searchAndSelectDevice(driver, userName);
	resetDevice(userName);
	
	// Step 4 - Generate phone configuration via Device-status menu
	testFuncs.myDebugPrinting("Step 4 - Generate phone configuration via Device-status menu");
	searchAndSelectDevice(driver, userName);
	generateDevice(userName);
	
	// Step 5 - Update phone configuration via Device-status menu
	testFuncs.myDebugPrinting("Step 5 - UpdateGenerate phone configuration via Device-status menu");
	searchAndSelectDevice(driver, userName);
	updateDevice(userName);
	
	// Step 6 - Send message via Device-status menu
	testFuncs.myDebugPrinting("Step 6 - Send message via Device-status menu");
	searchAndSelectDevice(driver, userName);
	sendMessage(userName);
	
	// Step 7 - Check the Telnet via Device-status menu
	testFuncs.myDebugPrinting("Step 7 - Check the Telnet via Device-status menu");
	searchAndSelectDevice(driver, userName);
	checktelnet(userName, "watchdog_call_src_info.txt");
	
	// Step 8 - Delete device
	testFuncs.myDebugPrinting("Step 8 - Delete device");
	searchAndSelectDevice(driver, userName);
	deleteDevice(userName);
	
    // Step 9 - Logout, re-login as Administrator user and delete the created user
  	testFuncs.myDebugPrinting("Step 9 - Logout, re-login as Administrator user and delete the created user");
	testFuncs.enterMenu(driver, "Monitoring_General_Informatiom_logout", testVars.getMainPageStr());
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, userName, "1");
    map.put("usersPrefix"	  , userName);
    map.put("usersNumber"	  , "1"); 
    map.put("startIdx"   	  , "1");
    map.put("srcUsername"	  , "Finished");
    map.put("action"	 	  , "Delete Users");
    map.put("skipVerifyDelete", "true");
    testFuncs.setMultipleUsersAction(driver, map);
    testFuncs.searchStr(driver, userName + "@" + testVars.getDomain() + " Finished");
  }
  
  // Delete device
  private void deleteDevice(String deviceName) {
	  
	  // Delete device
	  testFuncs.myDebugPrinting("Delete device", testVars.logerVars.NORMAL); 
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu1']/a"), 2000);
	  String buttonClass = driver.findElement(By.xpath("//*[@id='delete']")).getAttribute("class");
	  testFuncs.myAssertTrue("The action is active !!", buttonClass.contains("not-active")); 
  }

  // Check Telnet
  private void checktelnet(String deviceName, String command) {
	  
	  // Check Telnet
	  testFuncs.myDebugPrinting("Check Telnet", testVars.logerVars.NORMAL); 
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu1']/a"), 2000);
	  String buttonClass = driver.findElement(By.xpath("//*[@id='telnet']")).getAttribute("class");
	  testFuncs.myAssertTrue("The action is active !!", buttonClass.contains("not-active"));
  }

  // Send message
  private void sendMessage(String deviceName) {

	  // Send message
	  testFuncs.myDebugPrinting("Update device configuration", testVars.logerVars.NORMAL); 
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu1']/a"), 2000);
	  String buttonClass = driver.findElement(By.xpath("//*[@id='sendMsg']")).getAttribute("class");
	  testFuncs.myAssertTrue("The action is active !!", buttonClass.contains("not-active"));
  }

  // Update device configuration
  private void updateDevice(String deviceName) {

	  // Update device configuration
	  testFuncs.myDebugPrinting("Update device configuration", testVars.logerVars.NORMAL); 
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu1']/a"), 2000);
	  String buttonClass = driver.findElement(By.xpath("//*[@id='updateConfig']")).getAttribute("class");
	  testFuncs.myAssertTrue("The action is active !!", buttonClass.contains("not-active"));
  }

  // Generate device configuration
  private void generateDevice(String deviceName) {
	  
	  // Generate device configuration
	  testFuncs.myDebugPrinting("Generate device configuration", testVars.logerVars.NORMAL); 
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu1']/a"), 2000);	  
	  String buttonClass = driver.findElement(By.xpath("//*[@id='generateConfiguration']")).getAttribute("class");
	  testFuncs.myAssertTrue("The action is active !!", buttonClass.contains("not-active")); 
  }

  // Reset device via Device-Status menu
  private void resetDevice(String deviceName) {
	  
	  // Reset device
	  testFuncs.myDebugPrinting("Reset device", testVars.logerVars.NORMAL); 
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu1']/a"), 2000);	  
	  String buttonClass = driver.findElement(By.xpath("//*[@id='resetIPP']")).getAttribute("class");
	  testFuncs.myAssertTrue("The action is active !!", buttonClass.contains("not-active"));  
  }

  // Update device firmware via Device-Status menu
  private void updateDeviceFirmware(String deviceName, String firmware) {
	  
	  // Change device firmware
	  testFuncs.myDebugPrinting("Change device firmware", testVars.logerVars.NORMAL); 
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu1']/a"), 2000);	  
	  String buttonClass = driver.findElement(By.xpath("//*[@id='updateFirmware']")).getAttribute("class");
	  testFuncs.myAssertTrue("The action is active !!", buttonClass.contains("not-active"));     
  }

  // Change device tenant via Device-Status menu
  private void changeDeviceTenant(String deviceName, String nonDefTenant) {

	  // Change device tenant
	  testFuncs.myDebugPrinting("Change device tenant", testVars.logerVars.NORMAL); 
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu1']/a"), 2000);	  
	  String buttonClass = driver.findElement(By.xpath("//*[@id='changeTenant']")).getAttribute("class");
	  testFuncs.myAssertTrue("The action is active !!", buttonClass.contains("not-active"));
  }

  // Search for a device and select it via Select-All checkbox
  private void searchAndSelectDevice(WebDriver driver, String userName) {
	  
	  // Search device
	  testFuncs.myDebugPrinting("Search device", testVars.logerVars.NORMAL);
	  testFuncs.enterMenu(driver , "Monitor_device_status", "Devices Status");   
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input"), "user:" + userName.trim(), 5000);
	  driver.findElement(By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input")).sendKeys(Keys.ENTER);	        
	  testFuncs.verifyStrByXpath(driver, "//*[@id='table']/tbody[1]/tr/td[8]", userName.trim()); 
	  testFuncs.myWait(3000);
		 
	  // Select the searched device via check Select-All check-box
	  testFuncs.myDebugPrinting("Select the searched device via check Select-All check-box", testVars.logerVars.MINOR);
	  testFuncs.myClick(driver, By.xpath("//*[@id='selectall']"), 3000);
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
