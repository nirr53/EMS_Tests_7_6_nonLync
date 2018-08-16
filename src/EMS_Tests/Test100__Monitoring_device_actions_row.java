package EMS_Tests;

import java.util.ArrayList;
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
import EMS_Tests.enumsClass.*;

/**
* ----------------
* This test tests the actions of Device-Status menu when select only one device and login via Monitoring user(system+tenant)
* ----------------
* Tests:
* 	 - Login via Administrator and create a registered user
* 	 - Logout, re-login via a Monitoring-user (system) and enter the Device-Status menu.
* 	 - Search the user and try the following actions via 'Actions' button when only one device is selected
* 	 1.  Check a status via Device-status menu
* 	 2.  Change Tenant via Device-status menu
* 	 3.  Update firmware via Device-status menu
*    4   Open web admin via Device-status menu
* 	 5.  Reset phone via Device-status menu
* 	 6.  Generate phone configuration via Device-status menu
* 	 7.  Update phone configuration via Device-status menu
* 	 8.  Send message via Device-status menu
*    9.  Check the Telnet via Device-status menu
*    10. Delete device via Device-status menu
*    11. Logout, re-login as Administrator and delete the created user
*    
*    - Login via Administrator and create a registered user
* 	 - Logout, re-login via a Monitoring-user (tenant) and enter the Device-Status menu.
* 	 - Search the user and try the following actions via 'Actions' button when only one device is selected
* 	 1.  Check a status via Device-status menu
* 	 2.  Change Tenant via Device-status menu
* 	 3.  Update firmware via Device-status menu
*    4   Open web admin via Device-status menu
* 	 5.  Reset phone via Device-status menu
* 	 6.  Generate phone configuration via Device-status menu
* 	 7.  Update phone configuration via Device-status menu
* 	 8.  Send message via Device-status menu
*    9.  Check the Telnet via Device-status menu
*    10. Delete device via Device-status menu
*    11. Logout, re-login as Administrator and delete the created user
* 
* Results:
*    In both cases:
* 	 	1-10. All the actions except check-status and open-web-admin should be deactivated via Monitoring user.
* 	   	11.   Delete should end successfully
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test100__Monitoring_device_actions_row {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test100__Monitoring_device_actions_row(browserTypes browser) {
	  
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
  public void Monitoring_system_Device_status_actions() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String userName     	= "deviceactions" + testFuncs.getId();
	Map<String, String> map = new HashMap<String, String>();
    map.put("usersNumber",  "1"); 
    map.put("startIdx"   ,  String.valueOf(1));
	
	// Login via Administrator and create a registered user
	testFuncs.myDebugPrinting("Login via Administrator and create a registered user");
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
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
	
	// Logout, re-login via a Monitoring-user (system) and enter the Device-Status menu
	testFuncs.myDebugPrinting("Logout, re-login via a Monitoring-user (system) and enter the Device-Status menu");
	testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_MONITOR_GEN_INFOR_LOGOUT2, testVars.getMainPageStr());
	testFuncs.login(driver, testVars.getMonitSysLoginData(enumsClass.loginData.USERNAME), testVars.getMonitSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver , enumsClass.menuNames.MONITOR_DEVICE_STATUS, "Devices Status");  	
	
	// Step 1 - Check status via Device-status menu
	testFuncs.myDebugPrinting("Step 1 - Check status via Device-status menu");
	searchAndSelectDevice(driver, userName);
	checkStatusDevice();
	
	// Step 2 - Change Tenant via Device-status menu
	testFuncs.myDebugPrinting("Step 2 - Change Tenant via Device-status menu");
	searchAndSelectDevice(driver, userName);
	changeDeviceTenant(userName, testVars.getNonDefTenant(0));

	// Step 3 - Update firmware via Device-status menu
	testFuncs.myDebugPrinting("Step 3 - Update firmware via Device-status menu");
	searchAndSelectDevice(driver, userName);
	updateDeviceFirmware(userName, testVars.getDefPhoneModel());
	
	// Step 4 - Open web admin via Device-status menu
	testFuncs.myDebugPrinting("Step 4 - Open web admin via Device-status menu");
	searchAndSelectDevice(driver, userName);
	openWebAdminDevice();
	
	// Step 5 - Reset phone via Device-status menu
	testFuncs.myDebugPrinting("Step 5 - Reset phone via Device-status menu");
	searchAndSelectDevice(driver, userName);
	resetDevice(userName);
	
	// Step 6 - Generate phone configuration via Device-status menu
	testFuncs.myDebugPrinting("Step 6 - Generate phone configuration via Device-status menu");
	searchAndSelectDevice(driver, userName);
	generateDevice(userName);
	
	// Step 7 - Update phone configuration via Device-status menu
	testFuncs.myDebugPrinting("Step 7 - UpdateGenerate phone configuration via Device-status menu");
	searchAndSelectDevice(driver, userName);
	updateDevice(userName);
	
	// Step 8 - Send message via Device-status menu
	testFuncs.myDebugPrinting("Step 8 - Send message via Device-status menu");
	searchAndSelectDevice(driver, userName);
	sendMessage(userName);
	
	// Step 9 - Check the Telnet via Device-status menu
	testFuncs.myDebugPrinting("Step 9 - Check the Telnet via Device-status menu");
	searchAndSelectDevice(driver, userName);
	checktelnet(userName, "watchdog_call_src_info.txt");
	
	// Step 10 - Delete device
	testFuncs.myDebugPrinting("Step 10 - Delete device");
	searchAndSelectDevice(driver, userName);
	deleteDevice(userName);
	
    // Step 11 - Logout, re-login as Administrator and delete the created user
  	testFuncs.myDebugPrinting("Step 11 - Logout, re-login as Administrator and delete the created user");
	testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_MONITOR_GEN_INFOR_LOGOUT2, testVars.getMainPageStr());
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");
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
 
  @Ignore
  @Test
  public void Monitoring_tenant_Device_status_actions() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String userName     	= "deviceactions" + testFuncs.getId();
	Map<String, String> map = new HashMap<String, String>();
    map.put("usersNumber",  "1"); 
    map.put("startIdx"   ,  String.valueOf(1));
	
	// Login via Administrator and create a registered user
	testFuncs.myDebugPrinting("Login via Administrator and create a registered user");
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
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
	
	// Logout, re-login via a Monitoring-user (tenant) and enter the Device-Status menu
	testFuncs.myDebugPrinting("Logout, re-login via a Monitoring-user (tenant) and enter the Device-Status menu");
	testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_MONITOR_GEN_INFOR_LOGOUT2, testVars.getMainPageStr());
	testFuncs.login(driver, testVars.getMonitTenLoginData(enumsClass.loginData.USERNAME), testVars.getMonitTenLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver , enumsClass.menuNames.MONITOR_DEVICE_STATUS, "Devices Status");   
	
	// Step 1 - Check status via Device-status menu
	testFuncs.myDebugPrinting("Step 1 - Check status via Device-status menu");
	searchAndSelectDevice(driver, userName);
	checkStatusDevice();
	
	// Step 2 - Change Tenant via Device-status menu
	testFuncs.myDebugPrinting("Step 2 - Change Tenant via Device-status menu");
	searchAndSelectDevice(driver, userName);
	changeDeviceTenant(userName, testVars.getNonDefTenant(0));

	// Step 3 - Update firmware via Device-status menu
	testFuncs.myDebugPrinting("Step 3 - Update firmware via Device-status menu");
	searchAndSelectDevice(driver, userName);
	updateDeviceFirmware(userName, testVars.getDefPhoneModel());
	
	// Step 4 - Open web admin via Device-status menu
	testFuncs.myDebugPrinting("Step 4 - Open web admin via Device-status menu");
	searchAndSelectDevice(driver, userName);
	openWebAdminDevice();
	
	// Step 5 - Reset phone via Device-status menu
	testFuncs.myDebugPrinting("Step 5 - Reset phone via Device-status menu");
	searchAndSelectDevice(driver, userName);
	resetDevice(userName);
	
	// Step 6 - Generate phone configuration via Device-status menu
	testFuncs.myDebugPrinting("Step 6 - Generate phone configuration via Device-status menu");
	searchAndSelectDevice(driver, userName);
	generateDevice(userName);
	
	// Step 7 - Update phone configuration via Device-status menu
	testFuncs.myDebugPrinting("Step 7 - UpdateGenerate phone configuration via Device-status menu");
	searchAndSelectDevice(driver, userName);
	updateDevice(userName);
	
	// Step 8 - Send message via Device-status menu
	testFuncs.myDebugPrinting("Step 8 - Send message via Device-status menu");
	searchAndSelectDevice(driver, userName);
	sendMessage(userName);
	
	// Step 9 - Check the Telnet via Device-status menu
	testFuncs.myDebugPrinting("Step 9 - Check the Telnet via Device-status menu");
	searchAndSelectDevice(driver, userName);
	checktelnet(userName, "watchdog_call_src_info.txt");
	
	// Step 10 - Delete device
	testFuncs.myDebugPrinting("Step 10 - Delete device");
	searchAndSelectDevice(driver, userName);
	deleteDevice(userName);
	
    // Step 11 - Logout, re-login as Administrator and delete the created user
  	testFuncs.myDebugPrinting("Step 11 - Logout, re-login as Administrator and delete the created user");
	testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_MONITOR_GEN_INFOR_LOGOUT2, testVars.getMainPageStr());
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");
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
	  testFuncs.myDebugPrinting("Delete device", enumsClass.logModes.NORMAL); 
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/a"), 2000);
	  String buttonClass = driver.findElement(By.xpath("//*[@id='dl-menu']/ul/li[9]/a")).getAttribute("class");
	  testFuncs.myAssertTrue("The action is active !!", buttonClass.contains("not-active"));
  }

  // Check Telnet
  private void checktelnet(String deviceName, String command) {
	  
	  // Check Telnet
	  testFuncs.myDebugPrinting("Check Telnet", enumsClass.logModes.NORMAL); 
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/a"), 2000);
	  String buttonClass = driver.findElement(By.xpath("//*[@id='dl-menu']/ul/li[10]/a")).getAttribute("class");
	  testFuncs.myAssertTrue("The action is active !!", buttonClass.contains("not-active"));
  }

  // Send message
  private void sendMessage(String deviceName) {

	  // Send message
	  testFuncs.myDebugPrinting("Update device configuration", enumsClass.logModes.NORMAL); 
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/a"), 2000);
	  String buttonClass = driver.findElement(By.xpath("//*[@id='dl-menu']/ul/li[8]/a")).getAttribute("class");
	  testFuncs.myAssertTrue("The action is active !!", buttonClass.contains("not-active"));
  }

  // Update device configuration
  private void updateDevice(String deviceName) {

	  // Update device configuration
	  testFuncs.myDebugPrinting("Update device configuration", enumsClass.logModes.NORMAL); 
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/a"), 2000);
	  String buttonClass = driver.findElement(By.xpath("//*[@id='dl-menu']/ul/li[7]/a")).getAttribute("class");
	  testFuncs.myAssertTrue("The action is active !!", buttonClass.contains("not-active"));
  }

  // Generate device configuration
  private void generateDevice(String deviceName) {
	  
	  // Generate device configuration
	  testFuncs.myDebugPrinting("Generate device configuration", enumsClass.logModes.NORMAL); 
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/a"), 2000);
	  String buttonClass = driver.findElement(By.xpath("//*[@id='dl-menu']/ul/li[6]/a")).getAttribute("class");
	  testFuncs.myAssertTrue("The action is active !!", buttonClass.contains("not-active"));
  }

  // Reset device via Device-Status menu
  private void resetDevice(String deviceName) {
	  
	  // Reset device
	  testFuncs.myDebugPrinting("Reset device", enumsClass.logModes.NORMAL); 
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/a"), 2000);
	  String buttonClass = driver.findElement(By.xpath("//*[@id='dl-menu']/ul/li[5]/a")).getAttribute("class");
	  testFuncs.myAssertTrue("The action is active !!", buttonClass.contains("not-active"));
  }
  
  // Open Web Admin of device
  private void openWebAdminDevice() {
	  
	  // Reset device
	  testFuncs.myDebugPrinting("Open Web Admin of device", enumsClass.logModes.NORMAL); 
	  String parentHandle = driver.getWindowHandle();
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/a"), 2000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/ul/li[4]/a"), 20000);
	  String ip = testFuncs.readFile("ip_1.txt");
	  testFuncs.myDebugPrinting("ip - " + ip, enumsClass.logModes.MINOR);
	  ArrayList<?> tabs = new ArrayList<Object> (driver.getWindowHandles());
	  driver.switchTo().window((String) tabs.get(1));
	  testFuncs.myWait(100000);
	
	  // Verify the correct IP is opened
	  testFuncs.myDebugPrinting("Verify the correct IP is opened", enumsClass.logModes.MINOR); 
	  String txt = driver.getCurrentUrl();
	  testFuncs.myAssertTrue("The opened web-page is does not match the IP of the device !! \ntxt - " + txt, txt.contains(ip));
	  driver.close();
	  driver.switchTo().window(parentHandle);
  }

  // Update device firmware via Device-Status menu
  private void updateDeviceFirmware(String deviceName, String firmware) {
	  
	  // Change device firmware
	  testFuncs.myDebugPrinting("Change device firmware", enumsClass.logModes.NORMAL); 
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/a"), 2000);
	  String buttonClass = driver.findElement(By.xpath("//*[@id='dl-menu']/ul/li[3]/a")).getAttribute("class");
	  testFuncs.myAssertTrue("The action is active !!", buttonClass.contains("not-active"));
  }

  // Change device tenant via Device-Status menu
  private void changeDeviceTenant(String deviceName, String nonDefTenant) {

	  // Change device tenant
	  testFuncs.myDebugPrinting("Change device tenant", enumsClass.logModes.NORMAL); 
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/a"), 2000);
	  String buttonClass = driver.findElement(By.xpath("//*[@id='dl-menu']/ul/li[2]/a")).getAttribute("class");
	  testFuncs.myAssertTrue("The action is active !!", buttonClass.contains("not-active"));
  }
  
  // Check device status
  private void checkStatusDevice() {
	  
	  // Check device status
	  testFuncs.myDebugPrinting("Check device status", enumsClass.logModes.NORMAL); 
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/a"), 2000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/ul/li[1]/a"), 20000);  
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Status");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Error in Status check!");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[4]/div/button[1]"), 2000);  
  }

  // Search for a device and select it via Select-All checkbox
  private void searchAndSelectDevice(WebDriver driver, String userName) {
	  
	  // Search device
	  testFuncs.myDebugPrinting("Search device", enumsClass.logModes.NORMAL);
	  testFuncs.enterMenu(driver , enumsClass.menuNames.MONITOR_DEVICE_STATUS, "Devices Status");   
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input"), "user:" + userName.trim(), 5000);
	  driver.findElement(By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input")).sendKeys(Keys.ENTER);	        
	  testFuncs.myWait(7000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='table']/tbody[1]/tr/td[8]", userName.trim());
	  
	  // Select the searched device via check Select-All check-box
	  testFuncs.myDebugPrinting("Select the searched device via check Select-All check-box", enumsClass.logModes.MINOR);
	  testFuncs.myClick(driver, By.xpath("//*[@id='table']/tbody[1]/tr/td[2]/input"), 3000);  
  }

  @After
  public void tearDown() throws Exception {
	  
//    driver.quit();
    System.clearProperty("webdriver.chrome.driver");
	System.clearProperty("webdriver.ie.driver");
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
 
    	testFuncs.myFail(verificationErrorString);
    }
  }
}
