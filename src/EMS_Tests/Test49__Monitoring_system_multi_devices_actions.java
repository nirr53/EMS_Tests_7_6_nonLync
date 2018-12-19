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
import org.openqa.selenium.support.ui.Select;
import EMS_Tests.enumsClass.*;

/**
* ----------------
* This test tests the multiple device actions via a Monitoring user (system)
* ----------------
* Tests:
* 	 -  Login via Administrator, create a user with a POST query and logout
*    -  Login via a Monitoring user (system) and enter multiple-devices-changes menu
*    1. Verify that Delete-devices action is deactivated.
*    2. Verify that Change-phone-type action is deactivated.
*    3. Verify that Change-language action is deactivated.
* 	 4. Verify that Restart-devices action is deactivated.
* 	 5. Verify that Generate-configuration-users action is deactivated.
* 	 6. Verify that Update-configuration-users action is deactivated.
*    7. Verify that Send-message action is deactivated.
*    8. Verify that Change-firmware action is deactivated.
*    9. Verify that Change-VLAN-mode action is deactivated.
*    10. Logout. login as Administrator and delete the created user
* 
* Results:
*	 1-9. In all actions, in all modes, the selected button should stay inactive.
*	 10. User should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test49__Monitoring_system_multi_devices_actions {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test49__Monitoring_system_multi_devices_actions(browserTypes browser) {
	  
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
  public void Monitoring_multi_devices_actions() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String srcUserName      = "monitMultiDvcs" + testFuncs.getId();
	 
	// Login via Administrator, create a user with a POST query and logout
	testFuncs.myDebugPrinting("Login via Administrator, create a user with a POST query and add a device-PH to it");
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
	testFuncs.createUsers(testVars.getIp()			  ,
						  testVars.getPort() 	 	  ,
						  1							  ,	
						  srcUserName  		 		  ,
						  testVars.getDomain()		  ,
						  "registered"		  		  ,
						  testVars.getDefPhoneModel() ,
						  testVars.getDefTenant()     ,
						  testVars.getDefLocation());
	testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_GEN_INFOR_LOGOUT, testVars.getMainPageStr());
	
    // Login via a Monitoring user (system) and enter Multi-devices-changes menu
	testFuncs.myDebugPrinting("Login via a Monitoring user (system) and enter Multi-user-changes menu");
	testFuncs.login(driver, testVars.getMonitSysLoginData(enumsClass.loginData.USERNAME), testVars.getMonitSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_DEVICES, "Manage Multiple Devices");
    
    // Step 1 - Verify that Delete-devices action is deactivated.
  	testFuncs.myDebugPrinting("Step 1 - Verify that Delete-devices action is deactivated.");
    testFuncs.selectMultipleUsers(driver, srcUserName, "1");
	new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByValue("deleteDevices");
    testFuncs.myWait(5000);
	String deleteDevicesButton = driver.findElement(By.xpath("//*[@id='deleteDevicesTR']/td/div/a")).getAttribute("class");	
	testFuncs.myAssertTrue("Delete devices button is activated !!", deleteDevicesButton.contains("not-active"));
	
    // Step 2 - Verify that Change-IP-phone-type action is deactivated.
  	testFuncs.myDebugPrinting("Step 2 - Verify that Change-IP-phone-type action is deactivated.");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_DEVICES, "Manage Multiple Devices");
    testFuncs.selectMultipleUsers(driver, srcUserName, "1");
	new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByValue("changeType");
    testFuncs.myWait(5000);
	new Select(driver.findElement(By.xpath("//*[@id='ipptype']"))).selectByVisibleText("Audiocodes_420HD");
    testFuncs.myWait(5000);
	String changeIpPhoneTypeButton = driver.findElement(By.xpath("//*[@id='changeTypeTR']/td/div[1]/a[2]")).getAttribute("class");	
	testFuncs.myAssertTrue("Change IP phone Type button is activated !!", changeIpPhoneTypeButton.contains("not-active"));

	// Step 3 - Verify that Change-language action is deactivated.
	testFuncs.myDebugPrinting("Step 3 - Verify that Change-language action is deactivated.");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_DEVICES, "Manage Multiple Devices");
    testFuncs.selectMultipleUsers(driver, srcUserName, "1");
	new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByValue("changeLanguage");
    testFuncs.myWait(5000);
	new Select(driver.findElement(By.xpath("//*[@id='deviceLanguage']"))).selectByVisibleText("Hebrew");
    testFuncs.myWait(5000);
	String changeLangButton = driver.findElement(By.xpath("//*[@id='changeLanguageTR']/td/div[1]/a[2]")).getAttribute("class");	
	testFuncs.myAssertTrue("Change language button is activated !!", changeLangButton.contains("not-active"));
	
    // Step 4 - Verify that Restart-devices action is deactivated.
   	testFuncs.myDebugPrinting("Step 4 - Verify that Restart-devices action is deactivated.");  
   	String resModes[] = {"Graceful", "Force", "Scheduled"};
   	for (String resMode : resModes) {
   		
   		testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_DEVICES, "Manage Multiple Devices");
   	    testFuncs.selectMultipleUsers(driver, srcUserName, "1");
   		new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByValue("resetIpPhones");
   	    testFuncs.myWait(5000);  		
   		new Select(driver.findElement(By.xpath("//*[@id='resetIpPhonesTR']/td/div[1]/select"))).selectByValue(resMode);
   	    testFuncs.myWait(5000);
   		String resDeviceButton = driver.findElement(By.xpath("//*[@id='resetIpPhonesTR']/td/div[1]/a")).getAttribute("class");	
   		testFuncs.myAssertTrue("Reset device button is activated !!", resDeviceButton.contains("not-active"));  	
   	}
   	
   	// Step 5 - Verify that Generate-configuration-users action is deactivated.
  	testFuncs.myDebugPrinting("Step 5 - Verify that Generate-configuration-users action is deactivated.");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_DEVICES, "Manage Multiple Devices");
    testFuncs.selectMultipleUsers(driver, srcUserName, "1");
	new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByValue("setIpPhones");
    testFuncs.myWait(5000); 
	String generateConfButton = driver.findElement(By.xpath("//*[@id='setIpPhonesTR']/td/div/div/a")).getAttribute("class");	
	testFuncs.myAssertTrue("Generate configuration button is activated !!", generateConfButton.contains("not-active"));

	
    // Step 6 - Verify that Update-configuration-users action is deactivated.
  	testFuncs.myDebugPrinting("Step 6 - Verify that Update-configuration-users action is deactivated.");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_DEVICES, "Manage Multiple Devices");
    testFuncs.selectMultipleUsers(driver, srcUserName, "1");
	new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByValue("updateConfigFiles");
    testFuncs.myWait(5000);
	String updateConfButton = driver.findElement(By.xpath("//*[@id='updateConfigFilesTR']/td/div/a")).getAttribute("class");	
	testFuncs.myAssertTrue("Update configuration button is activated !!", updateConfButton.contains("not-active"));
      
    // Step 7 - Verify that Send-message action is deactivated.
  	testFuncs.myDebugPrinting("Step 7 - Verify that Send-message action is deactivated");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_DEVICES, "Manage Multiple Devices");
    testFuncs.selectMultipleUsers(driver, srcUserName, "1");
	new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByValue("sendMessage");
    testFuncs.myWait(5000);
    testFuncs.mySendKeys(driver, By.xpath("//*[@id='sendMessageTR']/td/div/input"), "message", 3000);
    String sendMessageButton = driver.findElement(By.xpath("//*[@id='sendMessageTR']/td/div/a")).getAttribute("class");	
	testFuncs.myAssertTrue("Send message button is activated !!", sendMessageButton.contains("not-active"));
    
	// Step 8 - Verify that Change-firmware action is deactivated.
	testFuncs.myDebugPrinting("Step 8 - Verify that Change-firmware action is deactivated.");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_DEVICES, "Manage Multiple Devices");
    testFuncs.selectMultipleUsers(driver, srcUserName, "1");
	new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByValue("updateFirmware");
    testFuncs.myWait(5000);
	new Select(driver.findElement(By.xpath("//*[@id='firmware_id']"))).selectByVisibleText("420HD");
    testFuncs.myWait(5000);   
    String changefirmwareButton = driver.findElement(By.xpath("//*[@id='updateFirmwareTR']/td/div[1]/a[2]")).getAttribute("class");	
	testFuncs.myAssertTrue("Change firmware button is activated !!", changefirmwareButton.contains("not-active"));
	
    // Step 9 - Verify that Change-VLAN action is deactivated.
 	testFuncs.myDebugPrinting("Step 9 - Verify that Change-VLAN action is deactivated.");  
 	String vlanModes[] = {"MANUAL", "CDP", "LLDP", "CDP_LLDP"};
 	for (String vlanMode : vlanModes) {
 		
 	 	testFuncs.myDebugPrinting("Check VLAN - " +  vlanMode, enumsClass.logModes.MINOR);
 		testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_DEVICES, "Manage Multiple Devices");
 	    testFuncs.selectMultipleUsers(driver, srcUserName, "1");
 		new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByValue("changeVlan");
 	    testFuncs.myWait(5000);  		
 		new Select(driver.findElement(By.xpath("//*[@id='changeVlanTR']/td/div[1]/select"))).selectByValue(vlanMode);
 	    testFuncs.myWait(5000);
 	    String changeVlanMode = driver.findElement(By.xpath("//*[@id='changeVlanTR']/td/div[1]/a[2]")).getAttribute("class");	
 		testFuncs.myAssertTrue("Change VLAN button is activated !!", changeVlanMode.contains("not-active"));
 	}
  
 	// Step 10 - Logout. login as Administrator and delete the created user
 	testFuncs.myDebugPrinting("Step 10 - Logout. login as Administrator and delete the created user");
	testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_GEN_INFOR_LOGOUT, testVars.getMainPageStr());
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");
	testFuncs.selectMultipleUsers(driver, srcUserName, "1");
	Map<String, String> map = new HashMap<String, String>();
	map.put("action"	      ,  "Delete Users");
	map.put("srcUsername"     ,  "Finished");
	map.put("usersPrefix"	  , srcUserName);
	map.put("skipVerifyDelete", "true");    
	map.put("startIdx"        ,  String.valueOf(1));
	map.put("usersNumber"     ,  "1"); 
	testFuncs.setMultipleUsersAction(driver, map);  
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
