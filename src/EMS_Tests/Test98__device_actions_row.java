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
import org.openqa.selenium.support.ui.Select;
import EMS_Tests.enumsClass.*;

/**
* ----------------
* This test tests the actions of Device-Status menu when select only one device
* ----------------
* Tests:
* 	 - Create a registered user using a POST query and enter the Device-Status menu.
* 	 - Search the user and try the following actions via 'Actions' button when only one device is selected.
* 	 1. Check a status via Device-status menu
* 	 2. Change Tenant via Device-status menu
* 	 3. Update firmware via Device-status menu
*    4  Open web admin via Device-status menu
*    5. Set a nickname via Device-status menu
* 	 6. Reset phone via Device-status menu
* 	 7. Generate phone configuration via Device-status menu
* 	 8. Update phone configuration via Device-status menu
* 	 9. Send message via Device-status menu
*    10. Check the Telnet via Device-status menu
*    11. Delete device via Device-status menu
*    12. Delete the created user
* 
* Results:
* 	 1-11. All the actions via the Device-Status menu should be performed successfully.
*      12. User should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test98__device_actions_row {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test98__device_actions_row(browserTypes browser) {
	  
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
  public void Device_status_actions() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String Id 			= testFuncs.getId();
	String userName     = "deviceactions" + Id;
	String nickname		= "nickname" 	  + Id;
	Map<String, String> map = new HashMap<String, String>();
    map.put("usersNumber",  "1"); 
    map.put("startIdx"   ,  String.valueOf(1));
	
	// Login via Administrator and create a user using POST query
	testFuncs.myDebugPrinting("Login via Administrator and create a user using POST query");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
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
	
//	// Step 4 - Open web admin via Device-status menu
//	testFuncs.myDebugPrinting("Step 4 - Open web admin via Device-status menu");
//	searchAndSelectDevice(driver, userName);
//	openWebAdminDevice();
	
	// Step 5 - Set a nickname via Device-status menu
	testFuncs.myDebugPrinting("Step 5 - Set a nickname via Device-status menu");
	searchAndSelectDevice(driver, userName);
	setDeviceNickname(userName, nickname);
	
	// Step 6 - Reset phone via Device-status menu
	testFuncs.myDebugPrinting("Step 6 - Reset phone via Device-status menu");
	searchAndSelectDevice(driver, userName);
	resetDevice(userName);
	
	// Step 7 - Generate phone configuration via Device-status menu
	testFuncs.myDebugPrinting("Step 7 - Generate phone configuration via Device-status menu");
	searchAndSelectDevice(driver, userName);
	generateDevice(userName);
	
	// Step 8 - Update phone configuration via Device-status menu
	testFuncs.myDebugPrinting("Step 8 - UpdateGenerate phone configuration via Device-status menu");
	searchAndSelectDevice(driver, userName);
	updateDevice(userName);
	
	// Step 9 - Send message via Device-status menu
	testFuncs.myDebugPrinting("Step 9 - Send message via Device-status menu");
	searchAndSelectDevice(driver, userName);
	sendMessage(userName);
	
	// Step 10 - Check the Telnet via Device-status menu
	testFuncs.myDebugPrinting("Step 10 - Check the Telnet via Device-status menu");
	searchAndSelectDevice(driver, userName);
	checktelnet(userName, "watchdog_call_src_info.txt");
	
	// Step 11 - Delete device
	testFuncs.myDebugPrinting("Step 11 - Delete device");
	searchAndSelectDevice(driver, userName);
	deleteDevice(userName);
	
    // Step 12 - Delete the users
  	testFuncs.myDebugPrinting("Step 12 - Delete the created users");
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
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/ul/li[10]/a"), 2000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete IP Phones Status");  
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete the selected IP phones status?");  
	  testFuncs.myClick(driver, By.xpath("/html/body/div[4]/div/button[1]"), 7000); 
	  
	  // Verify delete
	  testFuncs.myDebugPrinting("Verify delete", enumsClass.logModes.MINOR); 
	  testFuncs.searchStr(driver, deviceName + " (" + testFuncs.readFile("mac_1.txt") + ") Finished");
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/button[1]"), 10000);
	  String txt = driver.findElement(By.tagName("body")).getText();
	  testFuncs.myAssertTrue("Device was not deleted !!\ntxt - " + txt, !txt.contains(deviceName));
  }

  // Check Telnet
  private void checktelnet(String deviceName, String command) {
	  
	  // Check Telnet
	  testFuncs.myDebugPrinting("Check Telnet", enumsClass.logModes.NORMAL); 
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/a"), 2000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/ul/li[11]/a"), 2000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Telnet");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']/table/tbody/tr[1]/td[1]", "CMD:");  
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']/table/tbody/tr[2]/td[1]", "CMDs:");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']/table/tbody/tr[3]/td[1]", "User Name");  
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']/table/tbody/tr[4]/td[1]", "Password:");  
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']/table/tbody/tr[5]/td[1]", "Wait for response:");  
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='mtext']"), "message", 3000);  
	  new Select(driver.findElement(By.xpath("//*[@id='mtime']"))).selectByVisibleText(command);
	  testFuncs.myWait(2000);
	  testFuncs.myClick(driver, By.xpath("/html/body/div[4]/div/button[1]"), 10000); 
	  
	  // Verify change
	  testFuncs.myDebugPrinting("Verify change", enumsClass.logModes.MINOR); 
	  testFuncs.searchStr(driver, deviceName + " (" + testFuncs.readFile("mac_1.txt") + ")");
  }

  // Send message
  private void sendMessage(String deviceName) {

	  // Send message
	  testFuncs.myDebugPrinting("Update device configuration", enumsClass.logModes.NORMAL); 
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/a"), 2000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/ul/li[9]/a"), 2000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Send Message");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']/div[1]/label", "Message Text");  
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']/div[2]/label", "Display Time");   
	  testFuncs.mySendKeys(driver, By.xpath("	//*[@id='mtext']"), "message", 3000);
	  testFuncs.myClick(driver, By.xpath("/html/body/div[4]/div/button[1]"), 10000); 
	  
	  // Verify change
	  testFuncs.myDebugPrinting("Verify change", enumsClass.logModes.MINOR); 
	  testFuncs.searchStr(driver, deviceName + " (" + testFuncs.readFile("mac_1.txt") + ")");
  }

  // Update device configuration
  private void updateDevice(String deviceName) {

	  // Update device configuration
	  testFuncs.myDebugPrinting("Update device configuration", enumsClass.logModes.NORMAL); 
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/a"), 2000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/ul/li[8]/a"), 2000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Update IPP IP-Phones Configuration");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to update the configuration files of the selected IP phones?\n" + deviceName);  
	  testFuncs.myClick(driver, By.xpath("/html/body/div[4]/div/button[1]"), 10000); 
	  
	  // Verify change
	  testFuncs.myDebugPrinting("Verify change", enumsClass.logModes.MINOR); 
	  testFuncs.searchStr(driver, deviceName + " (" + testFuncs.readFile("mac_1.txt") + ")");
  }

  // Generate device configuration
  private void generateDevice(String deviceName) {
	  
	  // Generate device configuration
	  testFuncs.myDebugPrinting("Generate device configuration", enumsClass.logModes.NORMAL); 
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/a"), 2000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/ul/li[7]/a"), 2000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Generate IPP IP-Phones Configuration");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to generate the configuration files of the selected IP phones?");  
	  testFuncs.myClick(driver, By.xpath("/html/body/div[4]/div/button[1]"), 4000); 
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Generate IPP IP-Phones Configuration");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Update devices now");  
	  testFuncs.myClick(driver, By.xpath("/html/body/div[4]/div/button[1]"), 10000);  
	  
	  // Verify change
	  testFuncs.myDebugPrinting("Verify change", enumsClass.logModes.MINOR); 
	  testFuncs.searchStr(driver, deviceName + " (" + testFuncs.readFile("mac_1.txt") + ")");  
  }

  // Reset device via Device-Status menu
  private void resetDevice(String deviceName) {
	  
	  // Reset device
	  testFuncs.myDebugPrinting("Reset device", enumsClass.logModes.NORMAL); 
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/a"), 2000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/ul/li[6]/a"), 2000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Restart IP-Phones");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to restart the selected IP phones?");  
	  testFuncs.myClick(driver, By.xpath("/html/body/div[4]/div/button[1]"), 10000);  
	  
	  // Verify change
	  testFuncs.myDebugPrinting("Verify change", enumsClass.logModes.MINOR); 
	  testFuncs.searchStr(driver, deviceName + " (" + testFuncs.readFile("mac_1.txt") + ")");    
  }
  
  // Set nickname
  private void setDeviceNickname(String deviceName, String nickname) {
	  
	  // Set nickname
	  testFuncs.myDebugPrinting("set nickname for device", enumsClass.logModes.NORMAL); 
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/a"), 2000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/ul/li[5]/a"), 2000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Update Nickname");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Nickname"); 
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='mtext']"), nickname, 3000);
	  testFuncs.myClick(driver, By.xpath("/html/body/div[4]/div/button[1]"), 10000);  
	  
	  // Verify change
	  testFuncs.myDebugPrinting("Verify change", enumsClass.logModes.MINOR); 
	  testFuncs.searchStr(driver, deviceName + " [" + nickname + "]");  
  }
  
  // Open Web Admin of device
  private void openWebAdminDevice() {
	  
	  // Reset device
	  testFuncs.myDebugPrinting("Open Web Admin of device", enumsClass.logModes.NORMAL); 
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/a"), 5000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/ul/li[4]/a"), 200000);
	  String ip = testFuncs.readFile("ip_1.txt");
	  testFuncs.myDebugPrinting("ip - " + ip, enumsClass.logModes.MINOR);
	  String parentHandle = driver.getWindowHandle();
	  ArrayList<?> tabs = new ArrayList<Object> (driver.getWindowHandles());
	  testFuncs.myWait(100000);
	  driver.switchTo().window((String) tabs.get(1));
	
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
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/ul/li[3]/a"), 2000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Update Firmware");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Please select a firmware:");  
	  new Select(driver.findElement(By.xpath("//*[@id='firmware']"))).selectByVisibleText(firmware);
	  testFuncs.myWait(2000);
	  testFuncs.myDebugPrinting("waiting", enumsClass.logModes.NORMAL); 
	  testFuncs.myClick(driver, By.xpath("/html/body/div[4]/div/button[1]"), 20000);
	  	  
	  // Verify change
	  testFuncs.myDebugPrinting("Verify change", enumsClass.logModes.MINOR); 
	  testFuncs.searchStr(driver, deviceName + " (" + testFuncs.readFile("mac_1.txt") + ") Finished"); 	  
  }

  // Change device tenant via Device-Status menu
  private void changeDeviceTenant(String deviceName, String nonDefTenant) {

	  // Change device tenant
	  testFuncs.myDebugPrinting("Change device tenant", enumsClass.logModes.NORMAL); 
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/a")	     , 5000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/ul/li[2]/a"), 5000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Change Tenant");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Please select a tenant:");
	  new Select(driver.findElement(By.xpath("/html/body/div[4]/div/select"))).selectByVisibleText(nonDefTenant);	
	  testFuncs.myWait(4000);
	  testFuncs.myClick(driver, By.xpath("/html/body/div[4]/div/button[1]"), 10000); 
	  	  
	  // Verify change
	  testFuncs.myDebugPrinting("Verify change", enumsClass.logModes.MINOR); 
	  testFuncs.searchStr(driver, deviceName);  
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
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input"), "user:" + userName.trim(), 7000);
	  driver.findElement(By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input")).sendKeys(Keys.ENTER);	        
	  testFuncs.myWait(7000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='table']/tbody[1]/tr/td[8]", userName.trim());
	  
	  // Select the searched device via check Select-All check-box
	  testFuncs.myDebugPrinting("Select the searched device via check Select-All check-box", enumsClass.logModes.MINOR);
	  testFuncs.myClick(driver, By.xpath("//*[@id='table']/tbody[1]/tr/td[2]/input"), 7000);  
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
